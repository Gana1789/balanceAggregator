package com.kafkastreams.balanceAggregator.service;

import com.kafkastreams.balanceAggregator.model.generatedResources.BankBalance;
import com.kafkastreams.balanceAggregator.model.generatedResources.BankTransaction;

public interface BankBalanceService {

    public void sendBankTransaction(BankTransaction bankTransaction);

    public BankBalance processBalanceUpdate(BankBalance bankBalance, BankTransaction bankTransaction);


}

