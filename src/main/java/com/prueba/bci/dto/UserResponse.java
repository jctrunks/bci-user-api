package com.prueba.bci.dto;

import jakarta.validation.constraints.Digits;

import java.time.LocalDateTime;
import java.util.List;

public class UserResponse {
    private String id;
    private String name;
    private String email;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime last_login;
    private String token;
    private boolean isactive;
    private List<PhoneResponse> phones;

    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public LocalDateTime getLast_login() {
        return last_login;
    }

    public void setLast_login(LocalDateTime last_login) {
        this.last_login = last_login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public List<PhoneResponse> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneResponse> phones) {
        this.phones = phones;
    }

    public static class PhoneResponse {
        @Digits(integer = 8, fraction = 0)
        private int number;
        @Digits(integer = 3, fraction = 0)
        private int citycode;
        @Digits(integer = 3, fraction = 0)
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
