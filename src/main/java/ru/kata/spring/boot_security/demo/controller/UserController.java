package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@EnableAutoConfiguration
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/user")
    public ModelAndView showUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping(value = "/admin/userApp")
    public String showUserForm(Model model, ModelMap modelMap) {
        model.addAttribute("userFormer", new User());
        List<User> userList = userService.getUserList();
        modelMap.addAttribute("users", userList);
        return "adminPage";
    }

    @PostMapping(value = "/admin/userApp")
    public String addUserPostMethod(Model model, @ModelAttribute("userFormer") User userForm, ModelMap modelMap) {
        model.addAttribute("userFormer", userForm);
        String name = userForm.getName();
        String password = userForm.getPassword();
        int salary = userForm.getSalary();
        User newUser = new User(name,password,salary);
        userService.saveUser(newUser);
        List<User> userList = userService.getUserList();
        modelMap.addAttribute("users", userList);
        return "adminPage";
    }

    @GetMapping("/admin/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("userFinded", new User());
        return "updateUserPage";
    }

    @PostMapping("/admin/update/{id}")
    public String updateUser(@PathVariable("id") long id, @ModelAttribute("userFinded") User userFinded, Model model) {
        model.addAttribute("userFinded", userFinded);
        userService.updateUser(userFinded, id);
        return "updateUserPage";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        userService.deleteUserById(id);
        return "deletedUserPage";
    }
}
