package org.example.bookmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmanager.domain.Book;
import org.example.bookmanager.dto.book.BookDetailDto;
import org.example.bookmanager.dto.book.RequestAddBook;
import org.example.bookmanager.dto.book.ResponseAddBook;
import org.example.bookmanager.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public ResponseAddBook addBook(RequestAddBook request) {
        boolean exists = bookRepository.findByTitle(request.getTitle()).isPresent();
        if (exists) {
            return new ResponseAddBook(false, "이미 등록된 책 제목입니다.", null);
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setPublicationYear(request.getPublicationYear());
        book.setPrice(request.getPrice());

        bookRepository.save(book);
        return new ResponseAddBook(true, "등록성공", book.getId());
    }

    public BookDetailDto getBookDetail(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 ID 책을 찾을 수 없음"));
        return new BookDetailDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublicationYear(),
                book.getPrice()
        );

    }

    public List<BookDetailDto> getAllBooks(){
        return bookRepository.findAll().stream()
                .map(book -> new BookDetailDto(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getPublicationYear(),
                        book.getPrice()
                ))
                .toList();
    }
}
