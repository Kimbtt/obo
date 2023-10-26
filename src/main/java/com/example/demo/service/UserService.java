package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.request.CreateUserReq;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User createUser(CreateUserReq req) throws Exception;
    public List<User> getListUser();

    public User getUserById(int id);

    public boolean deleteUser(Long id);
}
