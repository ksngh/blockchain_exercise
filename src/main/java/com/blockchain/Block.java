package com.blockchain;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Block {

    private final int index;
    private final long timestamp;
    private final String previousHash;
    private final List<Transaction> transactions;
    private int nonce;
    private String hash;

    public Block(int index, String previousHash, List<Transaction> transactions) {
        this.index = index;
        this.previousHash = previousHash;
        this.transactions = new ArrayList<>(transactions);
        this.timestamp = Instant.now().toEpochMilli();
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        StringBuilder txConcat = new StringBuilder();
        for (Transaction tx : transactions) txConcat.append(tx.digest());
        String data = index + "|" + timestamp + "|" + previousHash + "|" + txConcat + "|" + nonce;
        return CryptoUtil.sha256(data);
    }

    public void mine(int difficulty) {
        String targetPrefix = "0".repeat(difficulty);
        while (!hash.startsWith(targetPrefix)) {
            nonce++;
            hash = calculateHash();
        }
    }

}

