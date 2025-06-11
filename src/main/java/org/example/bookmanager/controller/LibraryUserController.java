package org.example.bookmanager.controller;

import jakarta.validation.Valid; // 유효성 검사를 위한 어노테이션
import lombok.RequiredArgsConstructor; // Lombok: final 필드에 대한 생성자 자동 생성
import org.example.bookmanager.dto.user.LibraryUserDetailDto;
import org.example.bookmanager.dto.user.RequestAddLibraryUser;
import org.example.bookmanager.dto.user.ResponseAddLibraryUser;
import org.example.bookmanager.dto.user.UpdateLibraryUser; // 사용자 정보 수정 DTO
import org.example.bookmanager.service.BookService;
import org.example.bookmanager.service.LibraryUserService;
import org.example.bookmanager.service.LoanService;
import org.springframework.stereotype.Controller; // 컨트롤러임을 명시
import org.springframework.ui.Model; // 뷰로 데이터를 전달하기 위한 Model 인터페이스
import org.springframework.validation.BindingResult; // 유효성 검사 결과를 담는 객체
import org.springframework.web.bind.annotation.*; // 웹 요청 매핑 관련 어노테이션
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // 리다이렉트 시 데이터 전달용

import java.util.List;

@Controller // 이 클래스가 스프링 MVC 컨트롤러임을 나타냅니다.
@RequiredArgsConstructor // final 필드를 초기화하는 생성자를 자동으로 만듭니다.
@RequestMapping("/admin") // 이 컨트롤러의 모든 핸들러 메소드는 "/admin" 경로 아래에 매핑됩니다.
public class LibraryUserController {

    private final LibraryUserService libraryUserService; // 사용자 관련 비즈니스 로직 처리
    private final LoanService loanService; // 대출 관련 비즈니스 로직 처리
    private final BookService bookService; // 책 관련 비즈니스 로직 처리

    /**
     * 새 사용자 등록 폼을 보여줍니다.
     * GET /admin/user/add 요청을 처리합니다.
     */
    @GetMapping("/user/add")
    public String getLibraryUser(Model model){
        // 폼 바인딩을 위한 빈 객체를 모델에 추가합니다.
        model.addAttribute("requestAddLibraryUser", new RequestAddLibraryUser());
        return "user/addLibraryUser"; // user/addLibraryUser.html 뷰를 반환합니다.
    }

    /**
     * 새 사용자 등록 폼 제출을 처리합니다.
     * POST /admin/user/add 요청을 처리합니다.
     */
    @PostMapping("/user/add")
    public String postLibraryUser(@ModelAttribute @Valid RequestAddLibraryUser requestAddLibraryUse,
                                  BindingResult result, Model model) {
        // 유효성 검사 실패 시
        if (result.hasErrors()){
            // 리다이렉트하지 않고 폼 페이지로 직접 돌아가서 오류 메시지를 표시합니다.
            // @ModelAttribute 덕분에 requestAddLibraryUse 객체가 모델에 유지되어 입력값도 보존됩니다.
            return "user/addLibraryUser";
        }

        ResponseAddLibraryUser response = libraryUserService.addLibraryUser(requestAddLibraryUse);
        // 사용자 등록 비즈니스 로직 실패 시 (예: 중복 사용자)
        if (!response.isSuccess()) {
            model.addAttribute("errorMessage", response.getMessage());
            return "user/addLibraryUser";
        }
        // 등록 성공 시 해당 사용자의 상세 페이지로 리다이렉트합니다.
        return "redirect:/admin/user/" + response.getUserId();
    }

    /**
     * 특정 사용자 상세 정보를 보여줍니다.
     * GET /admin/user/{id} 요청을 처리합니다.
     */
    @GetMapping("/user/{id}")
    public String getUserDetail(@PathVariable Long id, Model model) {
        LibraryUserDetailDto userDto = libraryUserService.getUserByID(id); // ID로 사용자 정보 조회
        model.addAttribute("user", userDto); // 사용자 정보를 모델에 추가
        model.addAttribute("loans", loanService.getLoansByUserId(id)); // 해당 사용자의 대출 현황을 모델에 추가
        model.addAttribute("availableBooks", bookService.getAvailableBooks()); // 대출 가능한 모든 책 목록을 모델에 추가
        return "user/detail"; // user/detail.html 뷰를 반환합니다.
    }

