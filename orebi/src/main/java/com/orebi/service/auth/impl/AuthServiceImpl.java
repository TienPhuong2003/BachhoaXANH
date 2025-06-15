package com.orebi.service.auth.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orebi.dto.authDTO.LoginDTO;
import com.orebi.dto.authDTO.RegisterDTO;
import com.orebi.dto.response.MessageResponse;
import com.orebi.entity.Role;
import com.orebi.entity.User;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.repository.RoleRepository;
import com.orebi.repository.UserRepository;
import com.orebi.security.JwtTokenUtil;
import com.orebi.service.auth.AuthService;
import com.orebi.service.cart.CartService;
import com.orebi.service.email.EmailService;
import com.orebi.service.email.OtpService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final CartService cartService;

    @Value("${reset.password.url}")
    private String resetPasswordUrl;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            OtpService otpService,
            CartService cartService,
            JwtTokenUtil jwtTokenUtil,
            AuthenticationManager authenticationManager,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.cartService = cartService;
    }

    // đăng ký
    @Override
    public ResponseEntity<?> registerUser(RegisterDTO registerDTO) {
        try {
            // Kiểm tra email đã tồn tại
            Optional<User> existingUser = userRepository.findByEmail(registerDTO.getEmail());

            if (existingUser.isPresent()) {
                User user = existingUser.get();
                // Nếu tài khoản chưa xác thực OTP, xóa và đăng ký lại
                if (!user.isOtpVerified()) {
                    // Gửi lại OTP, không xóa user
                    otpService.generateAndSendOtp(user.getEmail());
                    return ResponseEntity.ok(new MessageResponse(
                            "Email đã tồn tại nhưng chưa xác thực. Đã gửi lại mã OTP mới, vui lòng kiểm tra email để xác thực tài khoản"));
                } else {
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Email đã tồn tại và đã được xác thực"));
                }
            }

            // Tạo user mới
            User newUser = new User();
            newUser.setName(registerDTO.getName());
            newUser.setEmail(registerDTO.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            newUser.setPhone(registerDTO.getPhone());
            newUser.setAddress(registerDTO.getAddress());
            newUser.setProvince(registerDTO.getProvince());
            newUser.setDistrict(registerDTO.getDistrict());
            newUser.setWard(registerDTO.getWard());
            newUser.setOtpVerified(false);

            // Set role mặc định
            Role userRole = roleRepository.findByRoleName("ROLE_USER")
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ROLE_USER"));
            newUser.setRole(userRole);
            userRepository.save(newUser);

            otpService.generateAndSendOtp(newUser.getEmail());

            return ResponseEntity.ok(new MessageResponse(
                    "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Lỗi đăng ký: " + e.getMessage()));
        }
    }

    // đăng nhập
    @Override
    public ResponseEntity<?> loginUser(LoginDTO loginDTO) {
        try {
            User user = userRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

            if (!user.isOtpVerified()) {
                otpService.generateAndSendOtp(user.getEmail());
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Tài khoản chưa xác thực. OTP mới đã được gửi đến email."));
            }
            if (!user.isActive()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse(
                                "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ admin để biết thêm thông tin."));
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenUtil.generateToken(authentication, user.getUserId(), user.getRole().getRoleName());

            return ResponseEntity.ok(Collections.singletonMap("accessToken", token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email hoặc mật khẩu không chính xác"));
        }
    }

    // xác thực otp
    @Override
    public ResponseEntity<?> verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("không tìm thấy người dùng"));

        if (!user.getOtp().equals(otp)) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP không chính xác"));
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiredAt())) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP đã hết hạn"));
        }

        user.setOtpVerified(true);
        user.setOtp(null);
        user.setOtpExpiredAt(null);
        user.setActive(true);
        userRepository.save(user);

        // Tạo giỏ hàng cho người dùng mới
        cartService.getOrCreateCartEntity(user.getUserId());

        return ResponseEntity.ok(new MessageResponse("Xác thực tài khoàn thành công"));
    }

    // Đặt lại mật khẩu
    @Override
    public ResponseEntity<?> resetPassword(String token, String newPassword) {
        String email = jwtTokenUtil.validatePasswordResetToken(token);
        if (email == null) {
            throw new RuntimeException("Token không hợp lệ hoặc đã hết hạn");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        try {
            return ResponseEntity.ok(new MessageResponse("Mật khẩu đã được đặt lại thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Lỗi: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email này"));

            String resetToken = jwtTokenUtil.generatePasswordResetToken(user.getEmail());
            String resetLink = resetPasswordUrl + resetToken;

            emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
            return ResponseEntity.ok(new MessageResponse("Link đặt lại mật khẩu đã được gửi đến email của bạn"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Lỗi: " + e.getMessage()));
        }
    }

    @Override
    // Gửi OTP
    public ResponseEntity<?> sendOtp(String email) {
        try {
            otpService.generateAndSendOtp(email);
            return ResponseEntity.ok(new MessageResponse("OTP đã được gửi"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Lỗi khi gửi OTP: " + e.getMessage()));
        }
    }

}
