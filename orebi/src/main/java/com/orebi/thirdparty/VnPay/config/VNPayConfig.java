package com.orebi.thirdparty.VnPay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VNPayConfig {

    @Value("${vnpay.merchant-id}")
    private String vnpTmnCode;

    @Value("${vnpay.secret-key}")
    private String vnpHashSecret;

    @Value("${vnpay.base-url}")
    private String vnpPayUrl;

    @Value("${vnpay.return-url}")
    private String vnpReturnUrl;

    @Bean
    public VNPayProperties vnPayProperties() {
        VNPayProperties props = new VNPayProperties();
        props.setMerchantId(vnpTmnCode);
        props.setSecretKey(vnpHashSecret);
        props.setBaseUrl(vnpPayUrl);
        props.setReturnUrl(vnpReturnUrl);
        return props;
    }
}
