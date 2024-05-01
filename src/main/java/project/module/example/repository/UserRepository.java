package project.module.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.module.example.data.domain.User;
import project.module.example.data.dto.user.SearchUserDTO;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u " +
            "WHERE u.birthDate BETWEEN :#{#searchUserDTO.birthDateRange.startDate} " +
            "AND :#{#searchUserDTO.birthDateRange.endDate}")
    List<User> search(SearchUserDTO searchUserDTO);

}
