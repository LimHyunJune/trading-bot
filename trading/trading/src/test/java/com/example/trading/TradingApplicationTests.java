package com.example.trading;

import com.example.trading.kafka.producer.RealtimeClosingPriceProducer;
import com.example.trading.util.RealTimeDataParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

@SpringBootTest
class TradingApplicationTests {

	@Autowired
	RealTimeDataParser realTimeDataParser;

	@Autowired
	RealtimeClosingPriceProducer realtimeClosingPriceProducer;

	@Test
	void parsingTest() {
		realTimeDataParser.parse("0|H0STCNT0|001|005930^140027^78100^5^-200^-0.26^" +
				"77749.74^77700^78100^77500^78100^78000^28^6564772^510412031500^" +
				"22785^29411^6626^76.04^3522285^2678247^1^0.41^80.56^090029^2^400^092550^" +
				"3^0^090120^2^600^20240823^20^N^95079^45537^544673^1846152^0.11^5220874^" +
				"125.74^0^^77700^005930^140027^78100^5^-200^-0.26^77749.75^77700^78100^" +
				"77500^78100^78000^79^6564851^510418201400^22785^29412^6627^76.04^" +
				"3522285^2678326^1^0.41^80.56^090029^2^400^092550^3^0^090120^2^600^" +
				"20240823^20^N^95079^45537^544673^1846152^0.11^5220874^125.74^0^^77700^" +
				"005930^140027^78100^5^-200^-0.26^77749.75^77700^78100^77500^78100^78000^1^" +
				"6564852^510418279500^22785^29413^6628^76.04^3522285^2678327^1^0.41^80.56^" +
				"090029^2^400^092550^3^0^090120^2^600^20240823^20^N^94973^" +
				"45558^544567^1846173^0.11^5220874^125.74^0^^77700");
	}

	@Test
	void realtimeClosingPriceProducerTest()
	{
		realtimeClosingPriceProducer.init("ad0d7b6f5b93548c3bda5dce9c7377bd-737115876.ap-northeast-3.elb.amazonaws.com:9094");
		String message = "0|H0STCNT0|001|005930^140027^78100^5^-200^-0.26^" +
				"77749.74^77700^78100^77500^78100^78000^28^6564772^510412031500^" +
				"22785^29411^6626^76.04^3522285^2678247^1^0.41^80.56^090029^2^400^092550^" +
				"3^0^090120^2^600^20240823^20^N^95079^45537^544673^1846152^0.11^5220874^" +
				"125.74^0^^77700^005930^140027^78100^5^-200^-0.26^77749.75^77700^78100^" +
				"77500^78100^78000^79^6564851^510418201400^22785^29412^6627^76.04^" +
				"3522285^2678326^1^0.41^80.56^090029^2^400^092550^3^0^090120^2^600^" +
				"20240823^20^N^95079^45537^544673^1846152^0.11^5220874^125.74^0^^77700^" +
				"005930^140027^78100^5^-200^-0.26^77749.75^77700^78100^77500^78100^78000^1^" +
				"6564852^510418279500^22785^29413^6628^76.04^3522285^2678327^1^0.41^80.56^" +
				"090029^2^400^092550^3^0^090120^2^600^20240823^20^N^94973^" +
				"45558^544567^1846173^0.11^5220874^125.74^0^^77700";
		List<JSONObject> jsonObjectList = realTimeDataParser.parse(message);
		for(JSONObject jsonObject : jsonObjectList)
			realtimeClosingPriceProducer.send("005930",jsonObject.toString());
	}

	@Test
	void realtimeClosingPriceConsumerTest() {
		ObjectMapper objectMapper = new ObjectMapper();
		Properties props = new Properties();
		KafkaConsumer<String, byte[]> consumer;
		props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "ad0d7b6f5b93548c3bda5dce9c7377bd-737115876.ap-northeast-3.elb.amazonaws.com:9094");
		props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
		props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Pattern.compile("closing_price"));
		ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(1000));
	}

}
