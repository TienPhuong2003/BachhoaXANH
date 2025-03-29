package com.orebi.service.email;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.orebi.dto.UserDTO;
import com.orebi.entity.User;
import com.orebi.exception.InvalidOtpException;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.repository.UserRepository;

@Service
public class OtpService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void generateAndSendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Email"));

        // Xóa OTP cũ nếu có
        clearExpiredOtp(email);

        // Tạo OTP ngẫu nhiên 6 số
        String otp = generateOTP();

        // Lưu OTP và thời gian hết hạn (5 phút)
        user.setOtp(otp);
        user.setOtpExpiredAt(LocalDateTime.now().plusMinutes(5));
        user.setOtpVerified(false);
        userRepository.save(user);

        // Gửi OTP qua email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP xác thực");
        message.setText("Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong 5 phút.");
        mailSender.send(message);
    }

    public UserDTO verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        if (user.getOtp() == null || user.getOtpExpiredAt() == null) {
            throw new InvalidOtpException("OTP không tồn tại hoặc đã được sử dụng");
        }
 
        if (user.getOtp().equals(otp)) {
            if (LocalDateTime.now().isBefore(user.getOtpExpiredAt())) {
                user.setOtpVerified(true);
                user.setOtp(null);
                user.setOtpExpiredAt(null);
                userRepository.save(user);
                return new UserDTO(user); 
            } else {
                clearExpiredOtp(email);
                throw new InvalidOtpException("OTP đã hết hạn");
            }
        }
        throw new InvalidOtpException("OTP không hợp lệ");
    }

    private void clearExpiredOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        user.setOtp(null);
        user.setOtpExpiredAt(null);
        user.setOtpVerified(false);
        userRepository.save(user);
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}