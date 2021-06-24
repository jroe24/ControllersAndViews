package com.jacobroe.ControllersAndViews.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.jacobroe.ControllersAndViews.models.User;
import com.jacobroe.ControllersAndViews.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    // Add User Repo
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Hash User PSWD
    public User registerUser(User user) {
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        user.setEmail(user.getEmail().toLowerCase());
        return userRepository.save(user);
    }
    
    // Find by EMAIL
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Find By ID
    public User findUserById(Long id) {
    	return userRepository.findById(id).orElse(null);
    }
    
    // Authenticate the User
    public boolean authenticateUser(String email, String password) {
        // first find the user by email
        User user = userRepository.findByEmail(email);
        // if we can't find it by email, return false
        if(user == null) {
            return false;
        } else {
            // if the passwords match, return true, else, return false
            if(BCrypt.checkpw(password, user.getPassword())) {
                return true;
            } else {
                return false;
            }
        }
    }
}