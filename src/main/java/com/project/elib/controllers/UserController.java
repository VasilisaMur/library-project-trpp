package com.project.elib.controllers;

import com.project.elib.models.Books;
import com.project.elib.models.Role;
import com.project.elib.models.User;
import com.project.elib.repo.UserRepository;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")

public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user){
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        // очистка ролей пользователя
        user.getRoles().clear();

        // добавление новых ролей
        for (String key : form.keySet()){
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("books", user.getFavorites());

        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password){

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        userRepository.save(user);

        return "redirect:/user/profile";
    }

    @GetMapping("favorites/add/{book}")
    public String addBookToFavorite(
            @AuthenticationPrincipal User user,
            @PathVariable Books book,
            Model model){

        book.getSubscribers().add(user);
        userRepository.save(user);

        return "redirect:/page/" + book.getId();

    }

    @GetMapping("favorites/remove/{book}")
    public String removeBookToFavorite(
            @AuthenticationPrincipal User user,
            @PathVariable Books book,
            Model model){

        book.getSubscribers().remove(user);
        userRepository.save(user);

        return "redirect:/page/" + book.getId();

    }

    @GetMapping("favorites")
    public String favoriteBooksList(
            Model model,
            @AuthenticationPrincipal User user
    ){
        model.addAttribute("books", user.getFavorites());

        return "favoriteBooks";
    }
}
