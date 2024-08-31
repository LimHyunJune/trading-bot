package com.example.trading;

import com.example.trading.manager.AuthManager;
import com.example.trading.model.Selection;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@SpringBootTest
public class SelectionTests {
    private static final Logger logger = LoggerFactory.getLogger(SelectionTests.class);

    @Value("${appKey}")
    String appKey;
    @Value("${appSecret}")
    String appSecret;
    @Value("${approvalKey}")
    String approvalKey;
    @Value("${accessToken}")
    String accessToken;
    Selection selection;

    @BeforeEach
    public void init()
    {
        AuthManager authManager = new AuthManager();
        authManager.setAppKey(appKey);
        authManager.setAppSecret(appSecret);
        authManager.setApprovalKey(approvalKey);
        authManager.setAccessToken(accessToken);
        selection = new Selection(authManager);
    }

    @Test
    void getFluctuationRateTest()
    {
        try {
            Method method = selection.getClass().getDeclaredMethod("getFluctuationRate");
            method.setAccessible(true);
            List<JsonObject> fluctuationRateInfoList = (List<JsonObject>) method.invoke(selection);
            for (int i = 0; i < fluctuationRateInfoList.size(); i++) {
                JsonElement fluctuationRateInfo = fluctuationRateInfoList.get(i);
                logger.info(String.valueOf(fluctuationRateInfo));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getTradingVolumeTest()
    {
        try {
            Method method = selection.getClass().getDeclaredMethod("getTradingVolume");
            method.setAccessible(true);
            List<JsonObject> tradingVolumeInfoList = (List<JsonObject>) method.invoke(selection);
            for (int i = 0; i < tradingVolumeInfoList.size(); i++) {
                JsonElement tradingVolumeInfo = tradingVolumeInfoList.get(i);
                logger.info(String.valueOf(tradingVolumeInfo));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

//    @Test
//    void getCurrentPriceTest()
//    {
//        try {
//            Method method = selection.getClass().getDeclaredMethod("getCurrentPrice", String.class);
//            method.setAccessible(true);
//            JSONObject currentPriceInfo = (JSONObject) method.invoke(selection, "000660");
//            logger.info(String.valueOf(currentPriceInfo));
//        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Test
    void startTest()
    {
        selection.start();
        String stock = selection.getFirstStock();
        logger.info(stock);
    }
}