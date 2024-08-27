package com.sparta.jpaadvance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // N 대 M
    @ManyToMany(mappedBy = "userList")
    private List<Food> foodList = new ArrayList<>();

    // 양방향 설정
    public void addFoodList(Food food) {
        this.foodList.add(food);
        food.getUserList().add(this); // 외래 키(연관 관계) 설정
    }

    // N 대 1
//    @OneToMany(mappedBy = "user")
//    private List<Food> foodList = new ArrayList<>(); // db에는 적용되지 않고, entity를 참조하기 위해 존재하는 것
//
//    public void addFoodList(Food food) {
//        this.foodList.add(food);
//        food.setUser(this); // 외래키(연관 관계) 설정
//    }


    // 1 대 1
//    @OneToOne(mappedBy = "user")
//    private Food food;
//
//    public void addFood(Food food) {
//        this.food = food;
//        food.setUser(this);
//    }


}
