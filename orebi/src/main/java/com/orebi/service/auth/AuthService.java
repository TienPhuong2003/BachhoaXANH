package com.orebi.service.auth;

import org.springframework.http.ResponseEntity;
import com.orebi.dto.authDTO.LoginDTO;
import com.orebi.dto.authDTO.RegisterDTO;

public interface AuthService {
   ResponseEntity<?> registerUser(RegisterDTO registerDTO);
   ResponseEntity<?> loginUser(LoginDTO loginDTO);
   ResponseEntity<?> verifyAccount(String email, String otp);
   ResponseEntity<?> forgotPassword(String email);
   ResponseEntity<?> resetPassword(String token, String newPass);
   ResponseEntity<?> sendOtp(String email);  
}