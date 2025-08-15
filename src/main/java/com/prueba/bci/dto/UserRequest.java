package com.prueba.bci.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class UserRequest {
    @NotBlank
    @Size(min = 2, max = 50, message = "Nombre debe contener al menos 2 caracteres y maximo 50")
    private String name;

    @NotBlank
    @Size(min = 5, max = 50, message = "Email debe contener al menos 5 caracteres  y maximo 50")
    private String email;

    @NotBlank
    @Size(min = 8, max = 50, message = "Password debe contener al menos 8 caracteres y maximo 50")
    private String password;

    @NotNull
    private List<PhoneRequest> phones;

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PhoneRequest> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneRequest> phones) {
        this.phones = phones;
    }

    public static class PhoneRequest {

        @Digits(integer = 8, fraction = 0, message = "numero debe contener 8 digitos")
        private int number;

        @Min(value = 1)
        @Max(value = 999)
        private int citycode;

        @Min(value = 1)
        @Max(value = 999)
        private int countrycode;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getCitycode() {
            return citycode;
        }

        public void setCitycode(int citycode) {
            this.citycode = citycode;
        }

        public int getCountrycode() {
            return countrycode;
        }

        public void setCountrycode(int countrycode) {
            this.countrycode = countrycode;
        }
    }
}
