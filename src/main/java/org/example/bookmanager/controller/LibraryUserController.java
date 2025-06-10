package org.example.bookmanager.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmanager.dto.user.RequestAddLibraryUser;
import org.example.bookmanager.dto.user.ResponseAddLibraryUser;
import org.example.bookmanager.service.LibraryUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class LibraryUserController {

    private final LibraryUserService libraryUserService;

    @GetMapping("/user/add")
    public  String getLibraryUser(){return "user/addLibraryUser";}

    @PostMapping("/user/add")
    public String postLibraryUser(@RequestBody RequestAddLibraryUser requestAddLibraryUser) {
        ResponseAddLibraryUser result = libraryUserService.addLibraryUser(requestAddLibraryUser);
        return "redirect:/user/addLibraryUser";
    }

}
