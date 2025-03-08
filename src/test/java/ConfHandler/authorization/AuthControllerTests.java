package ConfHandler.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorizationService authorizationService;

    private ObjectMapper objectMapper;
    private LoginCommand mockLoginCommand;
    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockLoginCommand = new LoginCommand("user@mail.com", "password");
    }

    @Test
    public void testLoginSuccess() throws Exception {
        ParticipantInfo participantInfo = new ParticipantInfo(UUID.randomUUID(), "Joe", "Doe",
                "user@mail.com", "", List.of(), "", List.of());

        when(authorizationService.login(mockLoginCommand)).thenReturn(participantInfo);

        String response = mockMvc.perform(post("/authenticate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mockLoginCommand)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();


        assertTrue(response.contains("\"name\":\"Joe\""));
        assertTrue(response.contains("\"surname\":\"Doe\""));
        assertTrue(response.contains("\"email\":\"user@mail.com\""));
        verify(authorizationService, times(1)).login(mockLoginCommand);
    }

    @Test
    public void testLoginFailure() throws Exception {
        when(authorizationService.login(mockLoginCommand)).thenThrow(new ParticipantNotFoundException("Invalid credentials"));

        mockMvc.perform(post("/authenticate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mockLoginCommand)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials not found"));

        verify(authorizationService, times(1)).login(mockLoginCommand);
    }
}
