package com.xm.crypto.recommendation.csvimporter.service.writer;

import com.xm.crypto.recommendation.csvimporter.dto.CryptoFileImportDto;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.Crypto;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoFileImport;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoPrice;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoFileImportRepository;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoPriceRepository;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoRepository;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CryptoPriceWriter implements ItemWriter<CryptoFileImportDto> {

    private final CryptoRepository cryptoRepository;
    private final CryptoFileImportRepository cryptoFileImportRepository;
    private final CryptoPriceRepository cryptoPriceRepository;

    Map<String, Long> cryptoMap = new ConcurrentHashMap<>();

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

            //Save Crypto
            if (!cryptoMap.containsKey(cryptoFileImportDto.symbol())) {
                Crypto crypto = cryptoRepository.findBySymbol(cryptoFileImportDto.symbol());
                if (crypto == null) {
                    Crypto cryptoBuilder = Crypto.builder()
                            .symbol(cryptoFileImportDto.symbol())
                            .supported(true)
                            .build();
                    crypto = cryptoRepository.save(cryptoBuilder);
                    cryptoMap.put(cryptoFileImportDto.symbol(), crypto.getId());
                }
            }

            //Save CryptoFile
            if (cryptoMap.containsKey(cryptoFileImportDto.symbol())) {
                CryptoFileImport cryptoFileImport = cryptoFileImportRepository.findByCryptoIdAndFileNameAndLastModifiedDate
                        (cryptoMap.get(cryptoFileImportDto.symbol()), cryptoFileImportDto.filename(), cryptoFileImportDto.lastModified());
                if (cryptoFileImport == null) {
                    CryptoFileImport cryptoFileImportBuilder = CryptoFileImport
                            .builder()
                            .timeFrame(30) // TODO Move to config
                            .fileName(cryptoFileImportDto.filename())
                            .lastModifiedDate(cryptoFileImportDto.lastModified())
                            .crypto(Crypto.builder().symbol(cryptoFileImportDto.symbol()).id(cryptoMap.get(cryptoFileImportDto.symbol())).build())
                            .build();
                    cryptoFileImportRepository.save(cryptoFileImportBuilder);
                }
            }

            //Save Crypto price
            if (cryptoMap.containsKey(cryptoFileImportDto.symbol())) {
                //Save CryptoPrice
                CryptoPrice cryptoPrice = CryptoPrice.builder()
                        .price(cryptoFileImportDto.price())
                        .priceTimestamp(ZonedDateTime.ofInstant(Instant.ofEpochMilli(cryptoFileImportDto.timestamp()), ZoneId.systemDefault()))
                        .crypto(Crypto.builder().symbol(cryptoFileImportDto.symbol()).id(cryptoMap.get(cryptoFileImportDto.symbol())).build())
                        .build();
                cryptoPrices.add(cryptoPrice);
            }
        }
        cryptoPriceRepository.saveAll(cryptoPrices);
    }
}
