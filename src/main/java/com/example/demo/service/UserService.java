package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.model.request.CreateUserReq;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    public User createUser(CreateUserReq req) throws Exception;
    public List<User> getListUser();

    public Optional<User> getUserById(long id);

    public boolean deleteUser(Long id);

     User getUserByEmail(String email);
}
