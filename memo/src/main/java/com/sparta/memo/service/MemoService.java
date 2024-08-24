package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemoService {

    private final Map<Long, Memo> memoList = new HashMap<>();

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // Memo Max Id Check
        // Collections.max(memoList.keySet()) : memoList의 key 값을 모두 가져와서, 이 중 가장 큰 값을 꺼냄
        // memoList의 값이 하나도 없으면 1 을 넣기
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);

        // DB 저장
        memoList.put(memo.getId(), memo);

        // Entity -> ReponseDto
        MemoResponseDto responseDto = new MemoResponseDto(memo);
        return responseDto;
    }


}
