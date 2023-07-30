package com.xm.crypto.recommendation.csvimporter.batch.writer;

import com.xm.crypto.recommendation.csvimporter.dto.CryptoFileImportDto;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.Crypto;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoFileImport;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoPrice;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoFileImportRepository;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoPriceRepository;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CryptoPriceWriter implements ItemWriter<CryptoFileImportDto> {

    private final CryptoRepository cryptoRepository;
    private final CryptoFileImportRepository cryptoFileImportRepository;
    private final CryptoPriceRepository cryptoPriceRepository;

    Map<String, Long> cryptoCache = new ConcurrentHashMap<>();

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

        List<CryptoPrice> cryptoPrices = new ArrayList<>();

        for (CryptoFileImportDto cryptoFileImportDto : cryptoFileImportDtos) {

            //Save Crypto name
            if (!cryptoCache.containsKey(cryptoFileImportDto.symbol())) {
                Crypto crypto = cryptoRepository.findBySymbol(cryptoFileImportDto.symbol());
                if (crypto == null) {
                    Crypto cryptoBuilder = Crypto.builder()
                            .symbol(cryptoFileImportDto.symbol())
                            .supported(true)
                            .build();
                    crypto = cryptoRepository.save(cryptoBuilder);
                }
                cryptoCache.put(cryptoFileImportDto.symbol(), crypto.getId());
            }

            //Save Crypto file details
            CryptoFileImport cryptoFileImport = cryptoFileImportRepository.findByCryptoSymbolAndFileNameAndLastModifiedDate
                    (cryptoFileImportDto.symbol(), cryptoFileImportDto.filename(), cryptoFileImportDto.lastModified());

            if (cryptoFileImport == null) {
                ///Update existing crypto file details
                CryptoFileImport cryptoFileImportBuilder = cryptoFileImportRepository.
                        findByCryptoSymbol(cryptoFileImportDto.symbol());

                if (cryptoFileImportBuilder == null) {
                    cryptoFileImportBuilder = CryptoFileImport
                            .builder()
                            .timeFrame(30) // TODO Move to config
                            .fileName(cryptoFileImportDto.filename())
                            .lastModifiedDate(cryptoFileImportDto.lastModified())
                            .crypto(Crypto.builder().symbol(cryptoFileImportDto.symbol()).id(cryptoCache.get(cryptoFileImportDto.symbol())).build())
                            .build();
                } else {
                    cryptoPriceRepository.deleteBySymbol(cryptoFileImportDto.symbol()); // Remove old price details
                    cryptoFileImportBuilder.setLastModifiedDate(cryptoFileImportDto.lastModified());
                    cryptoFileImportBuilder.setTimeFrame(30);
                }
                cryptoFileImportRepository.save(cryptoFileImportBuilder);
            }

            //Save Crypto price details
            CryptoPrice cryptoPrice = CryptoPrice.builder()
                    .price(cryptoFileImportDto.price())
                    .priceTimestamp(ZonedDateTime.ofInstant(Instant.ofEpochMilli(cryptoFileImportDto.timestamp()), ZoneId.systemDefault()))
                    .crypto(Crypto.builder().symbol(cryptoFileImportDto.symbol()).id(cryptoCache.get(cryptoFileImportDto.symbol())).build())
                    .build();
            cryptoPrices.add(cryptoPrice);
        }
        cryptoPriceRepository.saveAll(cryptoPrices);
    }
}
