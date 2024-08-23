package com.example.trading.datafetcher.realtime;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RealTimeDataParser {

    private List<JSONObject> parseClosingPriceData(String data)
    {
        String[] parts = data.split("\\^\\^77700");

        // 결과를 담을 리스트
        List<String[]> groupedParts = new ArrayList<>();

        for (String part : parts) {
            if (part.startsWith("^")) {
                part = part.substring(1);
            }
            String[] group = part.split("\\^");
            groupedParts.add(group);
        }

        List<JSONObject> result = new ArrayList<>();
        String[] infoList = getClosingPriceDataInfoList();
        for (String[] group : groupedParts) {
            JSONObject jsonObj = new JSONObject();
            for(int i = 0; i < infoList.length; i++)
            {
                jsonObj.put(infoList[i], group[i]);
            }
            System.out.println(jsonObj);
            result.add(jsonObj);
        }
        return result;
    }

    private static String[] getClosingPriceDataInfoList() {
        String info = "MKSC_SHRN_ISCD|STCK_CNTG_HOUR|STCK_PRPR|PRDY_VRSS_SIGN|PRDY_VRSS|PRDY_CTRT|WGHN_AVRG_STCK_PRC|STCK_OPRC|STCK_HGPR|STCK_LWPR|ASKP1|BIDP1|CNTG_VOL|ACML_VOL|ACML_TR_PBMN|SELN_CNTG_CSNU|SHNU_CNTG_CSNU|NTBY_CNTG_CSNU|CTTR|SELN_CNTG_SMTN|SHNU_CNTG_SMTN|" +
                "CCLD_DVSN|SHNU_RATE|PRDY_VOL_VRSS_ACML_VOL_RATE|OPRC_HOUR|OPRC_VRSS_PRPR_SIGN|OPRC_VRSS_PRPR|HGPR_HOUR|HGPR_VRSS_PRPR_SIGN|HGPR_VRSS_PRPR|LWPR_HOUR|LWPR_VRSS_PRPR_SIGN|LWPR_VRSS_PRPR|BSOP_DATE|NEW_MKOP_CLS_CODE|TRHT_YN|ASKP_RSQN1|BIDP_RSQN1|" +
                "TOTAL_ASKP_RSQN|TOTAL_BIDP_RSQN|VOL_TNRT|PRDY_SMNS_HOUR_ACML_VOL|PRDY_SMNS_HOUR_ACML_VOL_RATE|HOUR_CLS_CODE";
        return info.split("\\|");
    }


    public List<JSONObject> parse(String message)
    {
        String[] mData = message.split("\\|");
        System.out.println(message);
        String tr_id = mData[1];
        switch (tr_id)	{
            case "H0STASP0":
                break;
            case "H0STCNT0":
                return parseClosingPriceData(mData[3]);
            default:
                break;
        }
        return null;
    }

}
