package org.example.bookmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmanager.domain.Book;
import org.example.bookmanager.dto.RequestAddBook;
import org.example.bookmanager.dto.ResponseAddBook;
import org.example.bookmanager.repository.BookRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public ResponseAddBook addBook(RequestAddBook request) {
        boolean exists = bookRepository.findByTitle(request.getTitle()).isPresent();
        if (exists) {
            return new ResponseAddBook(false, "이미 등록된 책 제목입니다.");
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setPublicationYear(request.getPublicationYear());
        book.setPrice(request.getPrice());

        bookRepository.save(book);
        return new ResponseAddBook(true, "성공적으로 등록되었습니다.");
    }

}
