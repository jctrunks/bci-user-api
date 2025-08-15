package com.prueba.bci.service;

import com.prueba.bci.dto.UserRequest;
import com.prueba.bci.dto.UserResponse;
import com.prueba.bci.entity.Phone;
import com.prueba.bci.entity.User;
import com.prueba.bci.exception.EmailAlreadyExistsException;
import com.prueba.bci.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${app.regex.email}")
    private String emailRegex;

    @Value("${app.regex.password}")
    private String passwordRegex;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public UserResponse register(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("El correo ya registrado");
        }
        if (!request.getEmail().matches(emailRegex)) {
            throw new IllegalArgumentException("Correo con formato inválido");
        }
        if (!request.getPassword().matches(passwordRegex)) {
            throw new IllegalArgumentException("Contraseña con formato inválido");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setModified(now);
        user.setLastLogin(now);
        user.setIsactive(true);

        List<Phone> phones = request.getPhones().stream().map(p -> {
            Phone ph = new Phone();
            ph.setNumber(p.getNumber());
            ph.setCitycode(p.getCitycode());
            ph.setCountrycode(p.getCountrycode());
            ph.setUser(user);
            return ph;
        }).collect(Collectors.toList());
        user.setPhones(phones);

        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("uid", user.getId())
                .setIssuedAt(java.sql.Timestamp.valueOf(now))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        user.setToken(token);

        userRepository.save(user);
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        UserResponse resp = new UserResponse();
        resp.setId(user.getId());
        resp.setName(user.getName());
        resp.setEmail(user.getEmail());
        resp.setCreated(user.getCreated());
        resp.setModified(user.getModified());
        resp.setLast_login(user.getLastLogin());
        resp.setToken(user.getToken());
        resp.setIsactive(user.isIsactive());
        List<UserResponse.PhoneResponse> phones = user.getPhones().stream().map(ph -> {
            UserResponse.PhoneResponse pr = new UserResponse.PhoneResponse();
            pr.setNumber(ph.getNumber());
            pr.setCitycode(ph.getCitycode());
            pr.setCountrycode(ph.getCountrycode());
            return pr;
        }).collect(java.util.stream.Collectors.toList());
        resp.setPhones(phones);
        return resp;
    }
}
