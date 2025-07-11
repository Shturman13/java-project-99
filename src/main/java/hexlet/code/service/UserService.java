package hexlet.code.service;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        System.out.println("UserService initialized, userRepository: " + (userRepository != null));
        System.out.println("UserService initialized, passwordEncoder: " + (passwordEncoder != null));
    }

    public User createUser(User user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(new Date());
        }
        if (user.getUpdatedAt() == null) {
            user.setUpdatedAt(new Date());
        }
        // Проверка и обработка password
        String rawPassword = user.getPassword();
        System.out.println("Received password in UserService before encoding: " + rawPassword);
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty at this stage. Current value: "
                    + rawPassword);
        }
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        System.out.println("Encoded password before save: " + encodedPassword);
        System.out.println("User object before save: " + user);
        User savedUser = userRepository.save(user);
        // Временно закомментировано для диагностики
        // savedUser.setPassword(null); // Очищаем пароль перед возвратом
        System.out.println("Saved user password after save: " + savedUser.getPassword());
        return savedUser;
    }

    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            if (userDetails.getFirstName() != null) {
                user.setFirstName(userDetails.getFirstName());
            }
            if (userDetails.getLastName() != null) {
                user.setLastName(userDetails.getLastName());
            }
            if (userDetails.getEmail() != null) {
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
            user.setUpdatedAt(new Date());
            return userRepository.save(user);
        });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
