package project.module.example.data.mapper;

import org.springframework.stereotype.Component;
import project.module.example.config.CommonUtils;
import project.module.example.data.domain.Address;
import project.module.example.data.dto.address.AddressDTO;
import project.module.example.data.dto.address.CreateAddressDTO;
import project.module.example.data.dto.address.UpdateAddressDTO;

@Component
public class AddressMapper {

    public Address toCreateEntity(CreateAddressDTO createAddressDTO) {
        Address address = new Address();
        CommonUtils.If.setIfNotNull(createAddressDTO.getStreet(), address::setStreet);
        CommonUtils.If.setIfNotNull(createAddressDTO.getCity(), address::setCity);
        CommonUtils.If.setIfNotNull(createAddressDTO.getCountry(), address::setCountry);

        return address;
    }

    public Address toUpdateEntity(Address address, UpdateAddressDTO updateAddressDTO) {
        if (updateAddressDTO != null) {
            address = address == null ? new Address() : address;
            CommonUtils.If.setIfNotNull(updateAddressDTO.getStreet(), address::setStreet);
            CommonUtils.If.setIfNotNull(updateAddressDTO.getCity(), address::setCity);
            CommonUtils.If.setIfNotNull(updateAddressDTO.getCountry(), address::setCountry);
        }

        return address;
    }

    public AddressDTO toDto(Address address) {
        if (address == null) {
            return null;
        }

        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());

        return addressDTO;
    }

}
