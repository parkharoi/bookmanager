package org.example.bookmanager.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminContoller {

    @PostMapping("/users/add")
    public String createUser() {
        return "관리자가 사용자 등록 완료!";
    }

    @PostMapping("/books/add")
    public String createBook(){
        return "관리자가 책 등록 완료!";
    }
}
