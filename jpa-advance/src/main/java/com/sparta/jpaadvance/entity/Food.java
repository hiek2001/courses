package com.sparta.jpaadvance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "food")
public class Food { // 외래키의 주인
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;

    // N 대 M
    @ManyToMany
    @JoinTable(name = "orders", // 중간 테이블 생성
            joinColumns = @JoinColumn(name = "food_id"), // 현재 위치인 Food Entity 에서 중간 테이블로 조인할 컬럼 설정
            inverseJoinColumns = @JoinColumn(name = "user_id")) // 반대 위치인 User Entity 에서 중간 테이블로 조인할 컬럼 설정
    private List<User> userList = new ArrayList<>();

    // 양방향 설정
    public void addUserList(User user) {
        this.userList.add(user); // 외래 키(연관 관계) 설정
        user.getFoodList().add(this);
    }

    // N 대 1
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
    // 1 대 1
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;
}