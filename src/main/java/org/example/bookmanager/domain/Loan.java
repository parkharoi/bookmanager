
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

    // JPA/Hibernate가 엔티티를 로드
    public Loan() {

    }

    // service에서 사용
    public Loan(Book book, LibraryUser user, LocalDate loanDate, LocalDate dueDate) {
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returned = false;
    }

    // 연체 여부를 판단하는
    public boolean isOverdue() {
        if (this.returned) {
            return false;
        }
        return this.dueDate.isBefore(LocalDate.now());
    }
}