// src/main/java/org/example/bookmanager/domain/Loan.java

package org.example.bookmanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
// import lombok.NoArgsConstructor; // Lombok @NoArgsConstructor는 이제 필요 없습니다.
// import lombok.AllArgsConstructor; // 이것도 필요 없습니다.

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@Setter
// @NoArgsConstructor // Lombok @NoArgsConstructor는 이제 필요 없습니다.
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private LibraryUser user;

    @Column(nullable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    private boolean returned;

    // --- 기본 생성자 수동 추가 (이것이 가장 중요합니다!) ---
    public Loan() {
        // JPA/Hibernate가 엔티티를 로드할 때 사용합니다.
    }
    // --- 기본 생성자 수동 추가 끝 ---


    // LoanService에서 사용하는 생성자 (Lombok @AllArgsConstructor 대체)
    public Loan(Book book, LibraryUser user, LocalDate loanDate, LocalDate dueDate) {
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returned = false; // 대출 시에는 항상 반납되지 않음
    }

    // 연체 여부를 판단하는 헬퍼 메소드
    public boolean isOverdue() {
        if (this.returned) {
            return false;
        }
        return this.dueDate.isBefore(LocalDate.now());
    }
}