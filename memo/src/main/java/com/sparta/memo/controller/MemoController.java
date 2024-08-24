package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.service.MemoService;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        MemoService memoService = new MemoService();
        return memoService.createMemo(requestDto);
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // Map to List
        // memoList로 가져온 값들을 stream()을 사용하여 for문처럼 돌려서 하나씩 객체가 튀어나옴
        // MemoResponseDto::new -> dto에 선언해준 생성자를 통해 객체를 만들고, 그걸 모아서 list로 만들어줌
        List<MemoResponseDto> memoResponseList = memoList.values().stream().map(MemoResponseDto::new).toList();

        return memoResponseList;
    }

    @PutMapping("/memos/{id}")
    public Long updateMemos(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        // 해당 메모다 DB에 존재하는지 확인
        if(memoList.containsKey(id)) { // 해당하는 key가 있는지 확인
            // 해당 메모를 가져오기
            Memo memo = memoList.get(id);

            // 가져온 메모를 수정
            memo.update(requestDto);
            return memo.getId();
        } else {
            throw new IllegalArgumentException("선택한 메모가 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemos(@PathVariable Long id) {
        if(memoList.containsKey(id)) {
            memoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모가 존재하지 않습니다.");
        }
    }
}
