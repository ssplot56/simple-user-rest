package project.module.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.module.example.config.exception.ApiException;
import project.module.example.config.exception.ErrorCode;
import project.module.example.data.domain.User;
import project.module.example.data.dto.user.CreateUserDTO;
import project.module.example.data.dto.user.PartialUpdateUserDTO;
import project.module.example.data.dto.user.SearchUserDTO;
import project.module.example.data.dto.user.UpdateUserDTO;
import project.module.example.data.dto.user.UserDTO;
import project.module.example.data.mapper.UserMapper;
import project.module.example.repository.UserRepository;
import project.module.example.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static project.module.example.config.Constants.Exception.EMAIL_ALREADY_EXIST;
import static project.module.example.config.Constants.Exception.NOT_VALID_AGE;
import static project.module.example.config.Constants.Exception.PHONE_NUMBER_ALREADY_EXIST;
import static project.module.example.config.Constants.Exception.USER_NOT_FOUND;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Value("${project.user.min-age-to-register}")
    private Long minAgeToRegister;

    @Override
    public UserDTO create(CreateUserDTO createUserDTO) {
        User user = userMapper.toCreateEntity(createUserDTO);
        setCreateCustomizations(user, createUserDTO);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDTO update(Long id, UpdateUserDTO updateUserDTO) {
        User user = userMapper.toUpdateEntity(findById(id), updateUserDTO);
        setUpdateCustomizations(user, updateUserDTO);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDTO particularUpdate(Long id, PartialUpdateUserDTO partialUpdateUserDTO) {
        User user = userMapper.toUpdateEntity(findById(id), partialUpdateUserDTO);
        setUpdateCustomizations(user, partialUpdateUserDTO);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getById(Long id) {
        return userMapper.toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> search(SearchUserDTO searchUserDTO) {
        return userRepository.search(searchUserDTO).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_ENTITY, USER_NOT_FOUND.formatted(id)));
    }

    private void setCreateCustomizations(User user, CreateUserDTO createUserDTO) {
        setEmail(user, createUserDTO.getEmail());
        setPhoneNumber(user, createUserDTO.getPhoneNumber());
        setBirthDate(user, createUserDTO.getBirthDate());
    }

    private void setUpdateCustomizations(User user, PartialUpdateUserDTO updateUserDTO) {
        updateEmail(user, updateUserDTO.getEmail());
        updatePhoneNumber(user, updateUserDTO.getPhoneNumber());
        updateBirthDate(user, updateUserDTO.getBirthDate());
    }

    private void setEmail(User user, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ApiException(ErrorCode.ALREADY_REGISTERED, EMAIL_ALREADY_EXIST);
        }

        user.setEmail(email);
    }

    private void setPhoneNumber(User user, String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new ApiException(ErrorCode.ALREADY_REGISTERED, PHONE_NUMBER_ALREADY_EXIST);
        }

        user.setPhoneNumber(phoneNumber);
    }

    private void setBirthDate(User user, LocalDate birthDate) {
        validateAge(birthDate);
        user.setBirthDate(birthDate);
    }

    private void updateEmail(User user, String email) {
        if (email != null) {
            setEmail(user, email);
        }
    }

    private void updatePhoneNumber(User user, String phoneNumber) {
        if (phoneNumber != null) {
            setPhoneNumber(user, phoneNumber);
        }
    }

    private void updateBirthDate(User user, LocalDate birthDate) {
        if (birthDate != null) {
            setBirthDate(user, birthDate);
        }
    }

    private void validateAge(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now().minusYears(minAgeToRegister))) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, NOT_VALID_AGE.formatted(minAgeToRegister));
        }
    }

}
