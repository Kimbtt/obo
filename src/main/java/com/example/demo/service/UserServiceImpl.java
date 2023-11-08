package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.request.CreateUserReq;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(CreateUserReq req) throws Exception{
        try {
            User rs = userRepository.findByEmail(req.getEmail());
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }

        // Convert CreateUserReq -> User
        User user = new User();
        // user.setId(users.size()+1);
        user.setPhone(req.getPhone());
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        List<String> role = new ArrayList<>();
        role.add("USER");
        user.setRole(role);
        // Mã hóa mật khẩu sử dụng BCrypt
        user.setPassword(BCrypt.hashpw(req.getPassword(), BCrypt.gensalt(12)));

        // Thêm user
        // users.add(user);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> getListUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean deleteUser(Long id) {
        try {
            // Lấy tên người dùng nếu có
            Optional<User> rs = userRepository.findById(id);

            // Kiểm tra xem có tồn tại hay không
            if (rs.isPresent()){
                // Lấy giá trị của Optional
                User user =rs.get();
                // Xóa
                userRepository.delete(user);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getCurrentUser() {
        User currentUser = ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        System.out.println(currentUser);
        return currentUser;
    }

    @Override
    public User updateUser(User userUpdate) {
        if (userUpdate != null) {
            return userRepository.save(userUpdate);
        }
        throw new NotFoundException("Thông tin không hợp lệ!");
    }
}