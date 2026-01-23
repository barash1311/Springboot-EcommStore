package com.ecommerce.backend.service;

import com.ecommerce.backend.Exceptions.ResourceNotFoundException;
import com.ecommerce.backend.dto.AddressRequest;
import com.ecommerce.backend.dto.AddressResponse;
import com.ecommerce.backend.entity.address.Address;
import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.repository.AddressRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImplementation implements  AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;

    @Override
    @Transactional
    public AddressResponse createAddress(AddressRequest request) {
        User user = authUtil.loggedInUser();

        // Map request to Address entity
        Address address = modelMapper.map(request, Address.class);
        address.setUser(user);

        // Save address first
        Address savedAddress = addressRepository.save(address);

        // Update user's address list
        List<Address> addressList = user.getAddresses();
        addressList.add(savedAddress);
        user.setAddresses(addressList);
        userRepository.save(user);

        return modelMapper.map(savedAddress, AddressResponse.class);
    }

    @Override
    public List<AddressResponse> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();

        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address", "addressId", addressId));

        return modelMapper.map(address, AddressResponse.class);
    }

    @Override
    public List<AddressResponse> getMyAddresses() {
        User user = authUtil.loggedInUser();
        List<Address> addresses = user.getAddresses();

        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(Long addressId, AddressRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address", "addressId", addressId));

        // Update address fields
        address.setStreet(request.getStreet());
        address.setBuildingName(request.getBuildingName());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPincode(request.getPincode());

        Address updatedAddress = addressRepository.save(address);

        // Update user's address list
        User user = address.getUser();
        user.getAddresses().removeIf(addr -> addr.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressResponse.class);
    }

    @Override
    @Transactional
    public String deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address", "addressId", addressId));

        // Remove from user's address list
        User user = address.getUser();
        user.getAddresses().removeIf(addr -> addr.getAddressId().equals(addressId));
        userRepository.save(user);

        // Delete the address
        addressRepository.delete(address);

        return "Address deleted successfully with addressId: " + addressId;
    }
}
