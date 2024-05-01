package project.module.example.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.module.example.data.dto.user.CreateUserDTO;
import project.module.example.data.dto.user.PartialUpdateUserDTO;
import project.module.example.data.dto.user.SearchUserDTO;
import project.module.example.data.dto.user.UpdateUserDTO;
import project.module.example.data.dto.user.UserDTO;
import project.module.example.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return new ResponseEntity<>(userService.create(createUserDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        return new ResponseEntity<>(userService.update(id, updateUserDTO), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> particularUpdate(@PathVariable Long id,
                                                    @Valid @RequestBody PartialUpdateUserDTO partialUpdateUserDTO) {
        return new ResponseEntity<>(userService.particularUpdate(id, partialUpdateUserDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> search(@Valid @RequestBody SearchUserDTO searchUserDTO) {
        return new ResponseEntity<>(userService.search(searchUserDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
