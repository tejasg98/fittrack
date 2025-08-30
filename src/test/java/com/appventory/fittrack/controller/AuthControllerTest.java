package com.appventory.fittrack.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.appventory.fittrack.dto.UserRequest;
import com.appventory.fittrack.dto.UserResponse;
import com.appventory.fittrack.model.User;
import com.appventory.fittrack.repository.UserRepository;
import com.appventory.fittrack.security.JwtUtil;
import com.appventory.fittrack.service.UserService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)  

class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserService userService;

    @Test
    void testRegisterUser() throws Exception {
        UserResponse response = UserResponse.builder().id(1l).email("test@example.com").build();

        when(userService.createUser(any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/v1/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "fullName": "Test User",
                      "email": "test@example.com",
                      "password": "password123",
                      "dateOfBirth": "1995-05-20",
                      "weight": 70,
                      "height": 175,
                      "roles": ["USER"]
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(Mockito.anyString(), Mockito.any()))
        .thenReturn("mockJwt");

        mockMvc.perform(post("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"))
                .andExpect(cookie().exists("JWT_TOKEN"));
    }

    @Test
    void testLoginFailure_InvalidPassword() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        mockMvc.perform(post("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginFailure_UserNotFound() throws Exception {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"notfound@example.com\", \"password\": \"password\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/v1/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(cookie().value("JWT_TOKEN", (String) null));
    }
}
