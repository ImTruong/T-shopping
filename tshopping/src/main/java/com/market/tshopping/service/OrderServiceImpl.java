package com.market.tshopping.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.tshopping.entity.*;
import com.market.tshopping.entity.keys.IdOrderProduct;
import com.market.tshopping.payload.dto.ProductDTO;
import com.market.tshopping.payload.request.OrderCreateRequest;
import com.market.tshopping.payload.response.CheckOutResponse;
import com.market.tshopping.repository.AddressRepository;
import com.market.tshopping.repository.OrderProductRepository;
import com.market.tshopping.repository.OrderRepository;
import com.market.tshopping.repository.ProductRepository;
import com.market.tshopping.service.impl.OrderService;
import com.market.tshopping.utils.UserUtil;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Override
    public String createTemporaryOrder(List<ProductDTO> items) {
        String orderId = UUID.randomUUID().toString();
        String key = orderId;
        redisTemplate.opsForValue().set(key, items, 30, TimeUnit.MINUTES);
        return orderId;
    }

    @Override
    public CheckOutResponse getTemporaryOrder(String orderId) {
        CheckOutResponse checkOutResponse = new CheckOutResponse();
        String key = orderId;
        Object value = redisTemplate.opsForValue().get(key);
        List<ProductDTO> productList = objectMapper.convertValue(value, new TypeReference<List<ProductDTO>>() {});
        productList.forEach(
                productDTO -> {
                    Product product = productRepository.findById(productDTO.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productDTO.getId()));
                    productDTO.setProductName(product.getProductName());
                    productDTO.setPrice(product.getPrice() * productDTO.getQuantity());
                }
        );
        checkOutResponse.setProductList(productList);
        checkOutResponse.setTotalPrice(productList.stream().mapToDouble(ProductDTO::getPrice).sum());
        checkOutResponse.setTotalItems(productList.stream().mapToInt(ProductDTO::getQuantity).sum());
        return checkOutResponse;
    }

    @Override
    public Boolean deleteTemporaryOrder(String orderId) {
        String key = orderId;
        redisTemplate.delete(key);
        return true;
    }

    @Override
    public Boolean createOrder(OrderCreateRequest orderCreateRequest) {
        try{
            Order order = new Order();
            order.setStatus("Shipping");
            Address address;
            if(orderCreateRequest.getAddressId()!=null){
                address=new Address();
                address.setId(orderCreateRequest.getAddressId());
            }else{
                address = modelMapper.map(orderCreateRequest,Address.class);
                addressRepository.save(address);
            }
            order.setAddress(address);
            Users user = userUtil.getUserFromAuthentication();
            order.setUser(user);
            if(orderCreateRequest.getFirstName()!=null){
                String customerName = orderCreateRequest.getLastName() != null
                        ? orderCreateRequest.getFirstName() + " " + orderCreateRequest.getLastName()
                        : orderCreateRequest.getFirstName();
                order.setCustomerName(customerName);
                order.setCustomerPhoneNumber(orderCreateRequest.getPhoneNumber());
            }else{
                order.setCustomerName(user.getFirstName() + user.getLastName());
                order.setCustomerPhoneNumber(user.getPhoneNumber());
            }
            orderRepository.save(order);
            Object value = redisTemplate.opsForValue().get(orderCreateRequest.getTemporaryOrderId());
            List<ProductDTO> productList = objectMapper.convertValue(value, new TypeReference<List<ProductDTO>>() {});
            productList.forEach(
                    productDTO -> {
                        IdOrderProduct idOrderProduct=new IdOrderProduct(order.getId(), productDTO.getId());
                        OrderProduct orderProduct=new OrderProduct();
                        orderProduct.setIdOrderProduct(idOrderProduct);
                        orderProduct.setQuantity(productDTO.getQuantity());
                        orderProductRepository.save(orderProduct);
                    }
            );
            redisTemplate.delete(orderCreateRequest.getTemporaryOrderId());
        }catch (Exception e){
            System.out.println(e);
            return false;
        }


        return true;
    }

}
