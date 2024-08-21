package com.sparta.springresttemplateclient.service;

import com.sparta.springresttemplateclient.dto.ItemDto;
import com.sparta.springresttemplateclient.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RestTemplateService {

    private final RestTemplate restTemplate;

    public RestTemplateService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    public ItemDto getCallObject(String query) {
        // 요청 URL 만들기 (Client -> Server)
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7070")
                .path("/api/server/get-call-obj")
                .queryParam("query", query) // 검색어
                .encode()
                .build()
                .toUri();
        log.info("uri = "+uri);

        // getForEntity : Get 방식으로 해당 uri를 서버로 요청보냄. 서버에서 받을 클래스 타입을 ItemDto로 받는다는 것. (역직렬화)
        ResponseEntity<ItemDto> responseEntity = restTemplate.getForEntity(uri, ItemDto.class);

        log.info("statusCode = "+responseEntity.getStatusCode());

        return responseEntity.getBody();
    }

    public List<ItemDto> getCallList() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7070")
                .path("/api/server/get-call-list")
                .encode()
                .build()
                .toUri();
        log.info("uri = "+uri);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        log.info("statusCode = "+responseEntity.getStatusCode());
        log.info("Body = "+responseEntity.getBody());

        // String으로 한 줄씩 넘어오는걸 아래 메소드를 사용하여 묶어주기
        return fromJSONtoItems(responseEntity.getBody());

    }


    public ItemDto postCall(String query) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7070")
                .path("/api/server/post-call/{query}")
                .encode()
                .build()
                .expand(query) // @PathVariable
                .toUri();
        log.info("uri = "+uri);

        User user = new User("Ranny", "1234");

        // postForEntity : (1)POST 방식으로 uri를 만들어서 보냄. (2)HTTP Body에 넣어줄 데이터 (@RequestBody). (3)전달 받은 데이터와 mapping할 Class
        ResponseEntity<ItemDto> responseEntity = restTemplate.postForEntity(uri, user, ItemDto.class);

        log.info("statusCode = "+responseEntity.getStatusCode());

        return responseEntity.getBody();
    }

    public List<ItemDto> exchangeCall(String token) {
        return null;
    }

    // 중첩 JSON 형태을 String으로 받아옴 -> 값을 꺼내서 Items 형태로 변환
//    {
//        "items":[
//        {"title":"Mac","price":3888000},
//        {"title":"iPad","price":1230000},
//        {"title":"iPhone","price":1550000},
//        {"title":"Watch","price":450000},
//        {"title":"AirPods","price":350000}
//	        ]
//    }
    private List<ItemDto> fromJSONtoItems(String body) {
        JSONObject jsonObject = new JSONObject(body);
        JSONArray items = jsonObject.getJSONArray("items");
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(Object item : items) {
            ItemDto itemDto = new ItemDto((JSONObject) item); // 데이터를 뽑기 위해 JSONObject으로 casting
            itemDtoList.add(itemDto);
        }

        return itemDtoList;
    }
}