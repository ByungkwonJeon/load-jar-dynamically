package com.example.mainapp.sample;

public enum TransactionStatus {
    OPEN_VALID(10, "Open Valid", TransactionType.POSITIVE),
    CLOSED_INVALID(20, "Closed Invalid", TransactionType.NEGATIVE),
    NOTIFICATION_OF_CHANGE(21, "Notification Of Change Received", TransactionType.NEGATIVE),
    NO_INFORMATION_FOUND(30, "No Information Found", TransactionType.NEUTRAL),
    PAYMENT_INSTRUCTION_ERROR(31, "Payment Instruction Error", TransactionType.NEUTRAL),
    VALID_PATTERN(32, "Valid Pattern", TransactionType.POSITIVE),
    INVALID_PATTERN(33, "Invalid Pattern", TransactionType.NEGATIVE),
    REQUEST_REJECTED(34, "Request Rejected", TransactionType.NEGATIVE),
    ACCOUNT_NOT_SUPPORTED(35, "Account Not Supported", TransactionType.NEGATIVE),
    UNABLE_TO_DETERMINE(36, "Unable To Determine", TransactionType.NEUTRAL),
    DEBIT_RETURN_LIKELY(40, "Debit Return Likely", TransactionType.NEGATIVE),
    DEBIT_UNSUCCESSFUL(41, "Debit Unsuccessful", TransactionType.NEGATIVE),
    PAYMENT_REJECTED(42, "Payment Rejected", TransactionType.NEGATIVE),
    ACCOUNT_MISMATCH(43, "Account Mismatch", TransactionType.NEGATIVE),
    INFORMATION_FOUND(11, "Information Found", TransactionType.POSITIVE),
    OWNERSHIP_MATCH(50, "Ownership Match", TransactionType.POSITIVE),
    OWNERSHIP_PARTIAL_MATCH(51, "Ownership Partial Match", TransactionType.POSITIVE),
    ACCOUNT_TYPE_NO_MATCH(52, "Account Type No Match", TransactionType.POSITIVE),
    OWNERSHIP_NO_MATCH(60, "Ownership No Match", TransactionType.NEGATIVE),
    NO_INFORMATION_FOUND_2(70, "No Information Found", TransactionType.NEUTRAL),
    ACKNOWLEDGED(87, "Acknowledged", TransactionType.NEUTRAL),
    UPDATED_RESPONSE(88, "Updated Response", TransactionType.NEGATIVE),
    INITIATED(89, "Initiated", TransactionType.NEUTRAL),
    ACCESS_CONFIRMED(80, "Access Confirmed", TransactionType.NEUTRAL),
    ACCESS_NOT_CONFIRMED_AMOUNT(81, "Access Not Confirmed: Incorrect Deposit Amounts", TransactionType.NEUTRAL),
    ACCESS_NOT_CONFIRMED_ATTEMPTS(82, "Access Not Confirmed: Exceeded Number of Attempts", TransactionType.NEUTRAL),
    ACCESS_NOT_CONFIRMED_TOKEN(83, "Access Not Confirmed: Token Expired", TransactionType.NEUTRAL),
    UNKNOWN(-1, "Unknown", TransactionType.UNKNOWN);

    private final int code;
    private final String description;
    private final TransactionType type;

    TransactionStatus(int code, String description, TransactionType type) {
        this.code = code;
        this.description = description;
        this.type = type;
    }

    public static TransactionStatus fromCode(int code) {
        for (TransactionStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return UNKNOWN;
    }

    public String getDescription() {
        return description;
    }

    public TransactionType getType() {
        return type;
    }
}