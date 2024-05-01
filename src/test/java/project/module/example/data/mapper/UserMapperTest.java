package project.module.example.data.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.module.example.data.domain.Address;
import project.module.example.data.domain.User;
import project.module.example.data.dto.address.AddressDTO;
import project.module.example.data.dto.user.CreateUserDTO;
import project.module.example.data.dto.user.PartialUpdateUserDTO;
import project.module.example.data.dto.user.UpdateUserDTO;
import project.module.example.data.dto.user.UserDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static project.module.example.config.Constants.Test.JOHN_FIRST_NAME;
import static project.module.example.config.Constants.Test.JOHN_ID;
import static project.module.example.config.Constants.Test.JOHN_LAST_NAME;
import static project.module.example.config.Constants.Test.NEW_FIRST_NAME;
import static project.module.example.config.Constants.Test.NEW_LAST_NAME;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private UserMapper userMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName(JOHN_FIRST_NAME);
        user.setLastName(JOHN_LAST_NAME);
        user.setAddress(new Address());
    }

    @Test
    public void toCreateDTO_returnUser_ok() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setFirstName(JOHN_FIRST_NAME);
        createUserDTO.setLastName(JOHN_LAST_NAME);

        when(addressMapper.toCreateEntity(createUserDTO.getAddress())).thenReturn(new Address());

        User actualUser = userMapper.toCreateEntity(createUserDTO);
        assertEquals(user, actualUser);
    }

    @Test
    public void toUpdateDTO_updateOneField_ok() {
        PartialUpdateUserDTO updateUserDTO = new PartialUpdateUserDTO();
        updateUserDTO.setFirstName(NEW_FIRST_NAME);

        User expectedUser = new User();
        expectedUser.setFirstName(NEW_FIRST_NAME);
        expectedUser.setLastName(JOHN_LAST_NAME);
        expectedUser.setAddress(new Address());

        when(addressMapper.toUpdateEntity(user.getAddress(), updateUserDTO.getAddress())).thenReturn(new Address());

        User actualUser = userMapper.toUpdateEntity(user, updateUserDTO);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void toUpdateDTO_updateAllFields_ok() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName(NEW_FIRST_NAME);
        updateUserDTO.setLastName(NEW_LAST_NAME);

        User expectedUser = new User();
        expectedUser.setFirstName(NEW_FIRST_NAME);
        expectedUser.setLastName(NEW_LAST_NAME);
        expectedUser.setAddress(new Address());

        when(addressMapper.toUpdateEntity(user.getAddress(), updateUserDTO.getAddress())).thenReturn(new Address());

        User actualUser = userMapper.toUpdateEntity(user, updateUserDTO);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void toDto_returnDTO_ok() {
        User user = new User();
        user.setId(JOHN_ID);
        user.setFirstName(JOHN_FIRST_NAME);
        user.setLastName(JOHN_LAST_NAME);

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setId(JOHN_ID);
        expectedUserDTO.setFirstName(JOHN_FIRST_NAME);
        expectedUserDTO.setLastName(JOHN_LAST_NAME);
        expectedUserDTO.setAddress(new AddressDTO());

        when(addressMapper.toDto(user.getAddress())).thenReturn(new AddressDTO());

        UserDTO actualUserDTO = userMapper.toDto(user);
        assertEquals(expectedUserDTO, actualUserDTO);
    }

}
