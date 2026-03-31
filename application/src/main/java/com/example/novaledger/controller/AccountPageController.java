package com.example.novaledger.controller;

import com.example.novaledger.finance.account.dto.CreateAccountRequest;
import com.example.novaledger.finance.account.enums.AccountType;
import com.example.novaledger.finance.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
public class AccountPageController {

    private final AccountService accountService;

    public AccountPageController(AccountService accountService) {
        this.accountService = accountService;
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute("userId");
    }

    @GetMapping
    public String listAccounts(Model model, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        model.addAttribute("accounts", accountService.getAccounts(userId));
        return "accountList";
    }

    @GetMapping("/new")
    public String newAccountForm(Model model) {
        model.addAttribute("accountForm", new CreateAccountRequest());
        model.addAttribute("accountTypes", AccountType.values());
        return "accountCreate";
    }

    @PostMapping
    public String createAccount(@Valid @ModelAttribute("accountForm") CreateAccountRequest form,
                                BindingResult bindingResult,
                                Model model,
                                HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountTypes", AccountType.values());
            return "accountCreate";
        }
        Long userId = getCurrentUserId(request);
        accountService.createAccount(userId, form);
        return "redirect:/accounts";
    }

    @PostMapping("/{id}")
    public String deleteAccount(@PathVariable Long id,
                                @RequestParam String _method,
                                HttpServletRequest request) {
        if ("DELETE".equalsIgnoreCase(_method)) {
            Long userId = getCurrentUserId(request);
            accountService.deleteAccount(userId, id);
        }
        return "redirect:/accounts";
    }
}