package org.example.bookmanager.repository;

import org.example.bookmanager.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findFirstByTitleContainingIgnoreCaseAndAvailableTrue(String title);

    //ID로 대출 가능 도서 찾기
    Optional<Book> findByIdAndAvailableTrue(Long id);

    List<Book> findByAvailableTrue();


    //키워드 기반 도서 검색(ID 또는 제목)
    default Optional<Book> findAvailableBookByTitleOrId(String keyword) {
        try {
            Long id = Long.parseLong(keyword);
            return findByIdAndAvailableTrue(id);
        }catch (NumberFormatException e) {
            return findFirstByTitleContainingIgnoreCaseAndAvailableTrue(keyword);
        }
    }
}
