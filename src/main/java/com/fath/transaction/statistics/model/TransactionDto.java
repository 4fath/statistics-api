package com.fath.transaction.statistics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto implements Serializable {

    private static final long serialVersionUID = 5246499922593380143L;

    private double amount;
    private long timestamp;
}
