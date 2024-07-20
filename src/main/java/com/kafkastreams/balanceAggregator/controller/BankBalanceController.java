package com.kafkastreams.balanceAggregator.controller;


import com.kafkastreams.balanceAggregator.model.generatedResources.BalanceDto;
import com.kafkastreams.balanceAggregator.model.generatedResources.BankBalance;
import com.kafkastreams.balanceAggregator.model.generatedResources.BankTransaction;
import com.kafkastreams.balanceAggregator.service.BankBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@Slf4j
public class BankBalanceController {

    @Autowired
    BankBalanceService bankBalanceService;
    @Autowired
    StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    @PostMapping("/postTransaction")
    public String postBankTransaction(@RequestBody BankTransaction bankTransaction){
        bankBalanceService.sendBankTransaction(bankTransaction);
        return "BankTransaction processed";
    }


    @GetMapping("/getBalance/{accountId}")
    public BalanceDto getBankBalance(@PathVariable String accountId) {
long accountIdval=Long.parseLong(accountId);
        ReadOnlyKeyValueStore<Long, BankBalance> store =
                Objects.requireNonNull(streamsBuilderFactoryBean.getKafkaStreams()).store(StoreQueryParameters.fromNameAndType("balanceStore", QueryableStoreTypes.keyValueStore()));
        return BalanceDto.builder().balanceAmount((BigDecimal) store.get(accountIdval).getBalanceAmount()).lastUpdateDate(store.get(accountIdval).getLastBalanceUpdatedDate()).id(accountIdval).build();

    }
}
