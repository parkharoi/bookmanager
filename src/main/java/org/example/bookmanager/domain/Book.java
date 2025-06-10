package org.example.bookmanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    private String author;
    private String publisher;
    private int publicationYear;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private boolean available = true;

}
