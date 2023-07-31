package com.xm.crypto.recommendation.csvimporter.listener;

import com.xm.crypto.recommendation.csvimporter.batch.writer.CryptoPriceWriter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CryptoPriceStepListener implements StepExecutionListener {

    private final CryptoPriceWriter cryptoPriceWriter;

    public CryptoPriceStepListener(CryptoPriceWriter cryptoPriceWriter) {
        this.cryptoPriceWriter = cryptoPriceWriter;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LocalDateTime earliestDate = cryptoPriceWriter.getDateTimeForProcessingMonth();
        if(earliestDate != null) {
            stepExecution.getJobExecution().getExecutionContext().putString("monthStartDate", earliestDate.withDayOfMonth(1).toString());
        }
        return stepExecution.getExitStatus();
    }
}
