package org.example.bookmanager.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibraryUserDetailDto {
    private Long userId;
    private String name;
    private String phone;
    private String memo;

}
