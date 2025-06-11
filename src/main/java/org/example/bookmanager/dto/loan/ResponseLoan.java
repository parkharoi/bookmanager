// src/main/java/org/example/bookmanager/dto/loan/ResponseLoan.java
package org.example.bookmanager.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseLoan {
    private boolean success;
    private String message;
    private Long loanId;


}