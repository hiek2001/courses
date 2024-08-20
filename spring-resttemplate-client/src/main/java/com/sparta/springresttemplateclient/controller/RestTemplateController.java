package com.sparta.springresttemplateclient.controller;

import com.sparta.springresttemplateclient.dto.ItemDto;
import com.sparta.springresttemplateclient.service.RestTemplateService;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class RestTemplateController {

    private final RestTemplateService restTemplateService;

    public RestTemplateController(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    @GetMapping("/get-call-obj")
    public ItemDto getCallObject(String query) { //@RequestParam 생략 가능
        return restTemplateService.getCallObject(query);
    }

    @GetMapping("/get-call-list")
    public List<ItemDto> getCallList() {
        return restTemplateService.getCallList();
    }

    @GetMapping("/post-call")
    public ItemDto postCall(String query) {
        return restTemplateService.postCall(query);
    }

    @GetMapping("/exchange-call")
    public List<ItemDto> exchangeCall(@RequestHeader("Authorization") String token) { // @RequestHeader : header에서 해당되는 Authorization Key의 값을 가져올 수 있음
        return restTemplateService.exchangeCall(token);
    }
}
