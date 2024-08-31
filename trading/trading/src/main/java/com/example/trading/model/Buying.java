package com.example.trading.model;

import com.example.trading.datafetcher.realtime.RealtimeClosingPriceDataFetcher;
import com.example.trading.kafka.consumer.BuyingConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Buying {

    RealtimeClosingPriceDataFetcher realtimeClosingPriceDataFetcher;
    BuyingConsumer buyingConsumer;

    @Autowired
    public Buying(RealtimeClosingPriceDataFetcher realtimeClosingPriceDataFetcher, BuyingConsumer buyingConsumer)
    {
        this.realtimeClosingPriceDataFetcher = realtimeClosingPriceDataFetcher;
        this.buyingConsumer = buyingConsumer;
    }

    public void start(String stock)
    {
        realtimeClosingPriceDataFetcher.connect(stock);
        buyingConsumer.poll();
    }

    public void complete()
    {
        realtimeClosingPriceDataFetcher.disConnect();
    }
}