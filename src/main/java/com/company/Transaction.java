package com.company;

import java.sql.Timestamp;

public class Transaction {

    private int transactionId;
    private int accountNo;
    private String transactionType;
    private int amount;
    private Timestamp transactionTime;

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTransactionTime(Timestamp transactionTime) {
        this.transactionTime = transactionTime;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getAmount() {
        return amount;
    }

    public Timestamp getTransactionTime() {
        return transactionTime;
    }
}