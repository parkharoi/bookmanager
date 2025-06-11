// src/main/java/org/example/bookmanager/dto/loan/ResponseLoan.java
package org.example.bookmanager.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 이 DTO는 대출/반납 요청 후 결과를 클라이언트에 알릴 때 사용됩니다.
// 성공 여부, 메시지, 처리된 대출 ID 정도가 적합합니다.
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseLoan {
    private boolean success; // 요청 성공 여부
    private String message; // 사용자에게 보여줄 메시지 (예: "대출 성공", "대출 실패: 이미 대출 중")
    private Long loanId; // 처리된 대출의 ID (성공 시)

    // 이전 코드에 있던 불필요하거나 잘못된 필드들은 제거됩니다.
    // private long bookId;
    // private long title; // 제목은 문자열이어야 함
    // private long userId;
    // private long name; // 이름은 문자열이어야 함
    // private LocalDateTime loanDate;
    // private LocalDateTime returnDate;
    // private boolean returned;
    // private boolean overdue;
}