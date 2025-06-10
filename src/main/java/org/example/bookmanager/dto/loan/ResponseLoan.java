package org.example.bookmanager.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseLoan {
    private long loanId;

    private long bookId;
    private long title;

    private long userId;
    private long name;

    private LocalDateTime loanDate;
    private LocalDateTime returnDate;
    private boolean returned;
    private boolean overdue;

}
