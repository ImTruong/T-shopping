package com.market.tshopping.service;

import com.market.tshopping.entity.Address;
import com.market.tshopping.payload.dto.AddressDTO;
import com.market.tshopping.repository.AddressRepository;
import com.market.tshopping.service.impl.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public AddressDTO getAddressById(int addressId) {
        Optional<Address> addressOptional=addressRepository.findById(addressId);
        if(!addressOptional.isPresent()) return null;
        AddressDTO addressDTO=modelMapper.map(addressOptional.get(),AddressDTO.class);
        return addressDTO;
    }
}
