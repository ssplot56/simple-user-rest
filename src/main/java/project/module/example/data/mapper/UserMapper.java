package project.module.example.data.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.module.example.config.CommonUtils;
import project.module.example.data.domain.User;
import project.module.example.data.dto.user.CreateUserDTO;
import project.module.example.data.dto.user.PartialUpdateUserDTO;
import project.module.example.data.dto.user.UserDTO;

@Component
public class UserMapper {

    @Autowired
    private AddressMapper addressMapper;

    public User toCreateEntity(CreateUserDTO createUserDTO) {
        User user = new User();

        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setAddress(addressMapper.toCreateEntity(createUserDTO.getAddress()));

        return user;
    }

    public User toUpdateEntity(User user, PartialUpdateUserDTO updateUserDTO) {
        CommonUtils.If.setIfNotNull(updateUserDTO.getFirstName(), user::setFirstName);
        CommonUtils.If.setIfNotNull(updateUserDTO.getLastName(), user::setLastName);
        user.setAddress(addressMapper.toUpdateEntity(user.getAddress(), updateUserDTO.getAddress()));

        return user;
    }

    public UserDTO toDto(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setAddress(addressMapper.toDto(user.getAddress()));

        return userDTO;
    }

}