    /**
     * 사용자 정보 수정 폼을 보여줍니다.
     * GET /admin/user/edit/{id} 요청을 처리합니다.
     */
    @GetMapping("/user/edit/{id}")
    public String getEditUser(@PathVariable Long id, Model model) {
        // 수정할 사용자 정보를 조회합니다.
        LibraryUserDetailDto userDetail = libraryUserService.getUserByID(id);

        // UpdateLibraryUser DTO 객체를 생성하고 조회된 데이터로 채웁니다.
        UpdateLibraryUser updateUser = new UpdateLibraryUser();
        updateUser.setId(userDetail.getUserId());
        updateUser.setName(userDetail.getName());
        updateUser.setPhone(userDetail.getPhone());
        updateUser.setMemo(userDetail.getMemo());

        model.addAttribute("updateUser", updateUser); // 모델에 수정할 사용자 정보를 추가합니다.
        return "user/editLibraryUser"; // user/editLibraryUser.html 뷰를 반환합니다.
    }

    /**
     * 사용자 정보 수정 폼 제출을 처리합니다.
     * POST /admin/user/edit/{id} 요청을 처리합니다.
     */
    @PostMapping("/user/edit/{id}")
    public String postEditUser(@PathVariable Long id,
                               @ModelAttribute("updateUser") @Valid UpdateLibraryUser request,
                               BindingResult result,
                               Model model, // <-- Model 파라미터 추가! 유효성 검사 실패 시 에러 메시지 전달용
                               RedirectAttributes redirectAttributes){

        // 유효성 검사 실패 시
        if (result.hasErrors()){
            // DTO의 ID가 null이 되는 것을 방지하기 위해 @PathVariable의 ID를 다시 설정합니다.
            request.setId(id);
            // 유효성 검사 오류와 함께 request 객체를 모델에 다시 추가하여 폼에 입력값을 보존합니다.
            // @ModelAttribute가 자동으로 해주기도 하지만, 명시적으로 추가하는 것이 더 안전합니다.
            model.addAttribute("updateUser", request);
            return "user/editLibraryUser"; // 리다이렉트 없이 폼 페이지로 돌아가서 에러를 표시합니다.
        }
        try {
            // DTO에 URL로부터 받은 ID를 명시적으로 설정합니다.
            request.setId(id);
            // 서비스 계층의 사용자 정보 업데이트 메소드를 호출합니다.
            libraryUserService.updateLibraryUser(request);
            // 성공 메시지를 Flash Attribute에 담아 리다이렉트 후에도 메시지를 볼 수 있도록 합니다.
            redirectAttributes.addFlashAttribute("message", "사용자 정보가 성공적으로 수정되었습니다.");
            // 수정 성공 시 해당 사용자의 상세 페이지로 리다이렉트합니다.
            return "redirect:/admin/user/" + id;
        } catch (IllegalArgumentException e) {
            // 비즈니스 로직 오류(예: 중복 전화번호) 발생 시 에러 메시지를 Flash Attribute에 담습니다.
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            // 에러 발생 시 수정 폼으로 리다이렉트합니다. (ID를 URL에 포함)
            return "redirect:/admin/user/edit/" + id;
        }
    }

    /**
     * 도서 반납 요청을 처리합니다.
     * POST /admin/return 요청을 처리합니다. (경로가 "/admin/loan/return"이었으나, 기존 코드에 맞춰 변경)
     */
    @PostMapping("/return") // TODO: /admin/loan/return 으로 변경 고려
    public String returnBook(@RequestParam Long loanId) {
        Long userId = loanService.getUserIdByLoanId(loanId); // 대출 ID로 사용자 ID를 조회
        loanService.returnBook(loanId); // 도서 반납 처리
        return "redirect:/admin/user/" + userId; // 해당 사용자 상세 페이지로 리다이렉트
    }

    /**
     * 모든 사용자 목록을 보여줍니다.
     * GET /admin/user/list 요청을 처리합니다.
     */
    @GetMapping("/user/list")
    public String listUsers(Model model) {
        List<LibraryUserDetailDto> users = libraryUserService.getAllUsers(); // 모든 사용자 정보 조회
        model.addAttribute("users", users); // 사용자 목록을 모델에 추가
        return"user/list"; // user/list.html 뷰를 반환합니다.
    }
}