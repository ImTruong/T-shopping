package com.market.tshopping.service;

import com.market.tshopping.entity.*;
import com.market.tshopping.entity.keys.IdImage;
import com.market.tshopping.payload.dto.ImageDTO;
import com.market.tshopping.payload.dto.ProductDTO;
import com.market.tshopping.payload.request.ProductSearchingRequest;
import com.market.tshopping.repository.CategoryRepository;
import com.market.tshopping.repository.ImageRepository;
import com.market.tshopping.repository.ProductRepository;
import com.market.tshopping.service.impl.FileService;
import com.market.tshopping.service.impl.ProductService;
import com.market.tshopping.utils.ListUtil;
import com.market.tshopping.utils.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private ListUtil listUtil;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ProductDTO getProductById(int productId){
        Optional<Product> optionalProduct=productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product= optionalProduct.get();
            ProductDTO productDTO=modelMapper.map(product,ProductDTO.class);
            Users users = product.getUser();
            String sellerName= users.getFirstName()+" "+ users.getLastName();
            productDTO.setSellerName(sellerName);
            String categoryName=product.getCategory().getCategoryName();
            productDTO.setCategoryName(categoryName);
            Set<RatingProduct> ratingProductSet=product.getRatingProducts();
            OptionalDouble optionalDoubleRating=ratingProductSet.stream().mapToDouble(RatingProduct::getStar).average();
            double rating= optionalDoubleRating.isPresent() ? optionalDoubleRating.getAsDouble() : 0.0;
            productDTO.setRating(rating);
            List<String> imageList = product.getImages().stream()
                    .map(Image::getIdImage)
                    .map(IdImage::getImageUrl)
                    .collect(Collectors.toList());
            productDTO.setImages(imageList);
            return productDTO;
        } else {
            return null;
        }
    }

    @Override
    public List<ProductDTO> getAllProduct() {
        List<Product> productList=productRepository.findAll();
        List<ProductDTO> productDTOList = productList
                .stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .collect(Collectors.toList());
        return productDTOList;
    }

    @Override
    public List<String> getAllProductBrand() {
        List<ProductDTO> productDTOList=getAllProduct();
        List<String> brandList=productDTOList.stream()
                .map(ProductDTO::getBrand)
                .distinct()
                .collect(Collectors.toList());
        return brandList;
    }

    @Override
    public Page<ProductDTO> searchProduct(Map<String, String> allParams,String category,String brand, Pageable pageable) {
        Page<ProductDTO> result= null;
        ProductSearchingRequest productSearchingRequest=new ProductSearchingRequest(
                stringUtil.isNullOrEmpty(allParams.get("productName")) ? "" : allParams.get("productName"),
                stringUtil.isNullOrEmpty(category) ? null : listUtil.JsonIntToList(category),
                stringUtil.isNullOrEmpty(brand) ? null : listUtil.JsonStringToList(brand),
                stringUtil.isNullOrEmpty(allParams.get("minPrice")) ? null : Integer.parseInt(allParams.get("minPrice")),
                stringUtil.isNullOrEmpty(allParams.get("maxPrice")) ? null : Integer.parseInt(allParams.get("maxPrice")),
                stringUtil.isNullOrEmpty(allParams.get("star")) ? null : Integer.parseInt(allParams.get("star"))
        );
        try {
            result = productRepository.searchProductsByCriteria(productSearchingRequest,pageable);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<ProductDTO> getSellerProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String userName = ((UserDetails) authentication.getPrincipal()).getUsername();
            List<Product> productList=productRepository.findProductsByUserUserName(userName);
            List<ProductDTO> productDTOList = productList.stream()
                    .map(product ->{
                        ProductDTO productDTO=modelMapper.map(product, ProductDTO.class);
                        productDTO.setCategoryName(product.getCategory().getCategoryName());
                        Image image=imageRepository.findImageByProductAndDescription(product,"represent");
                        if(image!=null)productDTO.setImages(Collections.singletonList(image.getIdImage().getImageUrl()));
                        else productDTO.setImages(null);
                        return productDTO;
                    } )
                    .collect(Collectors.toList());
            return productDTOList;
        }
        return null;
    }

    @Override
    public Boolean createOrUpdateProduct(ProductDTO productDTO,Integer sellerId) {
        try{
            Product product=new Product();
            if(sellerId==null)
                product = productRepository.findById(productDTO.getId()).orElseThrow(ChangeSetPersister.NotFoundException::new);
            else{
                Users user=new Users();
                user.setId(sellerId);
                product.setUser(user);
            }
            Field[] fields=productDTO.getClass().getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                Object value=field.get(productDTO);
                if (value != null && (!(value instanceof String) || StringUtils.hasText((String) value))) {
                    try {
                        if(field.getName().equals("categoryName")){
                            Category category=categoryRepository.findCategoriesByCategoryName((String)value);
                            product.setCategory(category);
                        }
                        else{
                            Field entityField = product.getClass().getDeclaredField(field.getName());
                            entityField.setAccessible(true);
                            entityField.set(product, value);
                        }
                    } catch (NoSuchFieldException e) {}
                }
            }
            productRepository.save(product);
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<ImageDTO> getProductImages(int productId) {
        List<Image> images=imageRepository.findImagesByProductId(productId);
        List<ImageDTO> imageDTOs = images.stream()
                .map(image -> new ImageDTO(image.getIdImage().getImageUrl(),image.getProduct().getId() ,image.getDescription()))
                .collect(Collectors.toList());
        return imageDTOs;
    }

    @Override
    public Boolean addProductImage(MultipartFile file,Integer productId,String description){
        try{
            try{
                fileService.saveFile(file);
            }catch (Exception e){
                System.out.println(e);
                return false;
            }

            Image image=new Image();
            IdImage idImage=new IdImage( productId,file.getOriginalFilename());
            image.setDescription(description);
            image.setIdImage(idImage);
            imageRepository.save(image);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean deleteProductImage(ImageDTO imageDTO) {
        IdImage idImage=new IdImage(imageDTO.getProductId(), imageDTO.getImageUrl());
        Image image=imageRepository.findById(idImage).orElseThrow(NoSuchElementException::new);
        try{
            fileService.deleteFile(imageDTO.getImageUrl());
            imageRepository.delete(image);
            return true;
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }

    @Override
    public Boolean changeProductRepresentImage(MultipartFile file,int productId) {
        Optional<Image> imageOptional=imageRepository.findImageByProductIdAndDescription(productId, "represent");
        if(imageOptional.isPresent()){
            try{
                Image currentImage=imageOptional.get();
                fileService.deleteFile(currentImage.getIdImage().getImageUrl());
                imageRepository.delete(currentImage);
                fileService.saveFile(file);
                IdImage idImage=new IdImage(productId, file.getOriginalFilename());
                Image image=new Image();
                image.setIdImage(idImage);
                image.setDescription("represent");
                imageRepository.save(image);
                return true;
            }catch (Exception e){
                System.out.println(e);
                return false;
            }
        }
        return false;
    }
}
