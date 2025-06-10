package org.example.bookmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.bookmanager.dto.book.BookDetailDto;
import org.example.bookmanager.dto.book.RequestAddBook;
import org.example.bookmanager.dto.book.ResponseAddBook;
import org.example.bookmanager.service.BookService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BookController {

    private final BookService bookService;

    @GetMapping("/book/add")
    public String getAddBook(Model model, HttpServletRequest request) {
        model.addAttribute("requestAddBook", new RequestAddBook());
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrfToken);
        return "book/addBook";
    }


    @PostMapping("/book/add")

    public String postAddBook(@ModelAttribute RequestAddBook requestAddBook) {
        ResponseAddBook result = bookService.addBook(requestAddBook);
        if (result.isSuccess()){
            return "redirect:/admin/book/"+ result.getBookId();
        }else  {
            return "book/addBook";
        }
    }

    @GetMapping("/book/{id}")
    public String getBookDetail(@PathVariable Long id, Model model) {
        BookDetailDto book = bookService.getBookDetail(id);
        model.addAttribute("book", book);
        return "book/detail";
    }
}
