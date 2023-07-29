package com.xm.crypto.recommendation.csv.writer;

import com.xm.crypto.recommendation.csv.dto.CryptoFileImportDto;
import com.xm.crypto.recommendation.persistence.entities.Crypto;
import com.xm.crypto.recommendation.persistence.entities.CryptoFileImport;
import com.xm.crypto.recommendation.persistence.entities.CryptoPrice;
import com.xm.crypto.recommendation.persistence.repositories.CryptoFileImportRepository;
import com.xm.crypto.recommendation.persistence.repositories.CryptoPriceRepository;
import com.xm.crypto.recommendation.persistence.repositories.CryptoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CryptoPriceWriter implements ItemWriter<CryptoFileImportDto> {

    private final CryptoRepository cryptoRepository;
    private final CryptoFileImportRepository cryptoFileImportRepository;
    private final CryptoPriceRepository cryptoPriceRepository;

    @Autowired
    public CryptoPriceWriter(CryptoRepository cryptoRepository, CryptoFileImportRepository cryptoFileImportRepository,
                             CryptoPriceRepository cryptoPriceRepository) {
        this.cryptoRepository = cryptoRepository;
        this.cryptoFileImportRepository = cryptoFileImportRepository;
        this.cryptoPriceRepository = cryptoPriceRepository;
    }

    @Override
    @Transactional
    public void write(List<? extends CryptoFileImportDto> cryptoFileImportDtos) throws Exception {
        String symbol = null;
        List<CryptoPrice> cryptoPrices = new ArrayList<>();
        //TODO - In memory cache

        for (CryptoFileImportDto cryptoFileImportDto : cryptoFileImportDtos) {


            //TODO Save Crypto
            Crypto crypto = cryptoRepository.findBySymbol(cryptoFileImportDto.symbol());
            if (crypto == null) {
                Crypto cryptoBuilder = Crypto.builder()
                        .symbol(cryptoFileImportDto.symbol())
                        .supported(true)
                        .build();
                crypto = cryptoRepository.save(cryptoBuilder);
            }


            //TODO Duplicate File will not be processed
            if (crypto != null) {
                CryptoFileImport cryptoFileImport = cryptoFileImportRepository.findByCryptoIdAndFileNameAndLastModifiedDate
                        (crypto.getId(), cryptoFileImportDto.filename(), cryptoFileImportDto.lastModified());
                if (cryptoFileImport == null) {
                    CryptoFileImport cryptoFileImportBuilder = CryptoFileImport
                            .builder()
                            .timeFrame(30) // TODO Move to config
                            .fileName(cryptoFileImportDto.filename())
                            .lastModifiedDate(cryptoFileImportDto.lastModified())
                            .crypto(crypto)
                            .build();
                    cryptoFileImportRepository.save(cryptoFileImportBuilder);
                    symbol = crypto.getSymbol();
                }
            }

            if (!StringUtils.isEmpty(symbol) && symbol.equalsIgnoreCase(crypto.getSymbol())) {
                //Save CryptoPrice
                CryptoPrice cryptoPrice = CryptoPrice.builder()
                        .price(cryptoFileImportDto.price())
                        .priceTimestamp(ZonedDateTime.ofInstant(Instant.ofEpochMilli(cryptoFileImportDto.timestamp()), ZoneId.systemDefault()))
                        .crypto(crypto)
                        .build();
                cryptoPrices.add(cryptoPrice);
            }
        }
        cryptoPriceRepository.saveAll(cryptoPrices);
    }
}
