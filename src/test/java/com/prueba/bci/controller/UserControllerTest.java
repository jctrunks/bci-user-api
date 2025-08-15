package com.prueba.bci.controller;

import com.prueba.bci.dto.UserResponse;
import com.prueba.bci.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        UserService userService() {
            // Provide a Mockito mock as a bean instead of using @MockBean (deprecated in Boot 3.4+)
            return Mockito.mock(UserService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // This will be the Mockito mock defined in MockConfig
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("POST /api/users/register - should create and return user")
    void register_shouldCreateUser() throws Exception {
        // Arrange
        UserResponse.PhoneResponse phone = new UserResponse.PhoneResponse();
        phone.setNumber(1234567);
        phone.setCitycode(1);
        phone.setCountrycode(57);

        UserResponse stubResponse = new UserResponse();
        stubResponse.setId("abc-123");
        stubResponse.setName("Juan Rodriguez");
        stubResponse.setEmail("juan@rodriguez.org");
        stubResponse.setCreated(LocalDateTime.now());
        stubResponse.setModified(LocalDateTime.now());
        stubResponse.setLast_login(LocalDateTime.now());
        stubResponse.setToken("stub-jwt-token");
        stubResponse.setIsactive(true);
        stubResponse.setPhones(List.of(phone));

        Mockito.when(userService.register(any()))
                .thenReturn(stubResponse);

        String requestJson =
                "{" +
                        "\"name\": \"Juan Rodriguez\"," +
                        "\"email\": \"juan@rodriguez.org\"," +
                        "\"password\": \"Hunter22\"," +
                        "\"phones\": [{\"number\":1234567,\"citycode\":1,\"countrycode\":57}]" +
                        "}";

        // Act + Assert
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("abc-123")))
                .andExpect(jsonPath("$.name", is("Juan Rodriguez")))
                .andExpect(jsonPath("$.email", is("juan@rodriguez.org")))
                .andExpect(jsonPath("$.token", is("stub-jwt-token")))
                .andExpect(jsonPath("$.isactive", is(true)))
                .andExpect(jsonPath("$.phones", hasSize(1)))
                .andExpect(jsonPath("$.phones[0].number", is("1234567")))
                .andExpect(jsonPath("$.phones[0].citycode", is("1")))
                .andExpect(jsonPath("$.phones[0].countrycode", is("57")));
    }

    @Test
    @DisplayName("GET /api/users/getAll - should return list of users")
    void getAllUsers_shouldReturnList() throws Exception {
        // Arrange
        UserResponse.PhoneResponse phone = new UserResponse.PhoneResponse();
        phone.setNumber(7654321);
        phone.setCitycode(2);
        phone.setCountrycode(56);

        UserResponse u1 = new UserResponse();
        u1.setId("id-1");
        u1.setName("Alice");
        u1.setEmail("alice@example.com");
        u1.setCreated(LocalDateTime.now());
        u1.setModified(LocalDateTime.now());
        u1.setLast_login(LocalDateTime.now());
        u1.setToken("token-1");
        u1.setIsactive(true);
        u1.setPhones(List.of(phone));

        UserResponse u2 = new UserResponse();
        u2.setId("id-2");
        u2.setName("Bob");
        u2.setEmail("bob@example.com");
        u2.setCreated(LocalDateTime.now());
        u2.setModified(LocalDateTime.now());
        u2.setLast_login(LocalDateTime.now());
        u2.setToken("token-2");
        u2.setIsactive(false);
        u2.setPhones(List.of());

        Mockito.when(userService.getAllUsers())
                .thenReturn(List.of(u1, u2));

        // Act + Assert
        mockMvc.perform(get("/api/users/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("id-1")))
                .andExpect(jsonPath("$[0].name", is("Alice")))
                .andExpect(jsonPath("$[0].email", is("alice@example.com")))
                .andExpect(jsonPath("$[0].isactive", is(true)))
                .andExpect(jsonPath("$[0].phones", hasSize(1)))
                .andExpect(jsonPath("$[1].id", is("id-2")))
                .andExpect(jsonPath("$[1].name", is("Bob")))
                .andExpect(jsonPath("$[1].email", is("bob@example.com")))
                .andExpect(jsonPath("$[1].isactive", is(false)))
                .andExpect(jsonPath("$[1].phones", hasSize(0)));
    }
}