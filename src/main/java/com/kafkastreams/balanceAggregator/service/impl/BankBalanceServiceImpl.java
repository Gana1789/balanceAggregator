package com.kafkastreams.balanceAggregator.service.impl;

import com.kafkastreams.balanceAggregator.model.generatedResources.BankBalance;
import com.kafkastreams.balanceAggregator.model.generatedResources.BankTransaction;
import com.kafkastreams.balanceAggregator.model.generatedResources.BankTransactionState;
import com.kafkastreams.balanceAggregator.service.BankBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.kafkastreams.balanceAggregator.util.constants.BANK_TRANSACTIONS;

@Service
public class BankBalanceServiceImpl implements BankBalanceService {

    @Autowired
    KafkaTemplate<Long, Object> kafkaTemplate;


    @Override
    public BankBalance processBalanceUpdate(BankBalance bankBalance, BankTransaction bankTransaction) {
        bankBalance.setId(bankTransaction.getBalanceId());
        bankBalance.setLastBalanceUpdatedDate(bankTransaction.getTime());
        if (bankBalance.getBalanceAmount()==null ) {
            bankBalance.setBalanceAmount(BigDecimal.ZERO);
        }

            BigDecimal balance=new BigDecimal( bankBalance.getBalanceAmount().toString());

            if(balance.add(new BigDecimal(bankTransaction.getAmount().toString())).compareTo(BigDecimal.ZERO)>=0){
                bankTransaction.setTransactionState(BankTransactionState.APPROVED);
                bankBalance.setLatestTransaction(bankTransaction);
                bankBalance.setBalanceAmount(balance.add(new BigDecimal(bankTransaction.getAmount().toString())));
            }
            else{
                bankTransaction.setTransactionState(BankTransactionState.REJECTED);
                bankBalance.setLatestTransaction(bankTransaction);
            }


        return bankBalance;
    }




    @Override
    public void sendBankTransaction(BankTransaction bankTransaction){
        kafkaTemplate.send(BANK_TRANSACTIONS, bankTransaction.getBalanceId(), bankTransaction);
    }
}
