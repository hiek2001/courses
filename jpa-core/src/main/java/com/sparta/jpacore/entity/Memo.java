package com.sparta.jpacore.entity;

import com.sparta.jpacore.dto.MemoRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Table(name = "memo") // 매핑할 테이블의 이름을 지정
public class Memo extends Timestamped{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // nullable: null 허용 여부
        // unique: 중복 허용 여부 (false 일때 중복 허용)
        @Column(name = "username")
        private String username;

        // length: 컬럼 길이 지정
        @Column(name = "contents")
        private String contents;


        public Memo(MemoRequestDto memoRequestDto) {
                this.username = memoRequestDto.getUsername();
                this.contents = memoRequestDto.getContents();
        }

        public void update(MemoRequestDto requestDto) {
                this.username = requestDto.getUsername();
                this.contents = requestDto.getContents();
        }


}
