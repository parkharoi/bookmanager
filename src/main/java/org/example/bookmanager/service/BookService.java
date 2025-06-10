package org.example.bookmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmanager.domain.Book;
import org.example.bookmanager.domain.Loan;
import org.example.bookmanager.dto.book.BookDetailDto;
import org.example.bookmanager.dto.book.RequestAddBook;
import org.example.bookmanager.dto.book.ResponseAddBook;
import org.example.bookmanager.repository.BookRepository;
import org.example.bookmanager.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public ResponseAddBook addBook(RequestAddBook request) {

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

        Optional<Loan> loanOpt = loanRepository.findByBook_IdAndReturnedFalse(id);

        String loanStatus = loanOpt.isPresent() ? "대출 중" : "대출 가능";
        LocalDate loanDueDate = loanOpt.map(loan -> loan.getLoanDate().plusDays(15)).orElse(null);

        return new BookDetailDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublicationYear(),
                book.getPrice(),
                loanStatus,
                loanDueDate
        );

    }


    public List<BookDetailDto> getAllBooks() {
        return bookRepository.findAll().stream().map(book -> {
            Optional<Loan> loanOpt = loanRepository.findByBook_IdAndReturnedFalse(book.getId());

            String loanStatus = loanOpt.isPresent() ? "대출 중" : "대출 가능";
            LocalDate loanDueDate = loanOpt.map(loan -> loan.getLoanDate().plusDays(15)).orElse(null);

            return new BookDetailDto(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublisher(),
                    book.getPublicationYear(),
                    book.getPrice(),
                    loanStatus,
                    loanDueDate
            );
        }).toList();
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findAll().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

}
