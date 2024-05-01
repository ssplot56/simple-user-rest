package project.module.example.data.dto.address;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AddressDTO {

    private String street;
    private String city;
    private String country;

}
