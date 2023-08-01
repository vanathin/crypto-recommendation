package com.xm.crypto.recommendation.importer.batch.reader;


import com.xm.crypto.recommendation.importer.dto.CryptoPriceDTO;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@StepScope
public class MultiCryptoPriceReader extends MultiResourceItemReader<CryptoPriceDTO> {
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
