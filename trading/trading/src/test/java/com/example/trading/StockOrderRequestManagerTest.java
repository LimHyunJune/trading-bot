package com.example.trading;

import com.example.trading.manager.AuthManager;
import com.example.trading.manager.StockOrderRequestManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StockOrderRequestManagerTest {
    private static final Logger logger = LoggerFactory.getLogger(StockOrderRequestManagerTest.class);
    @Value("${appKey}")
    static String appKey;
    @Value("appSecret")
    static String appSecret;
    @Value("appApprovalKey")
    static String appApprovalKey;
    @Value("appAccessToken")
    static String appAccessToken;
    static StockOrderRequestManager stockOrderRequestManager;

    @BeforeAll
    public static void init(){
        AuthManager authManager = new AuthManager();
        authManager.setAppKey(appKey);
        authManager.setAppSecret(appSecret);
        authManager.setApprovalKey(appApprovalKey);
        authManager.setAccessToken(appAccessToken);
        stockOrderRequestManager = new StockOrderRequestManager(authManager);
    }

    @Test
    public void buyTest(){
        stockOrderRequestManager.buy("","","");
    }

    @Test
    public void sellTest(){
        stockOrderRequestManager.sell("","","");
    }

}