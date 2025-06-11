package org.example.bookmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookmanager.dto.user.LibraryUserDetailDto;
import org.example.bookmanager.dto.user.RequestAddLibraryUser;
import org.example.bookmanager.dto.user.ResponseAddLibraryUser;
import org.example.bookmanager.dto.user.UpdateLibraryUser;
import org.example.bookmanager.service.BookService;
import org.example.bookmanager.service.LibraryUserService;
import org.example.bookmanager.service.LoanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class LibraryUserController {

    private final LibraryUserService libraryUserService;
    private final LoanService loanService;
    private final BookService bookService;

    //사용자 등록
    @GetMapping("/user/add")
    public String getLibraryUser(Model model){
        model.addAttribute("requestAddLibraryUser", new RequestAddLibraryUser());
        return "user/addLibraryUser";
    }


    @PostMapping("/user/add")
    public String postLibraryUser(@ModelAttribute @Valid RequestAddLibraryUser requestAddLibraryUse,
                                  BindingResult result, Model model) {
        if (result.hasErrors()){
            return "user/addLibraryUser";
        }

        ResponseAddLibraryUser response = libraryUserService.addLibraryUser(requestAddLibraryUse);
        if (!response.isSuccess()) {
            model.addAttribute("errorMessage", response.getMessage());
            return "user/addLibraryUser";
        }
        return "redirect:/admin/user/" + response.getUserId();
    }

    //상세페이지
    @GetMapping("/user/{id}")
    public String getUserDetail(@PathVariable Long id, Model model) {
        LibraryUserDetailDto userDto = libraryUserService.getUserByID(id);
        model.addAttribute("user", userDto);
        model.addAttribute("loans", loanService.getLoansByUserId(id));
        model.addAttribute("availableBooks", bookService.getAvailableBooks());
        return "user/detail";
    }


    @GetMapping("/user/edit/{id}")
    public String getEditUser(@PathVariable Long id, Model model) {
        LibraryUserDetailDto userDetail = libraryUserService.getUserByID(id);

        UpdateLibraryUser updateUser = new UpdateLibraryUser();
        updateUser.setId(userDetail.getUserId());
        updateUser.setName(userDetail.getName());
        updateUser.setPhone(userDetail.getPhone());
        updateUser.setMemo(userDetail.getMemo());

        model.addAttribute("updateUser", updateUser);
        return "user/editLibraryUser";
    }


    @PostMapping("/user/edit/{id}")
    public String postEditUser(@PathVariable Long id,
                               @ModelAttribute("updateUser") @Valid UpdateLibraryUser request,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes){

        if (result.hasErrors()){
            request.setId(id);
            model.addAttribute("updateUser", request);
            return "user/editLibraryUser";
        }
        try {
            request.setId(id);
            libraryUserService.updateLibraryUser(request);
            redirectAttributes.addFlashAttribute("message", "사용자 정보가 성공적으로 수정되었습니다.");
            return "redirect:/admin/user/" + id;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/user/edit/" + id;
        }
    }


    @PostMapping("/return")
    public String returnBook(@RequestParam Long loanId) {
        Long userId = loanService.getUserIdByLoanId(loanId);
        loanService.returnBook(loanId);
        return "redirect:/admin/user/" + userId;
    }


    @GetMapping("/user/list")
    public String listUsers(Model model) {
        List<LibraryUserDetailDto> users = libraryUserService.getAllUsers();
        model.addAttribute("users", users);
        return"user/list";
    }
}