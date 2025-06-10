package org.example.bookmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.bookmanager.domain.Book;
import org.example.bookmanager.dto.book.BookDetailDto;
import org.example.bookmanager.dto.book.RequestAddBook;
import org.example.bookmanager.dto.book.ResponseAddBook;
import org.example.bookmanager.repository.BookRepository;
import org.example.bookmanager.service.BookService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
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
        } else  {
            return "book/addBook";
        }
    }

    @GetMapping("/book/{id}")
    public String getBookDetail(@PathVariable Long id, Model model) {
        BookDetailDto book = bookService.getBookDetail(id);
        model.addAttribute("book", book);
        return "book/detail";
    }

    @GetMapping("/book/list")
    public String getBookList(Model model) {
        List<BookDetailDto> bookList = bookService.getAllBooks();
        model.addAttribute("bookList", bookList);
        return "book/list";
    }

    @GetMapping("/book/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));
        model.addAttribute("book", book);
        return "book/editBook"; // editBook.html
    }

    @PostMapping("/book/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setPublisher(book.getPublisher());
        existing.setPublicationYear(book.getPublicationYear());
        existing.setPrice(book.getPrice());

        bookRepository.save(existing);
        return "redirect:/admin/book/" + id;
    }

}