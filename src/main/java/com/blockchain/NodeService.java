package com.blockchain;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class NodeService {
    private final Blockchain blockchain = new Blockchain(4); // 난이도 4 (테스트용)
    private final Queue<Transaction> mempool = new ConcurrentLinkedQueue<>();

    public void addTransaction(Transaction tx) {
        mempool.offer(tx);
    }

    public Block mine() {
        List<Transaction> batch = drainMempool(100); // 단순히 최대 100개 모아서 채굴
        if (batch.isEmpty()) {
            // 트랜잭션이 없어도 빈 블록(코인베이스 등)을 허용하려면 여기에 생성 로직 추가
            batch = List.of(new Transaction("SYSTEM", "miner", 0, System.currentTimeMillis()));
        }
        return blockchain.mineBlock(batch);
    }

    private List<Transaction> drainMempool(int max) {
        List<Transaction> picked = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            Transaction tx = mempool.poll();
            if (tx == null) break;
            picked.add(tx);
        }
        return picked;
    }

    public List<Block> getChain() {
        return blockchain.getChain();
    }

    public boolean isValid() {
        return blockchain.isValid();
    }

    public int difficulty() {
        return blockchain.getDifficulty();
    }
}

