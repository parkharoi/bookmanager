package org.example.bookmanager.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmanager.dto.loan.ResponseLoan;
import org.example.bookmanager.service.LoanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/loan")
public class LoanController {

    private final LoanService loanService;

    // 도서 대출 처리
    @PostMapping // /admin/loan
    public String loanBook(@RequestParam("userId") Long userId,
                           @RequestParam("bookId") Long bookId,
                           RedirectAttributes redirectAttributes) {
        // LoanService의 loanBook 메서드는 ResponseLoan을 반환합니다.
        ResponseLoan response = loanService.loanBook(userId, bookId);

        if (response.isSuccess()){
            redirectAttributes.addFlashAttribute("message", response.getMessage());
        } else {
            // 실패 메시지를 "error" 속성으로 전달
            redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
        }
        return "redirect:/admin/user/" + userId; // 대출 결과와 관계없이 사용자 상세 페이지로 리다이렉트
    }

    // 도서 반납 처리 (기존 LoanController 내용과 동일)
    @PostMapping("/return") // /admin/loan/return
    public String returnBook(@RequestParam("loanId") Long loanId, RedirectAttributes redirectAttributes) {
        Long userId = null; // userId를 미리 선언하여 try-catch 블록 외부에서도 접근 가능하게 함
        try {
            userId = loanService.getUserIdByLoanId(loanId); // 반납 처리 전 userId를 미리 가져옴
            loanService.returnBook(loanId);
            redirectAttributes.addFlashAttribute("message", "도서가 성공적으로 반납되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage()); // 에러 메시지를 "errorMessage" 속성으로 전달
            // 에러 발생 시 userId를 찾을 수 없는 경우 (loanId 자체가 잘못된 경우)를 대비
            if (userId == null) { // userId를 찾지 못했거나 초기화되지 않은 경우
                // 모든 사용자를 보여주는 목록 페이지로 리다이렉트하거나 관리자 홈으로 리다이렉트
                return "redirect:/admin/user/list"; // 모든 사용자 목록으로 리다이렉트
            }
        }
        // 에러 발생 시에도 userId를 알 수 있다면 해당 사용자 상세 페이지로 리다이렉트
        return "redirect:/admin/user/" + userId;
    }
}