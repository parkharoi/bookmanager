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
    private LocalDate dueDate;
    private LocalDate returnDate; // 실제 반납일 필드 추가
    private boolean returned;
    private boolean overdue; // 연체 여부 필드 추가 (LoanService에서 설정)
}