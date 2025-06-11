// src/main/java/org/example/bookmanager/dto/loan/LoanDetailDto.java
package org.example.bookmanager.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter; // Setter 추가 (필요 시)

import java.time.LocalDate;

@Getter
@Setter // LoanService에서 필요하다면 Setter 추가
@AllArgsConstructor
public class LoanDetailDto {
    private Long loanId;
    private String bookTitle;
    private LocalDate loanDate;
    //반납일
    private LocalDate dueDate;
    //실제 반납일
    private LocalDate returnDate;
    //반납 했는가
    private boolean returned;
    //연체 여부
    private boolean overdue;
}