package project.module.example.data.mapper;

import org.junit.jupiter.api.Test;
import project.module.example.data.domain.Address;
import project.module.example.data.dto.address.AddressDTO;
import project.module.example.data.dto.address.CreateAddressDTO;
import project.module.example.data.dto.address.UpdateAddressDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static project.module.example.config.Constants.Test.ADDRESS_CITY;
import static project.module.example.config.Constants.Test.ADDRESS_COUNTRY;
import static project.module.example.config.Constants.Test.ADDRESS_STREET;
import static project.module.example.config.Constants.Test.NEW_ADDRESS_CITY;

public class AddressMapperTest {

    private final AddressMapper addressMapper = new AddressMapper();

    @Test
    public void toCreateEntity_returnAddress_ok() {
        CreateAddressDTO createAddressDTO = new CreateAddressDTO();
        createAddressDTO.setStreet(ADDRESS_STREET);
        createAddressDTO.setCity(ADDRESS_CITY);
        createAddressDTO.setCountry(ADDRESS_COUNTRY);

        Address expectedAddress = new Address();
        expectedAddress.setStreet(ADDRESS_STREET);
        expectedAddress.setCity(ADDRESS_CITY);
        expectedAddress.setCountry(ADDRESS_COUNTRY);

        Address actualAddress = addressMapper.toCreateEntity(createAddressDTO);
        assertEquals(expectedAddress, actualAddress);
    }

    @Test
    public void toUpdateEntity_updateAddress_ok() {
        Address address = new Address();
        address.setStreet(ADDRESS_STREET);

        UpdateAddressDTO updateAddressDTO = new UpdateAddressDTO();
        updateAddressDTO.setCity(NEW_ADDRESS_CITY);

        Address expectedAddress = new Address();
        expectedAddress.setStreet(ADDRESS_STREET);
        expectedAddress.setCity(NEW_ADDRESS_CITY);

        Address actualAddress = addressMapper.toUpdateEntity(address, updateAddressDTO);
        assertEquals(expectedAddress, actualAddress);
    }

    @Test
    public void toDto_returnDTO_ok() {
        Address address = new Address();
        address.setStreet(ADDRESS_STREET);
        address.setCity(ADDRESS_CITY);
        address.setCountry(ADDRESS_COUNTRY);

        AddressDTO expectedAddressDTO = new AddressDTO();
        expectedAddressDTO.setStreet(ADDRESS_STREET);
        expectedAddressDTO.setCity(ADDRESS_CITY);
        expectedAddressDTO.setCountry(ADDRESS_COUNTRY);

        AddressDTO actualAddressDTO = addressMapper.toDto(address);
        assertEquals(expectedAddressDTO, actualAddressDTO);
    }

    @Test
    public void toDto_returnNull_ok() {
        AddressDTO actualAddressDTO = addressMapper.toDto(null);
        assertNull(actualAddressDTO);
    }

}
