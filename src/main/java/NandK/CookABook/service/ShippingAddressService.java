package NandK.CookABook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.shippingAddress.ShippingAddressCreationRequest;
import NandK.CookABook.dto.request.shippingAddress.ShippingAddressUpdateRequest;
import NandK.CookABook.dto.response.shippingAddress.ShippingAddressResponse;
import NandK.CookABook.entity.ShippingAddress;
import NandK.CookABook.entity.User;
import NandK.CookABook.repository.ShippingAddressRepository;

@Service
public class ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;

    public ShippingAddressService(ShippingAddressRepository shippingAddressRepository) {
        this.shippingAddressRepository = shippingAddressRepository;
    }

    public ShippingAddress createShippingAddress(ShippingAddressCreationRequest request, User user) {

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setName(request.getName());
        shippingAddress.setPhoneNumber(request.getPhoneNumber());
        shippingAddress.setCity(request.getCity());
        shippingAddress.setDistrict(request.getDistrict());
        shippingAddress.setWard(request.getWard());
        shippingAddress.setAddress(request.getAddress());
        shippingAddress.setDefaultAddress(request.getDefaultAddress());
        shippingAddress.setUser(user);

        if (shippingAddress.getDefaultAddress()) {
            this.setDefaultShippingAddress(shippingAddress);
        }

        return this.shippingAddressRepository.save(shippingAddress);
    }

    public void setDefaultShippingAddress(ShippingAddress shippingAddress) {
        User user = shippingAddress.getUser();
        List<ShippingAddress> shippingAddresses = this.shippingAddressRepository.findByUser(user);
        for (ShippingAddress address : shippingAddresses) {
            address.setDefaultAddress(false);
            this.shippingAddressRepository.save(address);
        }
        shippingAddress.setDefaultAddress(true);
    }

    public ShippingAddressResponse convertToShippingAddressResponse(ShippingAddress shippingAddress) {
        ShippingAddressResponse shippingAddressResponse = new ShippingAddressResponse();
        shippingAddressResponse.setId(shippingAddress.getId());
        shippingAddressResponse.setName(shippingAddress.getName());
        shippingAddressResponse.setPhoneNumber(shippingAddress.getPhoneNumber());
        shippingAddressResponse.setCity(shippingAddress.getCity());
        shippingAddressResponse.setDistrict(shippingAddress.getDistrict());
        shippingAddressResponse.setWard(shippingAddress.getWard());
        shippingAddressResponse.setAddress(shippingAddress.getAddress());
        shippingAddressResponse.setDefaultAddress(shippingAddress.getDefaultAddress());
        shippingAddressResponse.setUserId(shippingAddress.getUser().getId());
        return shippingAddressResponse;
    }

    public List<ShippingAddressResponse> getShippingAddressesByUser(User user) {
        List<ShippingAddress> shippingAddresses = this.shippingAddressRepository.findByUser(user);
        List<ShippingAddressResponse> shippingAddressResponses = new ArrayList<>();
        for (ShippingAddress shippingAddress : shippingAddresses) {
            shippingAddressResponses.add(this.convertToShippingAddressResponse(shippingAddress));
        }
        return shippingAddressResponses;
    }

    public ShippingAddress getDefaultShippingAddressByUser(User user) {
        List<ShippingAddress> shippingAddresses = this.shippingAddressRepository.findByUser(user);
        for (ShippingAddress shippingAddress : shippingAddresses) {
            if (shippingAddress.getDefaultAddress()) {
                return shippingAddress;
            }
        }
        return null;
    }

    public ShippingAddress getShippingAddressById(Long shippingAddressId) {
        Optional<ShippingAddress> shippingAddress = this.shippingAddressRepository.findById(shippingAddressId);
        if (shippingAddress.isPresent()) {
            return shippingAddress.get();
        } else {
            return null;
        }
    }

    // TODO: add defaultAress
    public ShippingAddress updateShippingAddress(ShippingAddressUpdateRequest request) {
        ShippingAddress shippingAddress = this.getShippingAddressById(request.getId());
        if (shippingAddress != null) {
            if (request.getName() != null && !request.getName().isBlank()) {
                shippingAddress.setName(request.getName());
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
                shippingAddress.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getCity() != null && !request.getCity().isBlank()) {
                shippingAddress.setCity(request.getCity());
            }
            if (request.getDistrict() != null && !request.getDistrict().isBlank()) {
                shippingAddress.setDistrict(request.getDistrict());
            }
            if (request.getWard() != null && !request.getWard().isBlank()) {
                shippingAddress.setWard(request.getWard());
            }
            if (request.getAddress() != null && !request.getAddress().isBlank()) {
                shippingAddress.setAddress(request.getAddress());
            }
            if (shippingAddress.getDefaultAddress() == false && request.getDefaultAddress() == true) {
                this.setDefaultShippingAddress(shippingAddress);
            }
            return this.shippingAddressRepository.save(shippingAddress);
        } else {
            return null;
        }
    }

    public void deleteShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddressRepository.delete(shippingAddress);
    }
}
