package com.example.trading.manager;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class StockOrderRequestManager {

    private final String uri = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/trading/order-cash";
    private final String cano = "12345678"; // 계좌 앞 8자리
    private final String acntPrdtCd = "00"; // 계좌 뒤 2자리
    private final String ordDvsn = "00"; // 주문 구분 00 : 지정가


    RestClient restClient;
    AuthManager authManager;
    @Autowired
    public StockOrderRequestManager(AuthManager authManager)
    {
        this.authManager = authManager;
        restClient = RestClient.create();
    }

    public void buy(String stock, String quantity, String price)
    {
        JSONObject header = new JSONObject();
        header.put("authorization", "Bearer " + authManager.getAccessToken());
        header.put("appkey", authManager.getAppKey());
        header.put("appsecret",authManager.getAppSecret());
        header.put("tr_id", "TTTC0802U");

        JSONObject body = new JSONObject();
        body.put("CANO", cano);
        body.put("ACNT_PRDT_CD", acntPrdtCd);
        body.put("PDNO", stock);
        body.put("ORD_DVSN", ordDvsn);
        body.put("ORD_QTY", quantity);
        body.put("ORD_UNPR", price);

        ResponseEntity<JSONObject> response = restClient.post()
                .uri(uri)
                .header(header.toString())
                .body(body.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(JSONObject.class);

    }

    public void sell(String stock, String quantity, String price)
    {
        JSONObject header = new JSONObject();
        header.put("authorization", "Bearer " + authManager.getAccessToken());
        header.put("appkey", authManager.getAppKey());
        header.put("appsecret",authManager.getAppSecret());
        header.put("tr_id", "TTTC0801U");

        JSONObject body = new JSONObject();
        body.put("CANO", cano);
        body.put("ACNT_PRDT_CD", acntPrdtCd);
        body.put("PDNO", stock);
        body.put("ORD_DVSN", ordDvsn);
        body.put("ORD_QTY", quantity);
        body.put("ORD_UNPR", price);

        ResponseEntity<JSONObject> response = restClient.post()
                .uri(uri)
                .header(header.toString())
                .body(body.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(JSONObject.class);

    }



}