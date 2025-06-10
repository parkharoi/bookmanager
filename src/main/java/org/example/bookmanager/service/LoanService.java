package org.example.bookmanager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookmanager.domain.Book;
import org.example.bookmanager.domain.LibraryUser;
import org.example.bookmanager.domain.Loan;
import org.example.bookmanager.dto.loan.LoanDetailDto;
import org.example.bookmanager.repository.BookRepository;
import org.example.bookmanager.repository.LibraryUserRepository;
import org.example.bookmanager.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final LibraryUserRepository libraryUserRepository;

    // 사용자 id로 대출 내역 조회
    public List<LoanDetailDto> getLoansByUserId(Long userId) {
        List<Loan> loans = loanRepository.findByUser_UserId(userId);
        return loans.stream()
                .map(loan -> new LoanDetailDto(
                        loan.getId(),
                        loan.getBook().getTitle(),
                        loan.getLoanDate(),
                        loan.getLoanDate().plusDays(15),
                        loan.isReturned()
                ))
                .toList();
    }

    // 대출 반납 처리
    @Transactional
    public void returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("대출 정보를 찾을 수 없습니다."));

        if (loan.isReturned()) {
            throw new IllegalStateException("이미 반납된 도서입니다.");
        }

        loan.setReturned(true);
        loanRepository.save(loan);

        Book book = loan.getBook();
        book.setAvailable(true); // 도서 상태를 대출 가능으로 변경
        bookRepository.save(book);
    }

    // 대출 ID로 사용자 ID 조회
    public Long getUserIdByLoanId(Long loanId) {
        return loanRepository.findById(loanId)
                .map(loan -> loan.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("대출 정보를 찾을 수 없습니다."));
    }

    // 사용자에게 도서 대출 처리
    @Transactional
    public void loanBookToUser(Long userId, Long bookId) {
        // 대출 가능한 책 조회
        Book book = bookRepository.findById(bookId) // ID로 바로 조회
                .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다."));

        // 책이 이미 대출 불가능 상태인지 확인
        if (!book.isAvailable()) {
            throw new IllegalStateException("해당 도서는 현재 대출 불가 상태입니다.");
        }

        // 사용자 확인
        LibraryUser user = libraryUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 같은 책(ID 기준)이 이미 해당 사용자에게 대출 중인지 확인 (반납되지 않은 상태)
        boolean alreadyLoaned = loanRepository.existsByUser_UserIdAndBook_IdAndReturnedFalse(userId, book.getId());
        if (alreadyLoaned) {
            throw new IllegalStateException("이미 해당 도서를 대출 중입니다.");
        }

        // 대출 처리
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(15));
        loan.setReturned(false);

        loanRepository.save(loan);

        // 도서 상태 갱신
        book.setAvailable(false); // 도서 상태를 대출 불가로 변경
        bookRepository.save(book);
    }
}