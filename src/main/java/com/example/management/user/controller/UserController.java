package com.example.management.user.controller;

import com.example.management.user.dto.UserDto;
import com.example.management.user.mapper.UserMapper;
import com.example.management.user.model.User;
import com.example.management.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = UserMapper.toDtoList(users);
        long userCount = userService.getUserCount();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        Map<Long, String> formattedDates = new HashMap<>();

        for (UserDto userDto : userDtos) {
            if (userDto.getDateNaissance() != null) {
                String formattedDate = userDto.getDateNaissance().format(formatter);
                formattedDates.put(userDto.getId(), formattedDate);
            }
        }

        model.addAttribute("users", userDtos);
        model.addAttribute("userCount", userCount);
        model.addAttribute("formattedDates", formattedDates);

        return "users/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "users/add";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute UserDto userDto, BindingResult result,
                          RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "users/add";
        }

        if (userService.emailExists(userDto.getEmail())) {
            result.rejectValue("email", "error.user", "Email already exists: " + userDto.getEmail());
            return "users/add";
        }

        try {
            User user = UserMapper.toEntity(userDto);
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User added successfully: " + userDto.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding user: " + e.getMessage());
        }

        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isPresent()) {
            UserDto userDto = UserMapper.toDto(userOptional.get());
            model.addAttribute("userDto", userDto);
            return "users/edit";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found with ID: " + id);
            return "redirect:/users";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute UserDto userDto,
                             BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "users/edit";
        }

        try {
            userDto.setId(id);
            User user = UserMapper.toEntity(userDto);
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully: " + userDto.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
        }

        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOptional = userService.getUserById(id);
            if (userOptional.isPresent()) {
                userService.deleteUser(id);
                redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }

        return "redirect:/users";
    }
}