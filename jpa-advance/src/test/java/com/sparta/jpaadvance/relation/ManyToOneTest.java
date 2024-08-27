package com.sparta.jpaadvance.relation;

import com.sparta.jpaadvance.entity.Food;
import com.sparta.jpaadvance.entity.User;
import com.sparta.jpaadvance.repository.FoodRepository;
import com.sparta.jpaadvance.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@Transactional
@SpringBootTest
public class ManyToOneTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Test
    @Rollback(value = false)
    @DisplayName("N대1 단방향 테스트")
    void test1() {
        User user = new User();
        user.setName("Ranny");

        Food food = new Food();
        food.setName("후라이드 치킨");
        food.setPrice(15000);
        food.setUser(user); // 외래키(연관 관계) 설정

        Food food2 = new Food();
        food2.setName("양념 치킨");
        food2.setPrice(20000);
        food2.setUser(user); // 외래키(연관 관계) 설정

        userRepository.save(user);
        foodRepository.save(food);
        foodRepository.save(food2);
    }
}
