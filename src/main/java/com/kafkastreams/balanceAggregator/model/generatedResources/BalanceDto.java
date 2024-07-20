package com.kafkastreams.balanceAggregator.model.generatedResources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalanceDto {

    long id;
    BigDecimal balanceAmount;
    LocalDate lastUpdateDate;
}
