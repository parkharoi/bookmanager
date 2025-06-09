package org.example.bookmanager.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private LibraryUser user;

    @ManyToOne
    private Book book;

    private LocalDate loanDate;
    private LocalDate returnDate;

    private boolean returned;
}
