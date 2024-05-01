package project.module.example.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.module.example.data.domain.User;
import project.module.example.data.dto.user.SearchUserDTO;
import project.module.example.data.object.DateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static project.module.example.config.Constants.Test.JOHN_BIRTH_DATE;
import static project.module.example.config.Constants.Test.JOHN_FIRST_NAME;
import static project.module.example.config.Constants.Test.JOHN_MAIL;
import static project.module.example.config.Constants.Test.JOHN_PHONE;
import static project.module.example.config.Constants.Test.NEW_BIRTH_DATE;
import static project.module.example.config.Constants.Test.NEW_MAIL;
import static project.module.example.config.Constants.Test.NEW_PHONE;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName(JOHN_FIRST_NAME);
        user.setEmail(JOHN_MAIL);
        user.setPhoneNumber(JOHN_PHONE);
        user.setBirthDate(JOHN_BIRTH_DATE);

        userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void existByEmail_returnTrue_ok() {
        boolean expected = userRepository.existsByEmail(JOHN_MAIL);
        assertThat(expected).isTrue();
    }

    @Test
    void existByEmail_returnFalse_ok() {
        boolean expected = userRepository.existsByEmail(NEW_MAIL);
        assertThat(expected).isFalse();
    }

    @Test
    void existsByPhone_returnTrue_ok() {
        boolean expected = userRepository.existsByPhoneNumber(JOHN_PHONE);
        assertThat(expected).isTrue();
    }

    @Test
    void existByPhone_returnFalse_ok() {
        boolean expected = userRepository.existsByPhoneNumber(NEW_PHONE);
        assertThat(expected).isFalse();
    }

    @Test
    void search_returnUsersList_ok() {
        List<User> usersToSave = new ArrayList<>();

        User alice = new User();
        alice.setBirthDate(NEW_BIRTH_DATE);
        usersToSave.add(alice);

        User bob = new User();
        bob.setBirthDate(LocalDate.of(1970, 1, 1));
        usersToSave.add(bob);

        userRepository.saveAll(usersToSave);

        DateRange dateRange = new DateRange(NEW_BIRTH_DATE.minusYears(1), JOHN_BIRTH_DATE.plusYears(1));
        SearchUserDTO searchUserDTO = new SearchUserDTO();
        searchUserDTO.setBirthDateRange(dateRange);

        List<User> receivedUsers = userRepository.search(searchUserDTO);

        assertThat(receivedUsers.size()).isEqualTo(2);
        assertThat(receivedUsers.contains(bob)).isFalse();
        assertThat(receivedUsers.contains(alice)).isTrue();
        assertThat(receivedUsers.contains(user)).isTrue();
    }

}