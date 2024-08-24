package com.sparta.jpacore.repository;

import com.sparta.jpacore.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MemoRepository extends JpaRepository<Memo, Long> {

    // SimpleJpaRepository에서는 없지만 선언함으로써 사용 가능
    List<Memo> findAllByOrderByModifiedAtDesc();

    // username에 unique 옵션이 있을 경우에 사용 가능
    List<Memo> findAllByUsername(String username);

    List<Memo> findAllByContentsContainingOrderByModifiedAtDesc(String keyword);

}
