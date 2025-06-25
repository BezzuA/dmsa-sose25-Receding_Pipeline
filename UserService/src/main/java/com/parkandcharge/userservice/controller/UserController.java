package com.parkandcharge.userservice.controller;
import com.parkandcharge.userservice.dto.AuthRequest;
import com.parkandcharge.userservice.dto.PaymentDto;
import com.parkandcharge.userservice.dto.RegisterRequest;
import com.parkandcharge.userservice.model.User;
import com.parkandcharge.userservice.repository.UserRepository;
import com.parkandcharge.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin // allows frontend calls
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody RegisterRequest request) {
        User createdUser = userService.createUser(request);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/signin")
    public User signin(@Valid @RequestBody AuthRequest request) {
        return userService.authenticate(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody RegisterRequest user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(user.getBalance());
    }

    @PostMapping("/{id}/topup")
    public ResponseEntity<Double> topUp(@PathVariable Long id, @RequestParam Double amount) {
        // Step 1: Make mock payment request
        Map<String, Object> paymentPayload = new HashMap<>();
        paymentPayload.put("userId", id);
        paymentPayload.put("amount", amount);

        // POST to PaymentService
        String paymentUrl = "http://paymentservice/api/payments";
        PaymentDto paymentResponse = restTemplate.postForObject(paymentUrl, paymentPayload, PaymentDto.class);

        if (paymentResponse == null || !"PAID".equals(paymentResponse.getStatus())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Add to user balance
        User user = userRepository.findById(id).orElseThrow();
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        return ResponseEntity.ok(user.getBalance());
    }

    @PostMapping("/{id}/deduct")
    public ResponseEntity<Void> deductBalance(@PathVariable Long id, @RequestParam double amount) {
        userService.deductBalance(id, amount);
        return ResponseEntity.ok().build();
    }

}