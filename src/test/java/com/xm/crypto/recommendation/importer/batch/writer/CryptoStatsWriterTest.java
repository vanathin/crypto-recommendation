package com.xm.crypto.recommendation.importer.batch.writer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.xm.crypto.recommendation.importer.persistence.repository.CryptoStatsRepository;
import com.xm.crypto.recommendation.importer.dto.CryptoStatsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

public class CryptoStatsWriterTest {

    @Mock
    private CryptoStatsRepository cryptoStatsRepository;

    @InjectMocks
    private CryptoStatsWriter cryptoStatsWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void write_withMultipleCryptoStatsDTO_savesCryptoStats() throws Exception {
        // Given
        CryptoStatsDTO cryptoStatsDTO1 = new CryptoStatsDTO();
        cryptoStatsDTO1.setCryptoId(1L);

        CryptoStatsDTO cryptoStatsDTO2 = new CryptoStatsDTO();
        cryptoStatsDTO2.setCryptoId(2L);

        // When
        cryptoStatsWriter.write(Arrays.asList(cryptoStatsDTO1, cryptoStatsDTO2));

        // Then
        verify(cryptoStatsRepository, times(1)).saveAll(anyList());
    }

    @Test
    void write_withNullCryptoStatsDTO_doesNotCallSaveAll() throws Exception {
        // Given
        cryptoStatsWriter.write(Collections.singletonList(null));

        // Then
        verify(cryptoStatsRepository, times(0)).saveAll(anyList());
    }

    @Test
    void write_whenSaveAllThrowsException_handlesException() throws Exception {
        // Given
        CryptoStatsDTO cryptoStatsDTO = new CryptoStatsDTO();
        cryptoStatsDTO.setCryptoId(1L);

        doThrow(new RuntimeException()).when(cryptoStatsRepository).saveAll(anyList());

        // When and Then
        assertThrows(Exception.class, () -> {
            cryptoStatsWriter.write(Collections.singletonList(cryptoStatsDTO));
        });
    }
}

