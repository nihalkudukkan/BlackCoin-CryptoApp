package com.example.blackcoinservice.controller;

import com.example.blackcoinservice.model.BlackBlock;
import com.example.blackcoinservice.model.response.BlockResponse;
import com.example.blackcoinservice.service.BlockService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

@RestController
@RequestMapping("/fe")
@AllArgsConstructor
public class FEController {

    private BlockService blockService;

    @GetMapping("/allblock")
    public List<BlockResponse> showBlock() {
        List<BlackBlock> blocks = blockService.getBlackBlocks();
        Stack<BlockResponse> response = new Stack<>();

        blocks.forEach(block->{
            BlockResponse c = new BlockResponse(
                    block.getBlockId(),
                    block.getTimestamp(),
                    block.getPreviousHash(),
                    block.getHash(),
                    block.getNonce(),
                    block.getTransactions().size()
            );
            response.push(c);
        });
        Collections.reverse(response);
        return response;
    }

}
