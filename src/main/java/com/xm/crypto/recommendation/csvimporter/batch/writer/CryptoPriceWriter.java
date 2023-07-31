package com.xm.crypto.recommendation.csvimporter.batch.writer;

import com.xm.crypto.recommendation.csvimporter.dto.CryptoPriceDTO;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.Crypto;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoPrice;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoFileImportRepository;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoPriceRepository;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CryptoPriceWriter implements ItemWriter<CryptoPriceDTO> {

    private final CryptoRepository cryptoRepository;
    private final CryptoFileImportRepository cryptoFileImportRepository;
    private final CryptoPriceRepository cryptoPriceRepository;

    Map<String, Long> cryptoCache = new ConcurrentHashMap<>();
    private LocalDateTime dateTimeForProcessingMonth = LocalDateTime.now();

    @Autowired
    public CryptoPriceWriter(CryptoRepository cryptoRepository, CryptoFileImportRepository cryptoFileImportRepository,
                             CryptoPriceRepository cryptoPriceRepository) {
        this.cryptoRepository = cryptoRepository;
        this.cryptoFileImportRepository = cryptoFileImportRepository;
        this.cryptoPriceRepository = cryptoPriceRepository;
    }

    @Override
    @Transactional
    public void write(List<? extends CryptoPriceDTO> cryptoPriceDtos) throws Exception {

        List<CryptoPrice> cryptoPrices = new ArrayList<>();

        for (CryptoPriceDTO priceDTO : cryptoPriceDtos) {

            LocalDateTime priceDate = priceDTO.getPriceTimestamp();

            if (priceDate.isBefore(dateTimeForProcessingMonth)) {
                dateTimeForProcessingMonth = priceDate;
            }
            //Save Crypto name
            if (!cryptoCache.containsKey(priceDTO.getSymbol())) {
                Crypto crypto = cryptoRepository.findBySymbol(priceDTO.getSymbol());
                if (crypto == null) {
                    Crypto cryptoBuilder = Crypto.builder()
                            .symbol(priceDTO.getSymbol())
                            .supported(true)
                            .timeFrameInMonth(1)
                            .build();
                    crypto = cryptoRepository.save(cryptoBuilder);
                }
                cryptoCache.put(priceDTO.getSymbol(), crypto.getId());
            }

            //Save Crypto price details
            CryptoPrice cryptoPrice = CryptoPrice.builder()
                    .price(priceDTO.getPrice())
                    .priceTimestamp(priceDTO.getPriceTimestamp())
                    .crypto(Crypto.builder().symbol(priceDTO.getSymbol()).id(cryptoCache.get(priceDTO.getSymbol())).build())
                    .build();
            cryptoPrices.add(cryptoPrice);
        }
        cryptoPriceRepository.saveAll(cryptoPrices);
    }

    public LocalDateTime getDateTimeForProcessingMonth() {
        return dateTimeForProcessingMonth;
    }
}
