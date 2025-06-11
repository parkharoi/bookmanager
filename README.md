# 📚 BookManager

Spring Boot 기반의 간단한 도서 대출/반납 관리 웹 애플리케이션입니다.  
관리자가 책을 등록하고, 사용자가 도서 대출 및 반납을 수행할 수 있습니다.

---

## 🛠️ 기술 스택

- **Java 21**
- **Spring Boot 3.5.0**
- Spring Web (MVC)
- Spring Security
- Spring Data JPA
- Thymeleaf (템플릿 엔진)
- H2 Database (테스트용 인메모리 DB)
- Lombok (코드 축약용)
- JUnit5 (테스트 프레임워크)

## 🧩 ERD
<img width="966" alt="스크린샷 2025-06-11 오후 3 33 05" src="https://github.com/user-attachments/assets/46e945b7-b42d-4c42-af9e-f98fbd6971a0" />


## ✅ 주요 기능

| 기능              | 설명 |
|-------------------|------|
| 📖 **도서 등록**       | 관리자가 책의 제목, 저자, 출판사, 출간년도, 가격 등을 등록할 수 있습니다. |
| 🧾 **도서 조회**       | 등록된 도서 목록을 확인하고, 상세 정보를 조회할 수 있습니다. |
| 👤 **사용자 관리**     | 도서관 사용자(회원)를 등록 및 관리할 수 있습니다. |
| 📚 **도서 대출**       | 사용자가 도서를 대출하면, 대출일과 반납 예정일이 자동 등록됩니다. |
| 🔁 **도서 반납**       | 사용자가 대출한 도서를 반납하면 상태가 변경되고, 연체 여부도 확인됩니다. |
| 🔐 **로그인 및 권한 관리** | Spring Security 기반의 로그인 기능과 관리자/사용자 권한에 따라 접근이 제어됩니다. |
| 🔍 **도서 검색**       | 책 제목을 기준으로 키워드 검색이 가능합니다. |


# 🔍 화면 기능

### 관리자 책 등록 📚
![고객 책등록](https://github.com/user-attachments/assets/dd1f1b0f-aa38-438d-880b-f17471c62907)


### 관리자 수정 및 리스트📚
![책 수정](https://github.com/user-attachments/assets/9dcc05ff-b6bd-4182-a15c-86e5ae6bc86b)


### 사용자 등록 및 대출/반납 👤
![사용자도서대출](https://github.com/user-attachments/assets/4a251086-6d73-4417-b22c-2c82962785ab)



### 도서관 사용자 도서 검색 🔍
![도서관 사용자 도서 조회](https://github.com/user-attachments/assets/e0fb09bc-7afd-4522-b510-fd1ab480729f)

