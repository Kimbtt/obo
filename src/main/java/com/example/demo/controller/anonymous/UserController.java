package com.example.demo.controller.anonymous;


import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.mapper.UserMapper;
import com.example.demo.model.request.AuthenticateReq;
import com.example.demo.model.request.CreateUserReq;
import com.example.demo.model.request.LoginReq;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import static com.example.demo.config.Constant.MAX_AGE_COOKIE;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private CustomUserDetails userDetails = new CustomUserDetails();

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserReq req, HttpServletResponse response) throws Exception{
        try {
            // Create user
            User result = userService.createUser(req);
            // Gen token
            UserDetails principal = new CustomUserDetails(result);
            String token = jwtTokenUtil.generateToken(principal);

            // Add token to cookie to login
            Cookie cookie = new Cookie("JWT_TOKEN",token);
            cookie.setMaxAge(MAX_AGE_COOKIE);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok(UserMapper.toUserDto(result));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Loi cmnr");
        }
    }

    /**
     * Đăng nhập
     * @param LoginReq req
     * @param HttpServletResponse response
     * @return
     */
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginReq req, HttpServletResponse response) {
        // Authenticate
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(),
                            req.getPassword()
                    )
            );

            // Gen token
            String token = jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal());

            // Add token to cookie to login
            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setMaxAge(MAX_AGE_COOKIE); // set ngày hết hạn(tính theo s)
            cookie.setPath("/"); // cookei được gửi đến những link
            response.addCookie(cookie);

            return ResponseEntity.ok(UserMapper.toUserDto(((CustomUserDetails) authentication.getPrincipal()).getUser()));
        } catch (Exception ex) {
            return new ResponseEntity<>("failed",HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * lấy danh sách user
     * @return
     */
    @GetMapping("api/list")
    public ResponseEntity<?> getAccount() {
        List<User> users = userService.getListUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("api/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Looix");
        }

    }


    /**
     * Xóa 1 user
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("api/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id ) throws Exception{
        try{
            // Xóa user theo id
            userService.deleteUser(id);
            return ResponseEntity.ok(String.format("Đã xóa thành công user có id: %d", id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa user có id: " + id);
        }
    }



}
