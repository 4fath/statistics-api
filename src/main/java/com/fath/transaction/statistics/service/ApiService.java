package com.fath.transaction.statistics.service;

import com.fath.transaction.statistics.Constants;
import com.fath.transaction.statistics.dao.ApiRepository;
import com.fath.transaction.statistics.model.SummaryDto;
import com.fath.transaction.statistics.model.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApiService {

    @Autowired
    private ApiRepository repository;

    public void saveTransaction(final TransactionDto dto) {
        final long timeInterval = System.currentTimeMillis() - Constants.SIXTY_SEC;

        final SummaryDto currentSummary = repository.findLatestSummaryIn60Sec(timeInterval);
        if (currentSummary.getTimestamp() >= dto.getTimestamp()) {
            reCalculateSummary(currentSummary, dto);
            repository.updateCurrentSummary(currentSummary);
        } else {
            SummaryDto summary = createSummary(currentSummary, dto);
            repository.createSummary(summary);
        }
    }

    public SummaryDto getStatistics() {
        final long timeRange = System.currentTimeMillis() - Constants.SIXTY_SEC;

        final SummaryDto latest = repository.findLatestSummaryIn60Sec(timeRange);
        final SummaryDto oldest = repository.findOldestSummaryIn60Sec(timeRange);

        return calculateResult(latest, oldest);
    }

    private SummaryDto calculateResult(final SummaryDto latest, final SummaryDto oldest) {
        if (latest.getTimestamp() == oldest.getTimestamp()) {
            return latest;
        }
        final SummaryDto result = new SummaryDto();
        result.setSum(latest.getSum() - oldest.getSum());
        result.setCount(latest.getCount() - oldest.getCount());
        result.setAverage(result.getSum() / result.getCount());
        result.setMax(latest.getMax());
        result.setMin(latest.getMin());
        return result;
    }

    private SummaryDto createSummary(final SummaryDto currentSummary, final TransactionDto transactionDto) {
        currentSummary.setTimestamp(transactionDto.getTimestamp());
        reCalculateSummary(currentSummary, transactionDto);
        return currentSummary;
    }

    private void reCalculateSummary(final SummaryDto currentSummary, final TransactionDto dto) {
        currentSummary.setCount(currentSummary.getCount() + 1);
        currentSummary.setSum(currentSummary.getSum() + dto.getAmount());
        final double average = currentSummary.getSum() / currentSummary.getCount();
        currentSummary.setAverage(average);

        if (currentSummary.getMax() < dto.getAmount()) {
            currentSummary.setMax(dto.getAmount());
        }

        if (dto.getAmount() < currentSummary.getMin()) {
            currentSummary.setMin(dto.getAmount());
        }
    }
}
