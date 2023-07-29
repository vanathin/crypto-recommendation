package com.xm.crypto.recommendation.csv.reader;


import com.xm.crypto.recommendation.csv.dto.CryptoFileImportDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@StepScope
public class MultiCryptoPriceReader extends MultiResourceItemReader<CryptoFileImportDto> {


    @Value("file:${crypto.price.files.location}/*.csv")
    private Resource[] resources;

    private CryptoPriceReader cryptoPriceReader;

    public MultiCryptoPriceReader(CryptoPriceReader cryptoPriceReader) {
        this.cryptoPriceReader = cryptoPriceReader;
    }

    @PostConstruct
    private void initialize() {
        setDelegate(cryptoPriceReader);
        setResources(resources);
    }
}
