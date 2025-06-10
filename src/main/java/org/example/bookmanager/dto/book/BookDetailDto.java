package org.example.bookmanager.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDetailDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private int publicationYear;
    private int price;
}
