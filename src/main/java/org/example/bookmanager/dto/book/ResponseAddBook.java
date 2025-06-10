package org.example.bookmanager.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseAddBook {
    private boolean success;
    private String message;
    private Long bookId;
}
