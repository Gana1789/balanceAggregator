package com.kafkastreams.balanceAggregator.topology;

import com.kafkastreams.balanceAggregator.model.generatedResources.BankBalance;
import com.kafkastreams.balanceAggregator.model.generatedResources.BankTransaction;
import com.kafkastreams.balanceAggregator.model.generatedResources.BankTransactionState;
import com.kafkastreams.balanceAggregator.service.BankBalanceService;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BankBalanceTopology {
    
    
    @Autowired
    BankBalanceService bankBalanceService;

    @Autowired
    public Topology buildTopology(StreamsBuilder streamsBuilder){
//        StreamsBuilder streamsBuilder=new StreamsBuilder();
        Map<String, Object> serdeConfig=new HashMap<>();
        serdeConfig.put("schema.registry.url", "http://localhost:8081");
        SpecificAvroSerde<BankTransaction> bankTransactionSpecificAvroSerde = new SpecificAvroSerde<>();
        SpecificAvroSerde<BankBalance> bankBalanceSpecificAvroSerde = new SpecificAvroSerde<>();
        bankTransactionSpecificAvroSerde.configure(serdeConfig, false);
        bankBalanceSpecificAvroSerde.configure(serdeConfig,false);
//        JsonSerde<BankBalance> BankingJsonSerde= new JsonSerde<>();
//        CustomJsonSerde<BankBalance> BankBalanceJsonSerde = new CustomJsonSerde<>(BankBalance.class);
        KStream<Long, BankBalance> bankBalanceKStream = streamsBuilder.stream("bank-transactions", Consumed.with(Serdes.Long(), bankTransactionSpecificAvroSerde))
                .groupByKey()
                .aggregate(BankBalance::new,
                        (key, bankTransaction, balance) -> bankBalanceService.processBalanceUpdate(balance,bankTransaction),
                        Materialized.<Long,BankBalance, KeyValueStore<Bytes, byte[]>>as("balanceStore").
                                withKeySerde(Serdes.Long()).withValueSerde(bankBalanceSpecificAvroSerde)
                       ).toStream();

        bankBalanceKStream.filter((key,value)-> value.getLatestTransaction().getTransactionState()!= BankTransactionState.REJECTED);
        bankBalanceKStream.to("bank-balances", Produced.with(Serdes.Long(), bankBalanceSpecificAvroSerde));
        return streamsBuilder.build();
    }
}
