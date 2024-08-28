package com.example.trading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    String appKey;
    String appSecret;
    String accessToken;
    String approvalKey;
    String analyticsServer;
}
