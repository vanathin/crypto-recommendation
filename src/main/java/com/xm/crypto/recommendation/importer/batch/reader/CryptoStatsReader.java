package com.xm.crypto.recommendation.importer.batch.reader;

import com.xm.crypto.recommendation.importer.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.importer.persistence.repository.CryptoPriceRepository;
import com.xm.crypto.recommendation.importer.persistence.repository.CryptoStatsRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CryptoStatsReader implements ItemReader<List<CryptoStatsDTO>>, StepExecutionListener {

    private final CryptoPriceRepository cryptoPriceRepository;
    private StepExecution stepExecution;
    private final CryptoStatsRepository cryptoStatsRepository;


    @Autowired
    public CryptoStatsReader(CryptoPriceRepository cryptoPriceRepository,
                             CryptoStatsRepository cryptoStatsRepository) {
        this.cryptoPriceRepository = cryptoPriceRepository;
        this.cryptoStatsRepository = cryptoStatsRepository;
    }

    @BeforeStep
    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

    @Override
    public List<CryptoStatsDTO> read() {
        if(stepExecution.getJobExecution().getExecutionContext().isEmpty()) {
            return null;
        }
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        String monthStartDateString = executionContext.getString("monthStartDate");

        LocalDateTime monthStartDate = LocalDateTime.parse(monthStartDateString);

        List<Object[]> result = cryptoStatsRepository.findCryptoStats(monthStartDate.toLocalDate(),
                getFirstDate(monthStartDate), getLastEndDate(monthStartDate));
        return result.stream().map(row -> {
            BigInteger bigInteger = (BigInteger) row[0];
            Long cryptoId = bigInteger.longValue();
            BigDecimal minPrice = (BigDecimal) row[1];
            BigDecimal maxPrice = (BigDecimal) row[2];
            LocalDate startDate = LocalDate.parse((String) row[3]);
            BigDecimal oldestPrice = (BigDecimal) row[4];
            BigDecimal newestPrice = (BigDecimal) row[5];
            return new CryptoStatsDTO(cryptoId, startDate, minPrice, maxPrice, oldestPrice, newestPrice);
        }).collect(Collectors.toList());
    }
    private LocalDateTime getLastEndDate(LocalDateTime monthStartDate) {
        return monthStartDate.with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }
    private LocalDateTime getFirstDate(LocalDateTime monthStartDate) {
        return monthStartDate.with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
}
