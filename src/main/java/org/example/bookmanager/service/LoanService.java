// src/main/java/org/example/bookmanager/service/LoanService.java

package org.example.bookmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmanager.domain.Book;
import org.example.bookmanager.domain.LibraryUser;
import org.example.bookmanager.domain.Loan;
import org.example.bookmanager.dto.loan.LoanDetailDto;
import org.example.bookmanager.dto.loan.ResponseLoan;
import org.example.bookmanager.repository.BookRepository;
import org.example.bookmanager.repository.LibraryUserRepository;
import org.example.bookmanager.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final LibraryUserRepository libraryUserRepository;

    /**
     * 특정 사용자의 모든 대출 내역을 조회합니다.
     * @param userId 사용자 ID
     * @return LoanDetailDto 리스트
     */
    @Transactional(readOnly = true)
    public List<LoanDetailDto> getLoansByUserId(Long userId) {
        List<Loan> loans = loanRepository.findByUser_UserId(userId);
        return loans.stream()
                .map(loan -> new LoanDetailDto(
                        loan.getId(),
                        loan.getBook().getTitle(),
                        loan.getLoanDate(),
                        loan.getDueDate(),
                        loan.getReturnDate(),
                        loan.isReturned(),
                        loan.isOverdue()
                ))
                .toList();
    }

    @Transactional
    public void returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("대출 정보를 찾을 수 없습니다. (ID: " + loanId + ")"));

        if (loan.isReturned()) {
            throw new IllegalStateException("이미 반납된 도서입니다.");
        }

        loan.setReturnDate(LocalDate.now());
        loan.setReturned(true);

        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Long getUserIdByLoanId(Long loanId) {
        return loanRepository.findById(loanId)
                .map(loan -> loan.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("대출 정보를 찾을 수 없습니다. (ID: " + loanId + ")"));
    }

    @Transactional
    public ResponseLoan loanBook(Long userId, Long bookId) {
        // 1. 사용자 존재 여부 확인
        LibraryUser user = libraryUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. (ID: " + userId + ")"));

        // 2. 사용자가 연체 중인 대출이 있는지 확인
        boolean userIsOverdue = loanRepository.findByUser_UserId(userId).stream()
                .anyMatch(Loan::isOverdue); // Loan 엔티티의 isOverdue() 사용
        if (userIsOverdue) {
            return new ResponseLoan(false, "연체 중인 사용자는 도서를 대출할 수 없습니다.", null);
        }

        // 3. 책 존재 여부 확인
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다. (ID: " + bookId + ")"));

        // 4. 책의 'available' 상태 확인
        if (!book.isAvailable()) {
            return new ResponseLoan(false, "해당 도서는 현재 대출 불가능 상태입니다.", null);
        }

        // 5. 같은 책(ID 기준)이 현재 다른 사용자에게 대출 중인지 확인 (반납되지 않은 상태)
        if (loanRepository.findByBook_IdAndReturnedFalse(bookId).isPresent()) {
            return new ResponseLoan(false, "해당 도서는 현재 다른 사용자에게 대출 중입니다.", null);
        }

        // 6. 대출일 및 반납 기한 계산
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(15);

        // 7. Loan 엔티티 생성 및 저장
        Loan loan = new Loan(book, user, loanDate, dueDate);
        Loan savedLoan = loanRepository.save(loan);

        // 8. 도서 상태 갱신
        book.setAvailable(false);
        bookRepository.save(book);

        return new ResponseLoan(true, "도서 대출에 성공했습니다.", savedLoan.getId());
    }
}