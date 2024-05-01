package project.module.example.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import project.module.example.config.Constants;
import project.module.example.config.exception.ApiException;
import project.module.example.data.domain.User;
import project.module.example.data.dto.user.CreateUserDTO;
import project.module.example.data.dto.user.PartialUpdateUserDTO;
import project.module.example.data.dto.user.SearchUserDTO;
import project.module.example.data.dto.user.UpdateUserDTO;
import project.module.example.data.dto.user.UserDTO;
import project.module.example.data.mapper.UserMapper;
import project.module.example.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.module.example.config.Constants.Exception.USER_NOT_FOUND;
import static project.module.example.config.Constants.Test.JOHN_BIRTH_DATE;
import static project.module.example.config.Constants.Test.JOHN_ID;
import static project.module.example.config.Constants.Test.JOHN_MAIL;
import static project.module.example.config.Constants.Test.JOHN_PHONE;
import static project.module.example.config.Constants.Test.NEW_BIRTH_DATE;
import static project.module.example.config.Constants.Test.NEW_FIRST_NAME;
import static project.module.example.config.Constants.Test.NEW_MAIL;
import static project.module.example.config.Constants.Test.NEW_PHONE;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    private CreateUserDTO createUserDTO;
    private UpdateUserDTO updateUserDTO;
    private PartialUpdateUserDTO partialUpdateUserDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(JOHN_ID);
        user.setEmail(JOHN_MAIL);
        user.setPhoneNumber(JOHN_PHONE);
        user.setBirthDate(JOHN_BIRTH_DATE);

        userDTO = new UserDTO();
        userDTO.setId(JOHN_ID);
        userDTO.setEmail(JOHN_MAIL);
        userDTO.setPhoneNumber(JOHN_PHONE);
        userDTO.setBirthDate(JOHN_BIRTH_DATE);

        createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail(JOHN_MAIL);
        createUserDTO.setPhoneNumber(JOHN_PHONE);
        createUserDTO.setBirthDate(JOHN_BIRTH_DATE);

        updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setEmail(NEW_MAIL);
        updateUserDTO.setPhoneNumber(NEW_PHONE);
        updateUserDTO.setBirthDate(NEW_BIRTH_DATE);

        partialUpdateUserDTO = new PartialUpdateUserDTO();
        partialUpdateUserDTO.setFirstName(NEW_FIRST_NAME);
        partialUpdateUserDTO.setEmail(NEW_MAIL);

        ReflectionTestUtils.setField(userService, "minAgeToRegister", 18L);
    }

    @Test
    void create_returnCreated_ok() {
        User userAfterMapper = new User();

        when(userMapper.toCreateEntity(createUserDTO)).thenReturn(userAfterMapper);
        when(userRepository.save(userAfterMapper)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO createdUser = userService.create(createUserDTO);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(createdUser.getPhoneNumber()).isEqualTo(userDTO.getPhoneNumber());
    }

    @Test
    void create_existingEmail_notOk() {
        when(userMapper.toCreateEntity(createUserDTO)).thenReturn(user);
        when(userRepository.existsByEmail(createUserDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(createUserDTO))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(Constants.Exception.EMAIL_ALREADY_EXIST);
    }

    @Test
    void create_existingPhoneNumber_notOk() {
        when(userMapper.toCreateEntity(createUserDTO)).thenReturn(user);
        when(userRepository.existsByPhoneNumber(createUserDTO.getPhoneNumber())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(createUserDTO))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(Constants.Exception.PHONE_NUMBER_ALREADY_EXIST);
    }

    @Test
    void create_invalidAge_notOk() {
        createUserDTO.setBirthDate(LocalDate.now().minusYears(17));
        when(userMapper.toCreateEntity(createUserDTO)).thenReturn(user);

        assertThatThrownBy(() -> userService.create(createUserDTO))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(Constants.Exception.NOT_VALID_AGE.formatted(18));
    }

    @Test
    void update_returnUpdatedUser_ok() {
        when(userRepository.findById(JOHN_ID)).thenReturn(Optional.of(user));
        when(userMapper.toUpdateEntity(user, updateUserDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserDTO());

        UserDTO result = userService.update(JOHN_ID, updateUserDTO);

        assertThat(result).isNotNull();

        verify(userRepository, times(1)).findById(JOHN_ID);
        verify(userMapper, times(1)).toUpdateEntity(user, updateUserDTO);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void particularUpdate_returnUpdatedUser_ok() {
        when(userRepository.findById(JOHN_ID)).thenReturn(Optional.of(user));
        when(userMapper.toUpdateEntity(user, partialUpdateUserDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserDTO());

        UserDTO updatedUser = userService.particularUpdate(JOHN_ID, partialUpdateUserDTO);

        assertThat(updatedUser).isNotNull();
        verify(userRepository).save(user);
    }

    @Test
    void particularUpdate_notExistingUser_notOk() {
        when(userRepository.findById(JOHN_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.particularUpdate(JOHN_ID, partialUpdateUserDTO))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(USER_NOT_FOUND.formatted(JOHN_ID));
    }

    @Test
    void getById_returnUser_ok() {
        when(userRepository.findById(JOHN_ID)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO targetUser = userService.getById(JOHN_ID);

        assertThat(targetUser).isNotNull();
        verify(userRepository).findById(JOHN_ID);
    }

    @Test
    void search_shouldReturnUsers_ok() {
        SearchUserDTO searchUserDTO = new SearchUserDTO();

        List<User> targetUsers = new ArrayList<>();
        targetUsers.add(user);
        targetUsers.add(new User());

        when(userRepository.search(searchUserDTO)).thenReturn(targetUsers);
        when(userMapper.toDto(Mockito.any(User.class))).thenReturn(new UserDTO());

        List<UserDTO> result = userService.search(searchUserDTO);

        assertThat(result.size()).isEqualTo(2);
        verify(userRepository).search(searchUserDTO);
    }

    @Test
    public void deleteById_deleted_ok() {
        userService.deleteById(JOHN_ID);

        verify(userRepository, times(1)).deleteById(JOHN_ID);
    }

}
