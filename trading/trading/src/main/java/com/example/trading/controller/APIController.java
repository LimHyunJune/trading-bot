package com.example.trading.controller;

import com.example.trading.auth.AuthDTO;

import com.example.trading.auth.AuthManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
public class APIController {

    @Autowired
    AuthManager authManager;

    @PostMapping("/register")
    public void register(@RequestBody AuthDTO authDTO) {
        authManager.setAppKey(authDTO.getAppKey());
        authManager.setAppSecret(authDTO.getAppSecret());
        authManager.getWebSocketConnectionKey();
    }

    @GetMapping("/test")
    public String test()
    {
        return authManager.getApprovalKey();
    }
}
