package com.xm.crypto.recommendation.util;

public class CryptoNameReaderUtil {

    public static String readCryptoName(String fileName) {
        return fileName.substring(0, Math.min(fileName.length(), 3));
    }
}
