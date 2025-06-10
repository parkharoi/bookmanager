package org.example.bookmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookmanager.dto.user.LibraryUserDetailDto;
import org.example.bookmanager.dto.user.RequestAddLibraryUser;
import org.example.bookmanager.dto.user.ResponseAddLibraryUser;
import org.example.bookmanager.service.BookService;
import org.example.bookmanager.service.LibraryUserService;
import org.example.bookmanager.service.LoanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class LibraryUserController {

    private final LibraryUserService libraryUserService;
    private final LoanService loanService;
    private final BookService bookService;

    @GetMapping("/user/add")
    public  String getLibraryUser(Model model){
        model.addAttribute("requestAddLibraryUser", new RequestAddLibraryUser());
        return "user/addLibraryUser";
    }

    @PostMapping("/user/add")
    public String postLibraryUser(@ModelAttribute @Valid RequestAddLibraryUser requestAddLibraryUse,
                                  BindingResult result, Model model) {
        if (result.hasErrors()){
            return "redirect:/admin/user/add";
        }

        ResponseAddLibraryUser response = libraryUserService.addLibraryUser(requestAddLibraryUse);
        if (!response.isSuccess()) {
            model.addAttribute("errorMessage", response.getMessage());
            return "user/addLibraryUser";
        }
        return "redirect:/admin/user/" + response.getUserId();
    }

    @GetMapping("/user/{id}")
    public String getUserDetail(@PathVariable Long id, Model model) {
        LibraryUserDetailDto userDto = libraryUserService.getUserByID(id);
        model.addAttribute("user", userDto);
        model.addAttribute("loans", loanService.getLoansByUserId(id));
        model.addAttribute("availableBooks", bookService.getAvailableBooks());
        return "user/detail";
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
