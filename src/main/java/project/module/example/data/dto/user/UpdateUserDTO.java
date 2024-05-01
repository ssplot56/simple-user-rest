package project.module.example.data.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import project.module.example.config.Constants;
import project.module.example.data.dto.address.UpdateAddressDTO;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateUserDTO extends PartialUpdateUserDTO {

    @Email(message = Constants.Exception.NOT_CORRECT_EMAIL_FORMAT)
    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phoneNumber;

    @Past
    @NotNull
    private LocalDate birthDate;

    @NotNull
    private UpdateAddressDTO address;

}
