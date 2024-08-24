package com.sparta.jpacore.service;

import com.sparta.jpacore.dto.MemoRequestDto;
import com.sparta.jpacore.dto.MemoResponseDto;
import com.sparta.jpacore.entity.Memo;
import com.sparta.jpacore.repository.MemoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        Memo memo = new Memo(requestDto);

        Memo saveMemo = memoRepository.save(memo);

        MemoResponseDto responseDto = new MemoResponseDto(saveMemo);
        return responseDto;
    }

    public List<MemoResponseDto> getMemos() {
        return memoRepository.findAllByOrderByModifiedAtDesc().stream().map(MemoResponseDto::new).toList();
    }

    public List<MemoResponseDto> getMemosByKeyword(String keyword) {
        return memoRepository.findAllByContentsContainingOrderByModifiedAtDesc(keyword).stream().map(MemoResponseDto::new).toList();
    }

    @Transactional
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        Memo memo = findMemo(id);

        memo.update(requestDto);
        return id;
    }

    public Long deleteMemo(Long id) {
        Memo memo = findMemo(id);
        memoRepository.delete(memo);
        return id;
    }

    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
        );
    }
}
