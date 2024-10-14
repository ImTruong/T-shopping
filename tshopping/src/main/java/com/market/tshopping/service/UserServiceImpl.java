package com.market.tshopping.service;

import com.market.tshopping.entity.Address;
import com.market.tshopping.entity.Role;
import com.market.tshopping.entity.Users;
import com.market.tshopping.payload.dto.AddressDTO;
import com.market.tshopping.payload.dto.UserDTO;
import com.market.tshopping.repository.AddressRepository;
import com.market.tshopping.repository.UserRepository;
import com.market.tshopping.service.impl.UserService;
import com.market.tshopping.utils.ObjectUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectUtil objectUtil;

    @Autowired
    AddressRepository addressRepository;

    public boolean preUpdateUser(UserDTO userDTO, AddressDTO addressDTO) {
        try {
            if(!objectUtil.isEmptyObject(addressDTO)){
                Address address;
                Optional<Users> userOptional=userRepository.findById(userDTO.getId());
                if(!userOptional.isPresent()) return false;
                Users user=userOptional.get();
                if(userDTO.getAddressId()!=null){
                    address=user.getAddress();
                }
                else address=new Address();
                modelMapper.map(addressDTO,address);
                try {
                    addressRepository.save(address);

                } catch (Exception e) {
                    System.out.println(e);
                    return false;
                }
                user.setAddress(address);
            }
        } catch (IllegalAccessException e) {
            return false;
        }
         return createOrUpdateUser(userDTO);
    }

    @Override
    public boolean createOrUpdateUser(UserDTO userDTO){
        try{
            Users user;
            if(userDTO.getId()!=null)
                user = userRepository.findById(userDTO.getId())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userDTO.getId()));
            else{
                user = new Users();
                Role role = new Role();
                role.setId(3);
                user.setRole(role);
            }
            Field[] fields = userDTO.getClass().getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                Object value=field.get(userDTO);
                if (value != null && (!(value instanceof String) || StringUtils.hasText((String) value))) {
                    try{
                        Field entityField = user.getClass().getDeclaredField(field.getName());
                        entityField.setAccessible(true);
                        if(field.getName().equals("password"))value=passwordEncoder.encode((String)value);
                        entityField.set(user,value);
                    }
                    catch(Exception e){}
                }
            }
            userRepository.save(user);
            return true;
        }catch (UsernameNotFoundException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            Users user=userRepository.findByUserName(username);
            UserDTO userDTO=modelMapper.map(user,UserDTO.class);
            Address address=user.getAddress();
            if(address!=null)userDTO.setAddressId(address.getId());
            return userDTO;
        }
        return null;
    }

}
