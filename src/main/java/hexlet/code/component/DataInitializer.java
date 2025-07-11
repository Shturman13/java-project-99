package hexlet.code.component;

import hexlet.code.model.User;
import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userService.findAll().isEmpty()) {
            User admin = new User();
            admin.setEmail("hexlet@example.com");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setPassword("qwerty");
            admin.setCreatedAt(new Date());
            admin.setUpdatedAt(new Date());
            System.out.println("Initializing admin user: " + admin.getEmail() + ", password: " + admin.getPassword());
            userService.createUser(admin);
        }
    }
}
