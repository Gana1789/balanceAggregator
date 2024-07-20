package com.kafkastreams.balanceAggregator.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class StreamConfiguration {

    @Bean(name =  KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public static StreamsBuilderFactoryBean defaultKafkaStreamsConfig(){
        Map<String, Object> config=new HashMap<>();

        config.put(StreamsConfig.APPLICATION_ID_CONFIG,"bankBalanceApp");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_DOC,"0");
        config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);

        config.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(config));
    }




//    @Bean
//    public KafkaStreamsConfiguration bankBalanceStreamConfig(){
//        Map<String, Object> props = new HashMap<>();
//        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "bankBalance");
//        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
//        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAv);
//        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams-2");
//
//        return new KafkaStreamsConfiguration(props);
//    }
}
