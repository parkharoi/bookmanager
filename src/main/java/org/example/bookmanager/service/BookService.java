package org.example.bookmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmanager.domain.Book;
import org.example.bookmanager.domain.Loan;
import org.example.bookmanager.dto.book.BookDetailDto;
import org.example.bookmanager.dto.book.RequestAddBook;
import org.example.bookmanager.dto.book.ResponseAddBook;
import org.example.bookmanager.repository.BookRepository; // BookRepository 임포트
import org.example.bookmanager.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;


    @Transactional
    public ResponseAddBook addBook(RequestAddBook request) {
        if (bookRepository.findByTitleAndAuthorAndPublisher(request.getTitle(), request.getAuthor(), request.getPublisher()).isPresent()) {
            return new ResponseAddBook(false, "이미 존재하는 책입니다.", null);
        }
        // --- 수정된 부분 끝 ---

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setPublicationYear(request.getPublicationYear());
        book.setPrice(request.getPrice());
        book.setAvailable(true); // 기본적으로 새로 등록된 책은 대출 가능 상태

        Book savedBook = bookRepository.save(book);
        return new ResponseAddBook(true, "책이 성공적으로 등록되었습니다.", savedBook.getId());
    }

    @Transactional(readOnly = true)
    public BookDetailDto getBookDetail(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다: " + id));
        return convertToDto(book);
    }

    public List<BookDetailDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<BookDetailDto> searchBooksByTitle(String titleKeyword) {
        return bookRepository.findByTitleContainingIgnoreCase(titleKeyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookDetailDto> searchBooksByAuthor(String authorKeyword) {
        return bookRepository.findByAuthorContainingIgnoreCase(authorKeyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookDetailDto> searchBooksByPublisher(String publisherKeyword) {
        return bookRepository.findByPublisherContainingIgnoreCase(publisherKeyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookDetailDto> sortBooks(List<BookDetailDto> books, String sortBy, String sortOrder) {
        Comparator<BookDetailDto> comparator = null;

        switch (sortBy) {
            case "title":
                comparator = Comparator.comparing(BookDetailDto::getTitle);
                break;
            case "author":
                comparator = Comparator.comparing(BookDetailDto::getAuthor);
                break;
            case "publicationYear":
                comparator = Comparator.comparing(BookDetailDto::getPublicationYear);
                break;
            default:
                comparator = Comparator.comparing(BookDetailDto::getTitle);
                break;
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        return books.stream().sorted(comparator).collect(Collectors.toList());
    }

    public List<BookDetailDto> getAvailableBooks() {
        List<Book> allAvailableBooks = bookRepository.findByAvailableTrue();

        return allAvailableBooks.stream()
                .filter(book -> !loanRepository.findByBook_IdAndReturnedFalse(book.getId()).isPresent())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BookDetailDto convertToDto(Book book) {
        Optional<Loan> loanOpt = loanRepository.findByBook_IdAndReturnedFalse(book.getId());

        String loanStatus = "대출 가능";
        LocalDate loanDueDate = null;

        if (loanOpt.isPresent()) {
            loanStatus = "대출 중";
            loanDueDate = loanOpt.get().getLoanDate().plusDays(15);
        }

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
}