package com.blockchain;

public record Transaction(String from, String to, long amount, long timestamp) {
    public String digest() {
        return from + "->" + to + ":" + amount + "@" + timestamp;
    }
}