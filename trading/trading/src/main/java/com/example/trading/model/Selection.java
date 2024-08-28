package com.example.trading.model;

import com.example.trading.manager.AuthManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class Selection {
    RestClient restClient;
    AuthManager authManager;
    private Queue<String> stocks;

    @Autowired
    public Selection(AuthManager authManager)
    {
        this.stocks = new LinkedList<>();
        this.authManager = authManager;
        restClient = RestClient.create();
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

    private List<JSONObject> getFluctuationRate()
    {
        try{
            JSONObject header = new JSONObject();
            header.put("content-type","application/json; charset=utf-8");
            header.put("authorization", authManager.getAccessToken());
            header.put("appkey", authManager.getAppKey());
            header.put("appsecret", authManager.getAppSecret());
            header.put("tr_id", "FHPST01700000");
            header.put("custtype", "P");

            ResponseEntity<JSONObject> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/ranking/fluctuation")
                            .queryParam("fid_rsfl_rate2", "")
                            .queryParam("fid_cond_mrkt_div_code", "J")
                            .queryParam("fid_cond_scr_div_code", "20170")
                            .queryParam("fid_input_iscd", "0000")
                            .queryParam("fid_rank_sort_cls_code", "0")
                            .queryParam("fid_input_cnt_1", "0")
                            .queryParam("fid_prc_cls_code", "0")
                            .queryParam("fid_input_price_1", "")
                            .queryParam("fid_input_price_2", "")
                            .queryParam("fid_vol_cnt", "")
                            .queryParam("fid_trgt_cls_code", "0")
                            .queryParam("fid_trgt_exls_cls_code", "0")
                            .queryParam("fid_div_cls_code", "0")
                            .queryParam("fid_rsfl_rate1", "")
                            .build()
                    )
                    .header(header.toString())
                    .retrieve()
                    .toEntity(JSONObject.class);
            return (List<JSONObject>) response.getBody().get("output");
        }
        catch (JSONException | RestClientException e) {
            throw new RuntimeException(e.getMessage());

        }
    }

    private List<JSONObject> getTradingVolume()
    {
        try{
            JSONObject header = new JSONObject();
            header.put("content-type","application/json; charset=utf-8");
            header.put("authorization", authManager.getAccessToken());
            header.put("appkey", authManager.getAppKey());
            header.put("appsecret", authManager.getAppSecret());
            header.put("tr_id", "FHPST01710000");
            header.put("custtype", "P");

            ResponseEntity<JSONObject> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/quotations/volume-rank")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_COND_SCR_DIV_CODE", "20171")
                            .queryParam("FID_INPUT_ISCD", "0000")
                            .queryParam("FID_DIV_CLS_CODE", "0")
                            .queryParam("FID_BLNG_CLS_CODE", "1")
                            .queryParam("FID_TRGT_CLS_CODE", "111111111")
                            .queryParam("FID_TRGT_EXLS_CLS_CODE", "0000000000")
                            .queryParam("FID_INPUT_PRICE_1", "")
                            .queryParam("FID_INPUT_PRICE_2", "")
                            .queryParam("FID_VOL_CNT", "")
                            .queryParam("FID_INPUT_DATE_1", "")
                            .build()
                    )
                    .header(header.toString())
                    .retrieve()
                    .toEntity(JSONObject.class);
            return (List<JSONObject>) response.getBody().get("output");
        }
        catch (JSONException | RestClientException e) {
            throw new RuntimeException(e.getMessage());

        }
    }

    private JSONObject getCurrentPrice(String code)
    {
        try{
            JSONObject header = new JSONObject();
            header.put("authorization", authManager.getAccessToken());
            header.put("appkey", authManager.getAppKey());
            header.put("appsecret", authManager.getAppSecret());
            header.put("tr_id", "FHKST01010100");

            ResponseEntity<JSONObject> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/quotations/inquire-price")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_INPUT_ISCD", code)
                            .build()
                    )
                    .header(header.toString())
                    .retrieve()
                    .toEntity(JSONObject.class);
            return (JSONObject) response.getBody().get("output");
        }
        catch (JSONException | RestClientException e) {
            throw new RuntimeException(e.getMessage());

        }
    }

    public void start()
    {
        List<JSONObject> tradingVolumeInfoList = getTradingVolume();
        List<JSONObject> fluctuationRateInfoList = getFluctuationRate();
        for(JSONObject tradingVolumeInfo : tradingVolumeInfoList)
        {
            if((double)tradingVolumeInfo.get("prdy_ctrt") <= 3.0)
                continue;

            for(JSONObject fluctuationRateInfo : fluctuationRateInfoList)
            {
                if(((String)tradingVolumeInfo.get("mksc_shrn_iscd")).equals((String)fluctuationRateInfo.get("stck_shrn_iscd")))
                {
                    stocks.add((String) tradingVolumeInfo.get("mksc_shrn_iscd"));
                }
            }
        }

    }

}