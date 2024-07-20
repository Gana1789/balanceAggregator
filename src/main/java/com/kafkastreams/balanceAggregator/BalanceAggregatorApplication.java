package com.kafkastreams.balanceAggregator;

import com.kafkastreams.balanceAggregator.config.StreamConfiguration;
import com.kafkastreams.balanceAggregator.topology.BankBalanceTopology;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableAutoConfiguration
public class BalanceAggregatorApplication {
	@Autowired
	BankBalanceTopology bankBalanceTopology;
	public static void main(String[] args) {
		SpringApplication.run(BalanceAggregatorApplication.class, args);
	}

	@PostConstruct
	public void startBankBalanceTopology() {
		var configuration= StreamConfiguration.defaultKafkaStreamsConfig().getStreamsConfiguration();
		var topology= bankBalanceTopology.buildTopology(new StreamsBuilder());
		assert configuration != null;
		var kafkaStreams= new KafkaStreams(topology,configuration);
		kafkaStreams.start();
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
	}

}
