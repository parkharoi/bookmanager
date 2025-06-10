package org.example.bookmanager.repository;

import org.example.bookmanager.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    // 해당 책 대출 확인
    Optional<Loan> findByBook_IdAndReturnedFalse(Long bookId);

    // 특정 사용자 대출 내역 전체
    List<Loan> findByUser_UserId(Long userId);

    // 특정 사용자 현재 대출 도서
    List<Loan> findByUser_UserIdAndReturnedFalse(Long userId);

    // 모든 미반납 도서
    List<Loan> findByReturnedFalse();

    boolean existsByUser_UserIdAndBook_IdAndReturnedFalse(Long userId, Long bookId);

}
