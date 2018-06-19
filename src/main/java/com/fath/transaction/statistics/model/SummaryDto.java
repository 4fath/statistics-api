package com.fath.transaction.statistics.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummaryDto implements Serializable {

    private static final long serialVersionUID = 7739744852403616361L;

    @JsonIgnore
    private long timestamp;
    private double sum;
    private double average;
    private double max;
    private double min;
    private int count;

}
