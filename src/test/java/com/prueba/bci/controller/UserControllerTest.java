package com.prueba.bci.controller;

import com.prueba.bci.dto.UserResponse;
import com.prueba.bci.dto.UserResponseNew;
import com.prueba.bci.exception.EmailAlreadyExistsException;
import com.prueba.bci.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// ... existing code ...
import com.prueba.bci.exception.GlobalExceptionHandler;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @BeforeEach
    void resetMock() {
        Mockito.reset(userService);
    }

    @Test
    @DisplayName("POST /api/users/register - should create and return user")
    void register_shouldCreateUser() throws Exception {
        UserResponse stubResponse = new UserResponse();
        stubResponse.setId("abc-123");
        stubResponse.setCreated(LocalDateTime.now());
        stubResponse.setModified(LocalDateTime.now());
        stubResponse.setLast_login(LocalDateTime.now());
        stubResponse.setToken("stub-jwt-token");
        stubResponse.setIsactive(true);

        Mockito.when(userService.register(any()))
                .thenReturn(stubResponse);

        String requestJson =
                "{" +
                        "\"name\": \"Juan Rodriguez\"," +
                        "\"email\": \"juan@rodriguez.org\"," +
                        "\"password\": \"Hunter22\"," +
                        "\"phones\": [{\"number\":12345678,\"citycode\":1,\"countrycode\":57}]" +
                        "}";

        // Act + Assert
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("abc-123")))
                .andExpect(jsonPath("$.token", is("stub-jwt-token")))
                .andExpect(jsonPath("$.isactive", is(true)))
                ;
    }

    @Test
    @DisplayName("GET /api/users/getAll - Debe retornar una lista de usuarios")
    void getAllUsers_shouldReturnList() throws Exception {

        UserResponseNew.PhoneResponse phone = new UserResponseNew.PhoneResponse();
        phone.setNumber(76543213);
        phone.setCitycode(2);
        phone.setCountrycode(56);

        UserResponseNew u1 = new UserResponseNew();
        u1.setId("id-1");
        u1.setName("Alice");
        u1.setEmail("alice@example.com");
        u1.setCreated(LocalDateTime.now());
        u1.setModified(LocalDateTime.now());
        u1.setLast_login(LocalDateTime.now());
        u1.setToken("token-1");
        u1.setIsactive(true);
        u1.setPhones(List.of(phone));

        UserResponseNew u2 = new UserResponseNew();
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


    @Test
    @DisplayName("POST /api/users/register - 400 Cuando el nombre está vacio")
    void register_shouldReturn400_whenNameBlank() throws Exception {
        String payload = "{" +
                "\"name\": \" \"," +
                "\"email\": \"juan@rodriguez.org\"," +
                "\"password\": \"Hunter22\"," +
                "\"phones\": [{\"number\":12345678,\"citycode\":1,\"countrycode\":57}]" +
                "}";
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Nombre")));
    }

    @Test
    @DisplayName("POST /api/users/register - 400 Cuando falta campo numero de teléfono")
    void register_shouldReturn400_whenPhonesMissing() throws Exception {
        String payload = "{" +
                "\"name\": \"Juan Rodriguez\"," +
                "\"email\": \"juan@rodriguez.org\"," +
                "\"password\": \"Hunter22\"" +
                "}";
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("teléfonos")));
    }

    @Test
    @DisplayName("POST /api/users/register - 400 cuando el campo teléfono está vacío")
    void register_shouldReturn400_whenPhonesEmpty() throws Exception {
        String payload = "{" +
                "\"name\": \"Juan Rodriguez\"," +
                "\"email\": \"juan@rodriguez.org\"," +
                "\"password\": \"Hunter22\"," +
                "\"phones\": []" +
                "}";
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("al menos un teléfono")));
    }

    @Test
    @DisplayName("POST /api/users/register - 400 cuando el numero de teléfono es nulo")
    void register_shouldReturn400_whenPhoneNumberNull() throws Exception {
        String payload = "{" +
                "\"name\": \"Juan Rodriguez\"," +
                "\"email\": \"juan@rodriguez.org\"," +
                "\"password\": \"Hunter22\"," +
                "\"phones\": [{\"number\":null,\"citycode\":1,\"countrycode\":57}]" +
                "}";
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", anyOf(containsString("número es obligatorio"),
                                                       containsString("número debe ser de 8 digitos"))));
    }

    @Test
    @DisplayName("POST /api/users/register - 400 cuando citycode está fuera de rango")
    void register_shouldReturn400_whenCitycodeOutOfRange() throws Exception {
        String payload = "{" +
                "\"name\": \"Juan Rodriguez\"," +
                "\"email\": \"juan@rodriguez.org\"," +
                "\"password\": \"Hunter22\"," +
                "\"phones\": [{\"number\":12345678,\"citycode\":0,\"countrycode\":57}]" +
                "}";
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("citycode")));
    }

    @Test
    @DisplayName("POST /api/users/register - 400 JSON mal formado / typos erroneos")
    void register_shouldReturn400_onMalformedJson() throws Exception {
        String payload = "{\"name\":\"Juan\",\"email\":\"juan@rodriguez.org\",\"password\":\"Hunter22\",\"phones\":[{\"number\":\"abc\",\"citycode\":1,\"countrycode\":57}]";
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", anyOf(containsString("Cuerpo de la solicitud inválido"),
                                                       containsString("tipos de datos incorrectos"),
                                                       notNullValue())));
    }

    @Test
    @DisplayName("POST /api/users/register - 400 cuando regex de email es invalido")
    void register_shouldReturn400_whenEmailRegexInvalid() throws Exception {
        Mockito.when(userService.register(ArgumentMatchers.any()))
                .thenThrow(new IllegalArgumentException("Correo con formato inválido"));

        String payload = "{" +
                "\"name\": \"Juan Rodriguez\"," +
                "\"email\": \"juan@rodriguez\"," +
                "\"password\": \"Hunter22\"," +
                "\"phones\": [{\"number\":12345678,\"citycode\":1,\"countrycode\":57}]" +
                "}";

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Correo con formato inválido")));
    }

    @Test
    @DisplayName("POST /api/users/register - 400 rcuando regex de password es invalido")
    void register_shouldReturn400_whenPasswordRegexInvalid() throws Exception {
        Mockito.when(userService.register(ArgumentMatchers.any()))
                .thenThrow(new IllegalArgumentException("Contraseña con formato inválido"));

        String payload = "{" +
                "\"name\": \"Juan Rodriguez\"," +
                "\"email\": \"juan@rodriguez.org\"," +
                "\"password\": \"shortttt\"," +
                "\"phones\": [{\"number\":12345678,\"citycode\":1,\"countrycode\":57}]" +
                "}";

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Contraseña con formato inválido")));
    }

    @Test
    @DisplayName("POST /api/users/register - 400 cuando el correo ya existe")
    void register_shouldReturn400_whenEmailAlreadyExists() throws Exception {
        Mockito.when(userService.register(ArgumentMatchers.any()))
                .thenThrow(new EmailAlreadyExistsException("El correo ya está registrado"));

        String payload = "{" +
                "\"name\": \"Juan Rodriguez\"," +
                "\"email\": \"juan@rodriguez.org\"," +
                "\"password\": \"Hunter22\"," +
                "\"phones\": [{\"number\":12345678,\"citycode\":1,\"countrycode\":57}]" +
                "}";

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("El correo ya está registrado")));
    }
}