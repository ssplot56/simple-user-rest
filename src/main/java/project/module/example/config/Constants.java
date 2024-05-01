package project.module.example.config;

import java.time.LocalDate;

public class Constants {

    public static class Exception {
        public static final String NOT_CORRECT_EMAIL_FORMAT = "Not correct email format. Try again.";
        public static final String USER_NOT_FOUND = "The user with the identifier '%s' does not exist.";
        public static final String EMAIL_ALREADY_EXIST = "This email already exists. Try another one.";
        public static final String PHONE_NUMBER_ALREADY_EXIST = "This phone number already exist. Try another one.";
        public static final String NOT_VALID_AGE = "User should be older than - '%s'.";
    }

    public static class Test {
        public static final Long JOHN_ID = 1L;
        public static final String JOHN_MAIL = "john@mail.com";
        public static final String JOHN_FIRST_NAME = "John";
        public static final String JOHN_LAST_NAME = "Cena";
        public static final String JOHN_PHONE = "12345678";
        public static final LocalDate JOHN_BIRTH_DATE = LocalDate.of(1990, 1, 1);
        public static final String ADDRESS_CITY = "Kyiv";
        public static final String ADDRESS_STREET = "Street";
        public static final String ADDRESS_COUNTRY = "Ukraine";

        public static final String NEW_MAIL = "notJohn@mail.com";
        public static final String NEW_FIRST_NAME = "notJohn";
        public static final String NEW_LAST_NAME = "notCena";
        public static final String NEW_PHONE = "87654321";
        public static final LocalDate NEW_BIRTH_DATE = LocalDate.of(1980, 1, 1);
        public static final String NEW_ADDRESS_CITY = "Krakow";
        public static final String NEW_ADDRESS_STREET = "WierdNamedStreet";
        public static final String NEW_ADDRESS_COUNTRY = "Poland";
    }

}
