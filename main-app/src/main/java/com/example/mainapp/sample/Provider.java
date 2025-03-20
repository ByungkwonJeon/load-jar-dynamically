package com.example.mainapp.sample;

public enum Provider {
    JPMC_ACH(1, "JPMC_ACH"),
    EWS(2, "EWS"),
    GIACT(3, "GIACT"),
    JPMC_LINK_CONFIRM(4, "JPMC_LINK_CONFIRM"),
    EWS_VA(5, "EWS_VA"),
    MICRODEPOSITS(6, "Microdeposits"),
    SUREPAY_UKCOP(7, "SUREPAY_UKCOP"),
    PATTERN_MATCH(8, "PATTERN_MATCH"),
    JPMC_BENE(9, "JPMC_BENE"),
    JPMC_BRIE_ACCOUNTS(10, "JPMC_BRIE_ACCOUNTS"),
    SUREPAY_IBAN(11, "SUREPAY_IBAN"),
    JPMC_CTA(12, "JPMC_CTA"),
    SUREPAY_VOP(13, "SUREPAY_VOP"),
    NCPI(14, "NCPI"),
    JPMC_AIS(15, "JPMC_AIS"),
    UNKNOWN(-1, "Unknown");

    private final int code;
    private final String description;

    Provider(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Provider fromCode(int code) {
        for (Provider provider : values()) {
            if (provider.code == code) {
                return provider;
            }
        }
        return UNKNOWN;
    }

    public String getDescription() {
        return description;
    }
}