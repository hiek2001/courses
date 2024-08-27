package com.sparta.jpaadvance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    // N 대 1
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // 1 대 1
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;
}