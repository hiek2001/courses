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
public class OneToOneTest {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    UserRepository userRepository;

//    @Test
//    void test() {
//        // 이 test() 메소드 자체에서 set으로 변경이 있어났는데, 해당 로직은 영속성 컨테스트가 영속 상태가 아니기 때문에
//        // 변경 감지가 일어나지 않음
//        // 이런 경우에는 @Transactional를 넣어줘야 함
    //      update 진행시, 반드시 Transaction 환경을 만들어야 함.!!
//        Food food = foodRepository.findById(1L).orElse(null);
//        food.setName("피자");
//    }

//    @Test
//    void test(Food food) {
//        // 이 test() 는 save() 내부 로직에 이미 @Transactional이 있어서
//        // 해당 메소드에 @Transactional이 없어도 잘 작동함
//        foodRepository.save(food);
//    }

//    @Test
//    void test() {
//        // food의 name이 반드시 값이 들어가야 할 경우
//        Food food = new Food();
//        food.setName("양고기");
//        foodRepository.save(food);
//
//        Food food2 = new Food();
//        food.setPrice(101010);
//        foodRepository.save(food);
//
//        // 위 test() 에서 @Transactional이 없는 경우 : 오류가 있는 것만 실행 안됨. 오류 없는 건 됨
//        // @Transactional이 있는 경우 : 오류가 한개라도 있으면 모든 내용이 rollback.
//        // 필요한 상황에 따라 사용할 것!
//    }

    // repository.save() 내부 로직에서 @Transactional이 이미 있어서
    // OneToOneTest() 메소드에 해당 어노테이션이 없더라도 잘 작동함
    @Test
    @Rollback(value = false) // 테스트에서는 @Transactional에 의해 자동 rollback 됨으로 false 설정
    @DisplayName("1대1 단방향 테스트")
    void test1() {
        User user = new User();
        user.setName("Robbie");

        // 외래 키의 주인인 Food Entity user 필드에 user 객체를 추가합니다.
        Food food = new Food();
        food.setName("후라이드 치킨");
        food.setPrice(15000);
        food.setUser(user); // 외래 키(연관 관계) 설명

        userRepository.save(user);
        foodRepository.save(food);
    }

    @Test
    @Rollback(value = false)
    @DisplayName("1대1 양방향 테스트 : 외래 키 저장 실패")
    void test2() {
        Food food = new Food();
        food.setName("고구마 피자");
        food.setPrice(30000);

        // 외래 키의 주인이 아닌 User에서 Food를 저장
        User user = new User();
        user.setName("Roobie");
        user.setFood(food);

        userRepository.save(user);
        foodRepository.save(food);

        // 확인해보면 user_id값이 들어가 있지 않음을 확인할 수 있음
    }

    @Test
    @Rollback(value = false)
    @DisplayName("1대1 양방향 테스트 : 외래 키 저장 실패 -> 성공")
    void test3() {
        Food food = new Food();
        food.setName("고구마 피자");
        food.setPrice(30000);

        // 외래 키의 주인이 아닌 User에서 Food를 저장하기 위해 addFood() 메소드를 추가
        User user = new User();
        user.setName("Roobie");
        user.addFood(food); // 외래키(연관관계) 설정 food.setUser(this); 추가

        userRepository.save(user);
        foodRepository.save(food);
    }

    @Test
    @Rollback(value = false)
    @DisplayName("1대1 양방향 테스트")
    void test4() {
        User user = new User();
        user.setName("Robbert");

        Food food = new Food();
        food.setName("고구마 피자");
        food.setPrice(30000);
        food.setUser(user); // 외래 키(연관 관계) 설정

        userRepository.save(user);
        foodRepository.save(food);

    }

    @Test
    @DisplayName("1대1 조회 : Food 기준 user 정보 조회")
    void test5() {
        Food food = foodRepository.findById(1L).orElseThrow(NullPointerException::new);
        // 음식 정보 조회
        System.out.println("food.getName() = "+food.getName());

        // 음식을 주문한 고객 정보 조회
        System.out.println("food.getUser().getName() = "+food.getUser().getName());
    }

    @Test
    @DisplayName("1대 1 조회 : User 기준 food 정보 조회")
    void test6() {
        User user = userRepository.findById(1L).orElseThrow(NullPointerException::new);
        // 고객 정보 조회
        System.out.println("user.getName()= "+user.getName());

        // 해당 고객이 주문한 음식 정보 조회
        Food food = user.getFood();
        System.out.println("food.getName() = "+food.getName());
        System.out.println("food.getPrice() = "+food.getPrice());
    }

}
