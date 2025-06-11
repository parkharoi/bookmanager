package org.example.bookmanager.repository;

import org.example.bookmanager.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    // 제목, 저자, 출판사를 기준으로 중복 책을 찾는 메소드 (addBook에서 사용)
    Optional<Book> findByTitleAndAuthorAndPublisher(String title, String author, String publisher);

    // 제목으로 검색 (대소문자 무시, 포함 여부)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // 저자로 검색 (대소문자 무시, 포함 여부)
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // 출판사로 검색 (대소문자 무시, 포함 여부)
    List<Book> findByPublisherContainingIgnoreCase(String publisher);

    // Book 엔티티의 'available' 필드가 true인 책 목록을 반환합니다.
    List<Book> findByAvailableTrue();
}