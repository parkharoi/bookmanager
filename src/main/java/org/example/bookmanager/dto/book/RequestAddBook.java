package org.example.bookmanager.dto.book;

import lombok.Data;

@Data
public class RequestAddBook {
    private String title;
    private String author;
    private String publisher;
    private int publicationYear;
    private int price;
}

