package hexlet.code.controller;

import hexlet.code.model.User;
import hexlet.code.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = hexlet.code.app.AppApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        System.out.println("Starting setup...");
        String uniqueEmail = "test_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        testUser = new User();
        testUser.setEmail(uniqueEmail);
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setPassword("password"); // Пароль будет захеширован в сервисе
        testUser = userService.createUser(testUser); // Создаем пользователя перед тестами
        System.out.println("Test user created with ID: " + testUser.getId() + ", email: " + uniqueEmail);
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    public void testCreateUser() throws Exception {
        System.out.println("Testing createUser...");
        Map<String, String> userData = new HashMap<>();
        String uniqueNewEmail = "newtest_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        userData.put("email", uniqueNewEmail);
        userData.put("firstName", "NewTest");
        userData.put("lastName", "User");
        userData.put("password", "newpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userData)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    public void testGetUser() throws Exception {
        System.out.println("Testing getUser...");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(testUser.getEmail()));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    public void testUpdateUser() throws Exception {
        System.out.println("Testing updateUser...");
        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", "UpdatedTest");
        userData.put("lastName", "User");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userData)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("UpdatedTest"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    public void testDeleteUser() throws Exception {
        System.out.println("Testing deleteUser...");
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
