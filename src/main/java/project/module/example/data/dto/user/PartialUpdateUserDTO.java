package project.module.example.data.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import project.module.example.data.dto.address.UpdateAddressDTO;

import java.time.LocalDate;

@Getter
@Setter
public class PartialUpdateUserDTO {

    @Email
    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @Past
    private LocalDate birthDate;

    private UpdateAddressDTO address;

}
