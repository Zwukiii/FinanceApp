package com.financeapp.backend.enums;

public enum TransactionType {
    INCOME("income"),
    EXPENSE("expense"),
    INVESTMENT("investment");

    private final String displayName;


    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
