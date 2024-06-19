package org.myblog.domain.user.repository;

import org.junit.jupiter.api.Test;
import org.myblog.domain.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void testIsUsernameDuplicate() {
        User user = new User("minjiki2");
        User user2 = new User("mj");
        User user3 = new User("minjiki2");

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        boolean isitExist = userRepository.existsByUsername("minjiki2");
        boolean isitExist2 = userRepository.existsByUsername("hello");

        System.out.println("isitExist : " + isitExist); // -- true
        System.out.println("isitExist2 : " + isitExist2); // -- false

    }

    @Test
    void testIsEmailDuplicate() {
        User user = new User("minjiki2", "email1");
        User user2 = new User("mj", "email2");
        User user3 = new User("minjiki2", "email2");

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        boolean isitExist = userRepository.existsByEmail("email1");
        boolean isitExist2 = userRepository.existsByEmail("noooooooo");

        System.out.println("isitExist : " + isitExist); // -- true
        System.out.println("isitExist2 : " + isitExist2); // -- false

    }

    @Test
    void testfindByUsername() {
        User user = new User("user1");

        userRepository.save(user);

        User foundUser = userRepository.findByUsername("user1");
        System.out.println("test >>>>>> user : " + foundUser.getUsername());
    }
}