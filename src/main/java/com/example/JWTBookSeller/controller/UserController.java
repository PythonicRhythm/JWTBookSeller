package com.example.JWTBookSeller.controller;

import com.example.JWTBookSeller.entity.Book;
import com.example.JWTBookSeller.service.BookService;
import com.example.JWTBookSeller.service.UserInfoService;
import com.example.JWTBookSeller.entity.AuthRequest;
import com.example.JWTBookSeller.entity.UserInfo;
import com.example.JWTBookSeller.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private BookService bookService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/books")
    public String bookPage(Model model) {
        model.addAttribute("bookList", this.bookService.getAllBooks());
        return "books";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        service.addUser(userInfo);
        return "login";
    }

    @PostMapping("/deleteuser/{id}")
    public String deleteUser(@RequestBody long id) {
        service.deleteUserByID(id);
        return "login";
    }

    @PostMapping("/addNewBook")
    public String addNewBook(@RequestBody Book book, Model model) {
        bookService.addNewBook(book);
        model.addAttribute("bookList", this.bookService.getAllBooks());
        return "books";
    }

    @PostMapping("/deletebook/{isbn}")
    public String deleteBook(@RequestBody long isbn, Model model) {
        bookService.deleteBookByID(isbn);
        model.addAttribute("bookList", this.bookService.getAllBooks());
        return "books";
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "userProfile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "adminProfile";
    }

    @PostMapping(value = "/generateToken", consumes = "application/json")
    public String authenticateAndGetTokenJSON(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping(value = "/generateToken", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public String authenticateAndGetTokenXML(@RequestBody AuthRequest authRequest, Model model) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.getUsername());
            UserDetails user = service.loadUserByUsername(token);
            if(user != null)
            {
                List<String> authorities = new ArrayList<>();
                user.getAuthorities()
                        .stream().anyMatch(a -> authorities.add(a.getAuthority().toString()));
                model.addAttribute("authorities", authorities);
                return jwtService.generateToken(authRequest.getUsername());
            }
            return null;
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

}
