package com.xm.crypto.recommendation.csvimporter.configuration;

import com.xm.crypto.recommendation.csvimporter.batch.reader.CryptoStatsReader;
import com.xm.crypto.recommendation.csvimporter.batch.reader.MultiCryptoPriceReader;
import com.xm.crypto.recommendation.csvimporter.batch.writer.CryptoPriceWriter;
import com.xm.crypto.recommendation.csvimporter.batch.writer.CryptoStatsWriter;
import com.xm.crypto.recommendation.csvimporter.dto.CryptoPriceDTO;
import com.xm.crypto.recommendation.csvimporter.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.csvimporter.listener.CryptoPriceStepListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableBatchProcessing
public class CryptoPriceJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MultiCryptoPriceReader multiCryptoPriceReader;
    private final CryptoPriceWriter cryptoPriceWriter;

    private final CryptoStatsReader cryptoStatsReader;
    private final CryptoStatsWriter cryptoStatsWriter;

    private final CryptoPriceStepListener cryptoPriceStepListener;
    @Autowired
    public CryptoPriceJobConfig(JobBuilderFactory jobBuilderFactory,
                                StepBuilderFactory stepBuilderFactory,
                                MultiCryptoPriceReader multiCryptoPriceReader,
                                CryptoPriceWriter cryptoPriceWriter, CryptoStatsReader cryptoStatsReader,
                                CryptoStatsWriter cryptoStatsWriter, CryptoPriceStepListener cryptoPriceStepListener) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.multiCryptoPriceReader = multiCryptoPriceReader;
        this.cryptoPriceWriter = cryptoPriceWriter;
        this.cryptoStatsReader = cryptoStatsReader;
        this.cryptoStatsWriter = cryptoStatsWriter;
        this.cryptoPriceStepListener = cryptoPriceStepListener;
    }

    @Bean
    public Step cryptoPriceStep() {
        return stepBuilderFactory.get("cryptoPriceStep")
                .<CryptoPriceDTO, CryptoPriceDTO>chunk(10)
                .reader(multiCryptoPriceReader)
                .writer(cryptoPriceWriter)
                .listener(cryptoPriceStepListener)
                .build();
    }
   /* @Bean
    public Step cryptoStatsStep() {
        return stepBuilderFactory.get("cryptoStatsStep")
                .<List<CryptoStatsDTO>, List<CryptoStatsDTO>>chunk(1)
                .reader(cryptoStatsReader)
                .writer(cryptoStatsWriter)
                .build();
    }*/

    @Bean
    public Step cryptoStatsStep() {
        return stepBuilderFactory.get("cryptoStatsStep")
                .tasklet(cryptoStatsTasklet())
                .listener(cryptoStatsReader)
                .build();
    }

    @Bean
    public Tasklet cryptoStatsTasklet() {
        return (contribution, chunkContext) -> {
            List<CryptoStatsDTO> stats = cryptoStatsReader.read();
            if(stats != null) {
                cryptoStatsWriter.write(stats);
            }
            return RepeatStatus.FINISHED;
        };
    }
    @Bean
    public Job importCryptoPriceJob() {
        return jobBuilderFactory.get("importCryptoPriceJob")
                .incrementer(new RunIdIncrementer())
                .start(cryptoPriceStep())
                .next(cryptoStatsStep())
                .build();
    }
}
