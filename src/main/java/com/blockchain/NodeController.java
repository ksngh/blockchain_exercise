package com.blockchain;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @GetMapping("/chain")
    public List<Block> chain() {
        return nodeService.getChain();
    }

    @GetMapping("/valid")
    public ResponseEntity<String> valid() {
        return ResponseEntity.ok(nodeService.isValid() ? "VALID" : "INVALID");
    }

    @PostMapping("/tx")
    public ResponseEntity<String> addTx(@RequestBody TxRequest req) {
        if (req.from() == null || req.to() == null || req.amount() <= 0)
            return ResponseEntity.badRequest().body("Invalid tx");
        nodeService.addTransaction(new Transaction(req.from(), req.to(), req.amount(), System.currentTimeMillis()));
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/mine")
    public Block mine() {
        return nodeService.mine();
    }

    public record TxRequest(String from, String to, long amount) {}
}
