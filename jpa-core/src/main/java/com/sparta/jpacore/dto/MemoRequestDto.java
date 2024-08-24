package com.sparta.jpacore.dto;

import com.sparta.jpacore.entity.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
public class MemoRequestDto {
    private String username;
    private String contents;

    public MemoRequestDto(Memo memo) {
        this.username = memo.getUsername();
        this.contents = memo.getContents();
    }

    public MemoRequestDto(String username, String contents) {
        this.username = username;
        this.contents = contents;
    }
}
