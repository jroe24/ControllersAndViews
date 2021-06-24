package com.jacobroe.ControllersAndViews.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.jacobroe.ControllersAndViews.models.User;
import com.jacobroe.ControllersAndViews.services.UserService;

@Controller
public class Users {
	
	private final UserService userService;
    
	
    public Users(UserService userService) {
        this.userService = userService;
    }
    
    //Route for registration page
    @RequestMapping("/registration")
    public String registerForm(@ModelAttribute("user") User user) {
        return "jsp/registrationPage.jsp";
    }
    
    //Route for login page
    @RequestMapping("/login")
    public String login() {
        return "jsp/loginPage.jsp";
    }
    
    @RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
    	// If result has errors, return the registration page
    	if(result.hasErrors()) {
    		return "jsp/registrationPage.jsp";
    	}
    	 // Save the user in the database, save the user id in session, and redirect them to the /home route
    	User u = userService.registerUser(user);
    	session.setAttribute("userId", u.getId());
    	return "redirect:/home";
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
    	// If the user is authenticated, save their user id in session
    	if(userService.authenticateUser(email, password)) {
    		session.setAttribute("useremail", email);
    		return "redirect:/home";
    	} else {
    		// else add error message and return login page
    		model.addAttribute("error", "The username / password combination is not valid! Please try again.");
    		return "jsp/loginPage.jsp";
    	}
    }
    
    @RequestMapping("/home")
    public String home(HttpSession session, Model model) {
        // Get user from session, save them in the model and return the home page
    	model.addAttribute("user", userService.findByEmail((String)session.getAttribute("useremail")));
    	return "jsp/homePage.jsp";
    }
    // Invalidates session and redirects to /login
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "jsp/redirect:/login";
    }
}