package org.example.bookmanager.controller;

import lombok.RequiredArgsConstructor;
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
    @PostMapping
    public String loanBook(@RequestParam("userId") Long userId,
                           @RequestParam("bookId") Long bookId,
                           RedirectAttributes redirectAttributes) {
        try {
            // LoanService의 loanBookToUser 메서드는 이제 bookId를 직접 받습니다.
            loanService.loanBookToUser(userId, bookId);
            redirectAttributes.addFlashAttribute("message", "도서가 성공적으로 대출되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/user/" + userId; // 사용자 상세 페이지로 리다이렉트
    }

    // 도서 반납 처리 (기존 LoanController 내용과 동일)
    @PostMapping("/return") // /admin/loan/return
    public String returnBook(@RequestParam("loanId") Long loanId, RedirectAttributes redirectAttributes) {
        try {
            loanService.returnBook(loanId);
            Long userId = loanService.getUserIdByLoanId(loanId); // 반납 후 사용자 ID를 다시 가져옴
            redirectAttributes.addFlashAttribute("message", "도서가 성공적으로 반납되었습니다.");
            return "redirect:/admin/user/" + userId;
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            // 에러 발생 시에도 userId를 알 수 있도록 로직 추가
            Long userId = null;
            try {
                userId = loanService.getUserIdByLoanId(loanId);
            } catch (IllegalArgumentException ex) {
                // loanId조차 유효하지 않아 userId를 찾을 수 없는 경우
                return "redirect:/admin/users"; // 또는 관리자 홈으로 리다이렉트
            }
            return "redirect:/admin/user/" + userId;
        }
    }
}