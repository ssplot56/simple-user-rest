package project.module.example.service;

import project.module.example.data.dto.user.CreateUserDTO;
import project.module.example.data.dto.user.PartialUpdateUserDTO;
import project.module.example.data.dto.user.SearchUserDTO;
import project.module.example.data.dto.user.UpdateUserDTO;
import project.module.example.data.dto.user.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO create(CreateUserDTO createUserDTO);

    UserDTO update(Long id, UpdateUserDTO updateUserDTO);

    UserDTO particularUpdate(Long id, PartialUpdateUserDTO partialUpdateUserDTO);

    UserDTO getById(Long id);

    List<UserDTO> search(SearchUserDTO searchUserDTO);

    void deleteById(Long id);

}
