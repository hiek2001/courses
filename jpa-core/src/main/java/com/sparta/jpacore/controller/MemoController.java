package com.sparta.jpacore.controller;

import com.sparta.jpacore.dto.MemoRequestDto;
import com.sparta.jpacore.dto.MemoResponseDto;
import com.sparta.jpacore.service.MemoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MemoController {


    private final MemoService memoService;

    @Autowired
    private MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        return memoService.createMemo(requestDto);
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        return memoService.getMemos();

    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable("id") Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.updateMemo(id, requestDto);
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        return memoService.deleteMemo(id);
    }

    @GetMapping("/memos/contents")
    public List<MemoResponseDto> getMemosByKeyword(@RequestParam(value="keyword") String keyword) {
        return memoService.getMemosByKeyword(keyword);
    }
}
