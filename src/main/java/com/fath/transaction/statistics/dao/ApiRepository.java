package com.fath.transaction.statistics.dao;

import com.fath.transaction.statistics.model.SummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ApiRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // timestamp is PK
    public SummaryDto findLatestSummaryIn60Sec(final long timeInterval) {
        try {
            final List<SummaryDto> summaryList = jdbcTemplate.query("SELECT * FROM summary WHERE timestamp = (SELECT max(timestamp) FROM summary WHERE timestamp > ?)", new SummaryRowMapper(), timeInterval);
            if (summaryList.size() == 1) {
                return summaryList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SummaryDto.builder()
                .timestamp(0L)
                .min(Double.MAX_VALUE)
                .max(Double.MIN_VALUE)
                .build();
    }

    public void updateCurrentSummary(final SummaryDto currentSummary) {
        try {
            jdbcTemplate.update("UPDATE summary SET sum = ?, average = ?, max=?, min=?, count=? WHERE timestamp=?",
                    currentSummary.getSum(), currentSummary.getAverage(), currentSummary.getMax(), currentSummary.getMin(), currentSummary.getCount(), currentSummary.getTimestamp());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void createSummary(final SummaryDto summary) {
        try {
            jdbcTemplate.update("INSERT INTO summary(timestamp, sum, average, max, min, count) VALUES (?,?,?,?,?,?)",
                    summary.getTimestamp(), summary.getSum(), summary.getAverage(), summary.getMax(), summary.getMin(), summary.getCount());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public SummaryDto findOldestSummaryIn60Sec(final long timeRange) {
        try {
            final List<SummaryDto> summaryList = jdbcTemplate.query("SELECT * FROM summary WHERE timestamp = (SELECT max(timestamp) FROM summary WHERE timestamp < ?)", new SummaryRowMapper(), timeRange);
            if (summaryList.size() == 1) {
                return summaryList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SummaryDto.builder()
                .min(Double.MAX_VALUE)
                .max(Double.MIN_VALUE)
                .build();
    }

    final class SummaryRowMapper implements RowMapper<SummaryDto> {

        @Nullable
        @Override
        public SummaryDto mapRow(ResultSet resultSet, int i) throws SQLException {
            return SummaryDto.builder()
                    .timestamp(resultSet.getLong("timestamp"))
                    .sum(resultSet.getDouble("sum"))
                    .average(resultSet.getDouble("average"))
                    .max(resultSet.getDouble("max"))
                    .min(resultSet.getDouble("min"))
                    .count(resultSet.getInt("count"))
                    .build();
        }
    }
}
