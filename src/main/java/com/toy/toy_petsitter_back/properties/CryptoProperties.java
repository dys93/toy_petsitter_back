package com.toy.toy_petsitter_back.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "secret")
public class CryptoProperties {
    private static String aeskey256;
    private static String ivspec;

    public static String getAeskey256() {
        return aeskey256;
    }

    public void setAeskey256(String aeskey256) {
        this.aeskey256 = aeskey256;
    }

    public static String getIvspec() {
        return ivspec;
    }

    public void setIvspec(String ivspec) {
        this.ivspec = ivspec;
    }

}
