package hexlet.code.controller;

import hexlet.code.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = hexlet.code.app.AppApplication.class)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
    }

    @BeforeEach
    void setUp() {
        System.out.println("Starting setup...");
        ResponseEntity<User[]> response = restTemplate.getForEntity("/api/users", User[].class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            System.out.println("Found " + response.getBody().length + " users, clearing...");
            for (User user : response.getBody()) {
                restTemplate.delete("/api/users/{id}", user.getId());
            }
        }
    }

    @Test
    void testCreateUser() {
        System.out.println("Testing createUser...");
        User user = new User();
        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";
        user.setEmail(uniqueEmail);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("pass123");

        ResponseEntity<User> response = restTemplate.postForEntity("/api/users", user, User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(uniqueEmail);
        assertThat(response.getBody().getPassword()).isNull();
        assertThat(response.getBody().getCreatedAt()).isNotNull();
    }

    @Test
    void testGetUser() {
        System.out.println("Testing getUser...");
        User user = createTestUser();
        assertThat(user).isNotNull();

        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}", User.class, user.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getBody().getPassword()).isNull();
        assertThat(response.getBody().getCreatedAt()).isNotNull();
    }

    @Test
    void testUpdateUser() {
        System.out.println("Testing updateUser...");
        User user = createTestUser();
        assertThat(user).isNotNull();

        User update = new User();
        String newEmail = "updated" + System.currentTimeMillis() + "@example.com";
        update.setEmail(newEmail);
        update.setPassword("newpass");

        restTemplate.put("/api/users/{id}", update, user.getId());
        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}", User.class, user.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo(newEmail);
        assertThat(response.getBody().getPassword()).isNull();
        assertThat(response.getBody().getUpdatedAt()).isNotNull();
    }

    @Test
    void testDeleteUser() {
        System.out.println("Testing deleteUser...");
        User user = createTestUser();
        assertThat(user).isNotNull();

        restTemplate.delete("/api/users/{id}", user.getId());
        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}", User.class, user.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private User createTestUser() {
        System.out.println("Creating test user...");
        User user = new User();
        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";
        user.setEmail(uniqueEmail);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("pass123");
        ResponseEntity<User> response = restTemplate.postForEntity("/api/users", user, User.class);
        return response.getStatusCode() == HttpStatus.CREATED ? response.getBody() : null;
    }
}
