package com.example.trading.model;

import com.example.trading.manager.AuthManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class Selection {

    private static final Logger logger = LoggerFactory.getLogger(Selection.class);

    RestClient restClient;
    AuthManager authManager;
    private Queue<String> stocks;

    @Autowired
    public Selection(AuthManager authManager)
    {
        this.stocks = new LinkedList<>();
        this.authManager = authManager;
        restClient = RestClient.builder().baseUrl("https://openapi.koreainvestment.com:9443").build();
    }

    public boolean isEmpty()
    {
        return stocks.isEmpty();
    }

    public void clearStocks(){ stocks.clear();}

    public String getFirstStock()
    {
        return stocks.poll();
    }

    private List<JsonObject> getFluctuationRate()
    {
        try{

            ResponseEntity<String> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/ranking/fluctuation")
                            .queryParam("fid_rsfl_rate2","")
                            .queryParam("fid_cond_mrkt_div_code","J")
                            .queryParam("fid_cond_scr_div_code","20170")
                            .queryParam("fid_input_iscd","0000")
                            .queryParam("fid_rank_sort_cls_code", "0")
                            .queryParam("fid_input_cnt_1","0")
                            .queryParam("fid_prc_cls_code","0")
                            .queryParam("fid_input_price_1","")
                            .queryParam("fid_input_price_2","")
                            .queryParam("fid_vol_cnt","")
                            .queryParam("fid_trgt_cls_code", "0")
                            .queryParam("fid_trgt_exls_cls_code", "0")
                            .queryParam("fid_div_cls_code", "0")
                            .queryParam("fid_rsfl_rate1","")
                            .build()
                    )
                    .headers(headers -> {
                        headers.set("content-type","application/json; charset=utf-8");
                        headers.set("authorization", "Bearer " + authManager.getAccessToken());
                        headers.set("appkey", authManager.getAppKey());
                        headers.set("appsecret", authManager.getAppSecret());
                        headers.set("tr_id", "FHPST01700000");
                        headers.set("custtype", "P");
                    })
                    .retrieve()
                    .toEntity(String.class);
            JsonArray  jsonArray = JsonParser.parseString(response.getBody()).getAsJsonObject().getAsJsonArray("output");
            List<JsonObject> jsonObjectList = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                if (element.isJsonObject()) {
                    jsonObjectList.add(element.getAsJsonObject());
                }
            }
            return jsonObjectList;
        }
        catch (JSONException | RestClientException e) {
            throw new RuntimeException(e.getMessage());

        }
    }

    private List<JsonObject> getTradingVolume()
    {
        try{

            ResponseEntity<String> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/quotations/volume-rank")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_COND_SCR_DIV_CODE", "20171")
                            .queryParam("FID_INPUT_ISCD", "0000")
                            .queryParam("FID_DIV_CLS_CODE", "1")
                            .queryParam("FID_BLNG_CLS_CODE", "1")
                            .queryParam("FID_TRGT_CLS_CODE", "111111111")
                            .queryParam("FID_TRGT_EXLS_CLS_CODE", "0000000000")
                            .queryParam("FID_INPUT_PRICE_1", "")
                            .queryParam("FID_INPUT_PRICE_2", "")
                            .queryParam("FID_VOL_CNT", "")
                            .queryParam("FID_INPUT_DATE_1", "")
                            .build()
                    )
                    .headers(headers -> {
                        headers.set("content-type","application/json; charset=utf-8");
                        headers.set("authorization", "Bearer " + authManager.getAccessToken());
                        headers.set("appkey", authManager.getAppKey());
                        headers.set("appsecret", authManager.getAppSecret());
                        headers.set("tr_id", "FHPST01710000");
                        headers.set("custtype", "P");
                    })
                    .retrieve()
                    .toEntity(String.class);
            JsonArray  jsonArray = JsonParser.parseString(response.getBody()).getAsJsonObject().getAsJsonArray("output");
            List<JsonObject> jsonObjectList = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                if (element.isJsonObject()) {
                    jsonObjectList.add(element.getAsJsonObject());
                }
            }
            return jsonObjectList;
        }
        catch (JSONException | RestClientException e) {
            throw new RuntimeException(e.getMessage());

        }
    }

//    private JSONObject getCurrentPrice(String code)
//    {
//        try{
//            JSONObject header = new JSONObject();
//            header.put("authorization", authManager.getAccessToken());
//            header.put("appkey", authManager.getAppKey());
//            header.put("appsecret", authManager.getAppSecret());
//            header.put("tr_id", "FHKST01010100");
//
//            ResponseEntity<JSONObject> response = restClient.get()
//                    .uri(uriBuilder -> uriBuilder
//                            .path("https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/quotations/inquire-price")
//                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
//                            .queryParam("FID_INPUT_ISCD", code)
//                            .build()
//                    )
//                    .header(header.toString())
//                    .retrieve()
//                    .toEntity(JSONObject.class);
//            return (JSONObject) response.getBody().get("output");
//        }
//        catch (JSONException | RestClientException e) {
//            throw new RuntimeException(e.getMessage());
//
//        }
//    }

    public void start()
    {
        List<JsonObject> tradingVolumeInfoList = getTradingVolume();
        List<JsonObject> fluctuationRateInfoList = getFluctuationRate();

        for(JsonObject tradingVolumeInfo : tradingVolumeInfoList)
        {
            if(tradingVolumeInfo.get("prdy_ctrt").getAsDouble() <= 3.0)
                continue;

            for(JsonObject fluctuationRateInfo : fluctuationRateInfoList)
            {
                if((tradingVolumeInfo.get("mksc_shrn_iscd").getAsString()).equals(fluctuationRateInfo.get("stck_shrn_iscd").getAsString()))
                {
                    logger.info("선정된 주식 : " + tradingVolumeInfo.get("hts_kor_isnm"));
                    stocks.add(tradingVolumeInfo.get("mksc_shrn_iscd").getAsString());
                }
            }
        }

    }

}