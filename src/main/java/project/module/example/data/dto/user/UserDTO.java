package project.module.example.data.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import project.module.example.data.dto.address.AddressDTO;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class UserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;
    private AddressDTO address;


}
