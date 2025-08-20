package com.prueba.bci.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import jakarta.validation.Valid;

public class UserRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "Nombre debe contener al menos 2 caracteres y maximo 50")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Size(min = 5, max = 50, message = "Email debe contener al menos 5 caracteres  y maximo 50")
    private String email;

    @NotBlank(message = "El password es obligatorio")
    @Size(min = 8, max = 50, message = "Password debe contener al menos 8 caracteres y maximo 50, al menos una mayuscula, una minuscula y un número")
    private String password;

    @NotNull(message = "La lista de teléfonos es obligatoria")
    @Size(min = 1, message = "Debe incluir al menos un teléfono")
    @Valid
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

        @NotNull(message = "número es obligatorio")
        @Digits(integer = 8, fraction = 0, message = "número debe ser de 8 digitos")
        private Integer number;

        @NotNull(message = "citycode es obligatorio")
        @Min(value = 1, message = "citycode debe ser mayor o igual a 1")
        @Max(value = 999, message = "citycode debe ser menor o igual a 999")
        private Integer citycode;

        @NotNull(message = "countrycode es obligatorio")
        @Min(value = 1, message = "countrycode debe ser mayor o igual a 1")
        @Max(value = 999, message = "countrycode debe ser menor o igual a 999")
        private Integer countrycode;

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Integer getCitycode() {
            return citycode;
        }

        public void setCitycode(Integer citycode) {
            this.citycode = citycode;
        }

        public Integer getCountrycode() {
            return countrycode;
        }

        public void setCountrycode(Integer countrycode) {
            this.countrycode = countrycode;
        }
    }
}
