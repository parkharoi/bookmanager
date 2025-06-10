package org.example.bookmanager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookmanager.domain.Loan;
import org.example.bookmanager.dto.loan.LoanDetailDto;
import org.example.bookmanager.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LoanService {

    private final LoanRepository loanRepository;

    //사용자 id로 대출 내역 조회
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

    //대출 반납 처리
    @Transactional
    public void returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(()-> new IllegalArgumentException("대출 정보 못찾음"));

        loan.setReturned(true);
        loan.setLoanDate(LocalDate.now());
    }

    //대출 ID로 사용자 ID 조회
    public Long getUserIdByLoanId(Long loanId) {
        return loanRepository.findById(loanId)
                .map(loan -> loan.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("대출 정보 못 찾음"));
    }

}
