package org.example.bookmanager.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmanager.dto.RequestAddBook;
import org.example.bookmanager.dto.RequestAddLibraryUser;
import org.example.bookmanager.dto.ResponseAddBook;
import org.example.bookmanager.dto.ResponseAddLibraryUser;
import org.example.bookmanager.service.BookService;
import org.example.bookmanager.service.LibraryUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final LibraryUserService libraryUserService;
    private final BookService bookService;

    @PostMapping("/users/add")
    public String postLibraryUser(@RequestBody RequestAddLibraryUser requestAddLibraryUser) {
        ResponseAddLibraryUser result = libraryUserService.addLibraryUser(requestAddLibraryUser);
        return "redirect:/addLibraryUser";
    }

    @PostMapping("/books/add")
    public String postBook(@RequestBody RequestAddBook requestAddBook) {
        ResponseAddBook result = bookService.addBook(requestAddBook);
        return "관리자가 책 등록 완료!";
    }
}
