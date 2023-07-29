package com.xm.crypto.recommendation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class CryptoPriceService {




    @Transactional
    public void readCsvAndUploadData() throws IOException {
        /*List<CSVRecord> csvRecords = CsvReaderUtil.readCsvFile(csvFilePath);

        //TODO moved to outside
        Crypto crypto = Crypto.builder()
                .fileName("")
                .timeFrame(30L)
                .name(CryptoNameReaderUtil.readCryptoName(""))
                .build();
        crypto = cryptoRepository.save(crypto);

        for (CSVRecord record : csvRecords) {
            CryptoPrice cryptoPrice = CryptoPrice.builder()
                    .price(new BigDecimal(record.get("price")))
                    .timeStamp(Instant.ofEpochMilli(Long.parseLong(record.get("timestamp")))
                            .atZone(ZoneId.systemDefault()))
                    .cryptoId(crypto.getId())
                    .build();
            cryptoPriceRepository.save(cryptoPrice);
        }*/


    }
}
