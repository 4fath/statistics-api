package com.fath.transaction.statistics.controller;


import com.fath.transaction.statistics.Constants;
import com.fath.transaction.statistics.model.SummaryDto;
import com.fath.transaction.statistics.model.TransactionDto;
import com.fath.transaction.statistics.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @Autowired
    private ApiService service;

    @PostMapping("/transactions")
    public ResponseEntity addTransaction(@RequestBody TransactionDto dto) {
        long timeDiff = System.currentTimeMillis() - dto.getTimestamp();
        if (timeDiff < Constants.SIXTY_SEC) {
            service.saveTransaction(dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/statistics")
    public ResponseEntity getStatistics() {
        SummaryDto summary = service.getStatistics();
        return ResponseEntity.ok(summary);
    }

}
