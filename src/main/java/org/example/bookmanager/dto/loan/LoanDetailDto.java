package org.example.bookmanager.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class LoanDetailDto {
    private Long loanId;
    private String bookTitle;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private boolean returned;

    public boolean isOverdue() {
        // 반납되지 않았고, 반납 예정일이 오늘보다 이전인 경우 연체
        return !returned && dueDate.isBefore(LocalDate.now());
    }
}