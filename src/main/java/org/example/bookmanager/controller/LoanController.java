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
    @PostMapping
    public String loanBook(@RequestParam("userId") Long userId,
                           @RequestParam("bookId") Long bookId,
                           RedirectAttributes redirectAttributes) {
        ResponseLoan response = loanService.loanBook(userId, bookId);

        if (response.isSuccess()){
            redirectAttributes.addFlashAttribute("message", response.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
        }
        return "redirect:/admin/user/" + userId;
    }

    // 도서 반납 처리
    @PostMapping("/return")
    public String returnBook(@RequestParam("loanId") Long loanId, RedirectAttributes redirectAttributes) {
        Long userId = null;
        try {
            userId = loanService.getUserIdByLoanId(loanId);
            loanService.returnBook(loanId);
            redirectAttributes.addFlashAttribute("message", "도서가 성공적으로 반납되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            if (userId == null) {
                return "redirect:/admin/user/list";
            }
        }
        return "redirect:/admin/user/" + userId;
    }
}