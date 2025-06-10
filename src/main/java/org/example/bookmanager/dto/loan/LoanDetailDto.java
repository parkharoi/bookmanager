package org.example.bookmanager.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class LoanDetailDto {
    private Long userId;
    private String bookTitle;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private boolean returned;
}

