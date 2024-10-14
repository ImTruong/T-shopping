package com.market.tshopping.service;

import com.market.tshopping.entity.Cart;
import com.market.tshopping.entity.Image;
import com.market.tshopping.entity.Product;
import com.market.tshopping.entity.Users;
import com.market.tshopping.entity.keys.IdCart;
import com.market.tshopping.entity.keys.IdImage;
import com.market.tshopping.payload.dto.ProductDTO;
import com.market.tshopping.payload.response.CartResponse;
import com.market.tshopping.repository.CartRepository;
import com.market.tshopping.repository.UserRepository;
import com.market.tshopping.service.impl.CartService;
import com.market.tshopping.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserUtil userUtil;

    @Override
    public CartResponse getUserCart(){
        CartResponse cartResponse=new CartResponse(0,0);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails){
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            List<Cart> cartList=cartRepository.findCartsByUserUserName(username);
            cartResponse.setTotalItems(cartList.size());
            cartResponse.setTotalPrice(cartList.stream().mapToDouble(cart -> cart.getProduct().getPrice()).sum());
            cartResponse.setProductList(
                    cartList.stream().map(cart -> {
                        ProductDTO productDTO=new ProductDTO();
                        Product product=cart.getProduct();
                        productDTO.setQuantity(cart.getQuantity());
                        productDTO.setProductName(product.getProductName());
                        productDTO.setPrice(product.getPrice());
                        productDTO.setId(product.getId());
                        Optional<String> imageUrl = product.getImages().stream()
                                .filter(image -> "represent".equals(image.getDescription()))
                                .map(Image::getIdImage)
                                .map(IdImage::getImageUrl)
                                .findFirst();
                        imageUrl.ifPresent(url -> productDTO.setImages(Collections.singletonList(url)));
                        return productDTO;
                    }).toList()
            );
        }
        return cartResponse;
    }

    @Override
    public Boolean addProductToUserCart(int productId, Integer quantity) {

        Users user=userUtil.getUserFromAuthentication();
        if(user!=null){
            IdCart idCart=new IdCart(user.getId(),productId);
            Optional<Cart> oldCart=cartRepository.findById(idCart);
            if(oldCart.isPresent())quantity+=oldCart.get().getQuantity();
            Cart cart=new Cart(idCart,quantity);
            try{
                cartRepository.save(cart);
            }catch (Exception e){
                System.out.println(e);
                return false;
            }
        }
        return false;
    }

    @Override
    public Boolean deleteProductFromUserCart(int productId){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails){

            String username= ((UserDetails) authentication.getPrincipal()).getUsername();
            Users user=userRepository.findByUserName(username);
            IdCart idCart=new IdCart(user.getId(),productId);
            try{
                cartRepository.deleteById(idCart);
            }catch (Exception e){
                System.out.println(e);
                return false;
            }
        }

        return false;
    }
}
