package com.xm.crypto.recommendation.csvimporter.configuration;

import com.xm.crypto.recommendation.csvimporter.dto.CryptoFileImportDto;
import com.xm.crypto.recommendation.csvimporter.batch.reader.MultiCryptoPriceReader;
import com.xm.crypto.recommendation.csvimporter.batch.writer.CryptoPriceWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class CryptoPriceJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MultiCryptoPriceReader multiCryptoPriceReader;
    private final CryptoPriceWriter cryptoPriceWriter;

    @Autowired
    public CryptoPriceJobConfig(JobBuilderFactory jobBuilderFactory,
                                StepBuilderFactory stepBuilderFactory,
                                MultiCryptoPriceReader multiCryptoPriceReader,
                                CryptoPriceWriter cryptoPriceWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.multiCryptoPriceReader = multiCryptoPriceReader;
        this.cryptoPriceWriter = cryptoPriceWriter;
    }

    @Bean
    public Step importCryptoPriceStep() {
        return stepBuilderFactory.get("importCryptoPriceStep")
                .<CryptoFileImportDto, CryptoFileImportDto>chunk(10)
                .reader(multiCryptoPriceReader)
                //.processor(processor())
                .writer(cryptoPriceWriter)
                .build();
    }
    @Bean
    public Job importCryptoPriceJob(Step importCryptoPriceStep) {
        return jobBuilderFactory.get("importCryptoPriceJob")
                .incrementer(new RunIdIncrementer())
                .flow(importCryptoPriceStep)
                .end()
                .build();
    }
}
