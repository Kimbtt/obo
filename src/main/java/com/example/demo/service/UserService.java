package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.model.request.CreateUserReq;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    User createUser(CreateUserReq req) throws Exception;

    List<User> getListUser();

    Optional<User> getUserById(long id);

    boolean deleteUser(Long id);

    User getUserByEmail(String email);

    User getCurrentUser();

    User updateUser(User userUpdate);
}
