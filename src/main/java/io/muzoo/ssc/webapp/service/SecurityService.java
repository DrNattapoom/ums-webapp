package io.muzoo.ssc.webapp.service;

import io.muzoo.ssc.webapp.model.User;

import org.mindrot.jbcrypt.BCrypt;
import javax.servlet.http.HttpServletRequest;

public class SecurityService {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    public boolean isAuthorized(HttpServletRequest request) {
        String username = (String) request.getSession()
                .getAttribute("username");
        // get user from the database
        User user = userService.getUserByUsername(username);
        // do checking
        return (username != null && user != null);
    }
    
    public boolean authenticate(String username, String password, HttpServletRequest request) {
        // get user from the database
        User user = userService.getUserByUsername(username);
        boolean isMatched = user != null && BCrypt.checkpw(password, user.getPassword());
        if (isMatched) {
            request.getSession().setAttribute("username", username);
            return true;
        } else {
            return false;
        }
    }
    
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }
    
}