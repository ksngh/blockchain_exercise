package com.blockchain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Blockchain {
    private final List<Block> chain = new ArrayList<>();
    private final int difficulty;

    public Blockchain(int difficulty) {
        this.difficulty = difficulty;
        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        List<Transaction> genesisTx = List.of(new Transaction("GENESIS", "GENESIS", 0, System.currentTimeMillis()));
        Block genesis = new Block(0, "0", genesisTx);
        genesis.mine(difficulty);
        return genesis;
    }

    public synchronized Block mineBlock(List<Transaction> txs) {
        Block last = getLatestBlock();
        Block newBlock = new Block(last.getIndex() + 1, last.getHash(), txs);
        newBlock.mine(difficulty);
        chain.add(newBlock);
        return newBlock;
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public List<Block> getChain() {
        return new ArrayList<>(chain);
    }

    public boolean isValid() {
        String prefix = "0".repeat(difficulty);
        for (int i = 1; i < chain.size(); i++) {
            Block cur = chain.get(i);
            Block prev = chain.get(i - 1);

            // 해시 재계산 일치 여부
            if (!cur.getHash().equals(cur.calculateHash())) return false;
            // 난이도 조건 충족 여부
            if (!cur.getHash().startsWith(prefix)) return false;
            // 이전 해시 연결성 확인
            if (!cur.getPreviousHash().equals(prev.getHash())) return false;
        }
        return true;
    }

}

