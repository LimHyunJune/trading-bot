package com.example.trading;

import com.example.trading.manager.AuthManager;
import com.example.trading.model.Selection;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
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
    static String appKey;
    @Value("appSecret")
    static String appSecret;
    @Value("appApprovalKey")
    static String appApprovalKey;
    @Value("appAccessToken")
    static String appAccessToken;
    static Selection selection;

    @BeforeAll
    static public void init()
    {
        AuthManager authManager = new AuthManager();
        authManager.setAppKey(appKey);
        authManager.setAppSecret(appSecret);
        authManager.setApprovalKey(appApprovalKey);
        authManager.setAccessToken(appAccessToken);
        selection = new Selection(authManager);
    }

    @Test
    void getFluctuationRateTest()
    {
        try {
            Method method = selection.getClass().getMethod("getFluctuationRate");
            method.setAccessible(true);
            List<JSONObject> fluctuationRateInfoList = (List<JSONObject>) method.invoke(selection);
            for(JSONObject fluctuationRateInfo : fluctuationRateInfoList)
                logger.info(String.valueOf(fluctuationRateInfo));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getTradingVolumeTest()
    {
        try {
            Method method = selection.getClass().getMethod("getTradingVolume");
            method.setAccessible(true);
            List<JSONObject> tradingVolumeInfoList = (List<JSONObject>) method.invoke(selection);
            for(JSONObject tradingVolumeInfo : tradingVolumeInfoList)
                logger.info(String.valueOf(tradingVolumeInfo));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getCurrentPriceTest()
    {
        try {
            Method method = selection.getClass().getMethod("getCurrentPrice", String.class);
            method.setAccessible(true);
            JSONObject currentPriceInfo = (JSONObject) method.invoke(selection, "000660");
            logger.info(String.valueOf(currentPriceInfo));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void startTest()
    {
        selection.start();
        String stock = selection.getFirstStock();
        logger.info(stock);
    }
}