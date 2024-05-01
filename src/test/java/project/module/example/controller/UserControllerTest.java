package project.module.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.module.example.data.dto.address.AddressDTO;
import project.module.example.data.dto.address.CreateAddressDTO;
import project.module.example.data.dto.address.UpdateAddressDTO;
import project.module.example.data.dto.user.CreateUserDTO;
import project.module.example.data.dto.user.PartialUpdateUserDTO;
import project.module.example.data.dto.user.SearchUserDTO;
import project.module.example.data.dto.user.UpdateUserDTO;
import project.module.example.data.dto.user.UserDTO;
import project.module.example.data.object.DateRange;
import project.module.example.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static project.module.example.config.Constants.Test.ADDRESS_CITY;
import static project.module.example.config.Constants.Test.ADDRESS_COUNTRY;
import static project.module.example.config.Constants.Test.ADDRESS_STREET;
import static project.module.example.config.Constants.Test.JOHN_BIRTH_DATE;
import static project.module.example.config.Constants.Test.JOHN_FIRST_NAME;
import static project.module.example.config.Constants.Test.JOHN_ID;
import static project.module.example.config.Constants.Test.JOHN_LAST_NAME;
import static project.module.example.config.Constants.Test.JOHN_MAIL;
import static project.module.example.config.Constants.Test.JOHN_PHONE;
import static project.module.example.config.Constants.Test.NEW_ADDRESS_CITY;
import static project.module.example.config.Constants.Test.NEW_ADDRESS_COUNTRY;
import static project.module.example.config.Constants.Test.NEW_ADDRESS_STREET;
import static project.module.example.config.Constants.Test.NEW_BIRTH_DATE;
import static project.module.example.config.Constants.Test.NEW_FIRST_NAME;
import static project.module.example.config.Constants.Test.NEW_LAST_NAME;
import static project.module.example.config.Constants.Test.NEW_MAIL;
import static project.module.example.config.Constants.Test.NEW_PHONE;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;

    @BeforeEach
    public void init() {
        initUserDTO();
    }

    @Test
    public void createUser_returnCreated_ok() throws Exception {
        CreateUserDTO createUserDTO = buildCreateUserDTO();
        given(userService.create(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));
        when(userService.create(any(CreateUserDTO.class))).thenReturn(userDTO);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(userDTO.getPhoneNumber()))
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(userDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$.address.city").value(userDTO.getAddress().getCity()))
                .andExpect(jsonPath("$.address.country").value(userDTO.getAddress().getCountry()))
                .andExpect(jsonPath("$.address.street").value(userDTO.getAddress().getStreet()));
    }

    @Test
    public void createUser_notValidEmail_notOk() throws Exception {
        CreateUserDTO createUserDTO = buildCreateUserDTO();
        createUserDTO.setEmail("notValidEmail");

        when(userService.create(any(CreateUserDTO.class))).thenReturn(userDTO);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void createUser_futureBirthDate_notOk() throws Exception {
        CreateUserDTO createUserDTO = buildCreateUserDTO();
        createUserDTO.setBirthDate(LocalDate.now().plusYears(1));

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUser_returnUpdated_ok() throws Exception {
        UpdateUserDTO updateUserDTO = buildUpdateUserDTO();
        userDTO.setEmail(updateUserDTO.getEmail());

        when(userService.update(eq(JOHN_ID), any(UpdateUserDTO.class))).thenReturn(userDTO);

        mvc.perform(put("/api/users/{id}", JOHN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(JOHN_ID))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(userDTO.getPhoneNumber()))
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(userDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$.address.city").value(userDTO.getAddress().getCity()))
                .andExpect(jsonPath("$.address.country").value(userDTO.getAddress().getCountry()))
                .andExpect(jsonPath("$.address.street").value(userDTO.getAddress().getStreet()));

        verify(userService, times(1)).update(eq(JOHN_ID), any(UpdateUserDTO.class));
    }

    @Test
    public void particularUpdate_returnUpdated_ok() throws Exception {
        PartialUpdateUserDTO updateUserDTO = buildPartialUpdateUserDTO();
        userDTO.setEmail(NEW_MAIL);
        userDTO.setLastName(NEW_LAST_NAME);

        when(userService.particularUpdate(eq(JOHN_ID), any(PartialUpdateUserDTO.class))).thenReturn(userDTO);

        mvc.perform(patch("/api/users/{id}", JOHN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(JOHN_ID))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(userDTO.getPhoneNumber()))
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(userDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$.address.city").value(userDTO.getAddress().getCity()))
                .andExpect(jsonPath("$.address.country").value(userDTO.getAddress().getCountry()))
                .andExpect(jsonPath("$.address.street").value(userDTO.getAddress().getStreet()));

        verify(userService, times(1)).particularUpdate(eq(JOHN_ID), any(PartialUpdateUserDTO.class));
    }

    @Test
    public void getById_returnUser_ok() throws Exception {
        when(userService.getById(eq(JOHN_ID))).thenReturn(userDTO);

        mvc.perform(get("/api/users/{id}", JOHN_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).getById(eq(JOHN_ID));
    }

    @Test
    public void search_returnUsers_ok() throws Exception {
        SearchUserDTO searchUserDTO = new SearchUserDTO();
        searchUserDTO.setBirthDateRange(buildDateRange(NEW_BIRTH_DATE, JOHN_BIRTH_DATE));

        List<UserDTO> userDTOList = List.of(new UserDTO(), new UserDTO());

        when(userService.search(any(SearchUserDTO.class))).thenReturn(userDTOList);

        mvc.perform(get("/api/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(userDTOList.size()));
    }

    @Test
    public void search_invalidFromDate_notOk() throws Exception {
        SearchUserDTO searchUserDTO = new SearchUserDTO();
        searchUserDTO.setBirthDateRange(buildDateRange(null, JOHN_BIRTH_DATE));

        mvc.perform(get("/api/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void search_invalidToDate_notOk() throws Exception {
        SearchUserDTO searchUserDTO = new SearchUserDTO();
        searchUserDTO.setBirthDateRange(buildDateRange(NEW_BIRTH_DATE, null));

        mvc.perform(get("/api/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void search_nullRange_notOk() throws Exception {
        SearchUserDTO searchUserDTO = new SearchUserDTO();
        searchUserDTO.setBirthDateRange(null);

        mvc.perform(get("/api/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteById_deleted_ok() throws Exception {
        mvc.perform(delete("/api/users/{id}", JOHN_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void initUserDTO() {
        userDTO = new UserDTO();
        userDTO.setId(JOHN_ID);
        userDTO.setEmail(JOHN_MAIL);
        userDTO.setFirstName(JOHN_FIRST_NAME);
        userDTO.setLastName(JOHN_LAST_NAME);
        userDTO.setPhoneNumber(JOHN_PHONE);
        userDTO.setBirthDate(JOHN_BIRTH_DATE);
        userDTO.setAddress(getAddressDTO());
    }

    private CreateUserDTO buildCreateUserDTO() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail(JOHN_MAIL);
        createUserDTO.setFirstName(JOHN_FIRST_NAME);
        createUserDTO.setLastName(JOHN_LAST_NAME);
        createUserDTO.setPhoneNumber(JOHN_PHONE);
        createUserDTO.setBirthDate(JOHN_BIRTH_DATE);
        createUserDTO.setAddress(getCreateAddressDTO());

        return createUserDTO;
    }

    private UpdateUserDTO buildUpdateUserDTO() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setEmail(NEW_MAIL);
        updateUserDTO.setFirstName(NEW_FIRST_NAME);
        updateUserDTO.setLastName(NEW_LAST_NAME);
        updateUserDTO.setPhoneNumber(NEW_PHONE);
        updateUserDTO.setBirthDate(NEW_BIRTH_DATE);
        updateUserDTO.setAddress(getUpdateAddressDTO());
        return updateUserDTO;
    }

    private PartialUpdateUserDTO buildPartialUpdateUserDTO() {
        PartialUpdateUserDTO partialUpdateUserDTO = new PartialUpdateUserDTO();
        partialUpdateUserDTO.setEmail(NEW_MAIL);
        partialUpdateUserDTO.setLastName(NEW_LAST_NAME);

        UpdateAddressDTO updateAddressDTO = getUpdateAddressDTO();
        updateAddressDTO.setCountry(null);
        updateAddressDTO.setCity(null);

        partialUpdateUserDTO.setAddress(updateAddressDTO);

        return partialUpdateUserDTO;
    }

    private AddressDTO getAddressDTO() {
        AddressDTO dto = new AddressDTO();
        dto.setCity(ADDRESS_CITY);
        dto.setStreet(ADDRESS_STREET);
        dto.setCountry(ADDRESS_COUNTRY);
        return dto;
    }

    private CreateAddressDTO getCreateAddressDTO() {
        CreateAddressDTO createDTO = new CreateAddressDTO();
        createDTO.setCity(ADDRESS_CITY);
        createDTO.setStreet(ADDRESS_STREET);
        createDTO.setCountry(ADDRESS_COUNTRY);
        return createDTO;
    }

    private UpdateAddressDTO getUpdateAddressDTO() {
        UpdateAddressDTO updateDTO = new UpdateAddressDTO();
        updateDTO.setCity(NEW_ADDRESS_CITY);
        updateDTO.setStreet(NEW_ADDRESS_STREET);
        updateDTO.setCountry(NEW_ADDRESS_COUNTRY);
        return updateDTO;
    }

    private DateRange buildDateRange(LocalDate from, LocalDate to) {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(from);
        dateRange.setEndDate(to);
        return dateRange;
    }

}
