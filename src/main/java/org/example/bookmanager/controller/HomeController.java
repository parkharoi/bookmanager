package org.example.bookmanager.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmanager.dto.book.BookDetailDto;
import org.example.bookmanager.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BookService bookService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "adminHome";
    }

    @GetMapping("/search")
    public String userBooks(
            @RequestParam(value = "searchType", required = false, defaultValue = "title") String searchType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder,
            Model model
    ) {
        List<BookDetailDto> books;

        if (keyword == null || keyword.trim().isEmpty()) {
            books = bookService.getAllBooks();
        } else {
            if ("title".equals(searchType)) {
                books = bookService.searchBooksByTitle(keyword);
            } else if ("author".equals(searchType)) {
                books = bookService.searchBooksByAuthor(keyword);
            } else if ("publisher".equals(searchType)) {
                books = bookService.searchBooksByPublisher(keyword);
            } else {
                books = bookService.getAllBooks();
            }
        }

        books = bookService.sortBooks(books, sortBy, sortOrder);

        boolean showDueDate = books.stream().anyMatch(book -> book.getLoanDueDate() != null);

        model.addAttribute("books", books);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("showDueDate", showDueDate);

        return "userSearch";
    }
}