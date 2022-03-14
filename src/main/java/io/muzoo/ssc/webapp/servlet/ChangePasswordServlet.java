package io.muzoo.ssc.webapp.servlet;

import io.muzoo.ssc.webapp.Routable;
import io.muzoo.ssc.webapp.model.User;
import io.muzoo.ssc.webapp.service.SecurityService;
import io.muzoo.ssc.webapp.service.UserService;

import org.apache.commons.lang.StringUtils;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangePasswordServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/user/password";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            // do MVC in here
            String username = StringUtils.trim(request.getParameter("username")); // from query

            UserService userService = UserService.getInstance();
            User user = userService.getUserByUsername(username);

            // prefill the form
            request.setAttribute("user", user);
            request.setAttribute("username", user.getUsername());

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/password.jsp");
            rd.include(request, response);
            // flash session: remove the attributes right after they are used
            request.getSession().removeAttribute("hasError");
            request.getSession().removeAttribute("message");
        } else {
            request.getSession().removeAttribute("hasError");
            request.getSession().removeAttribute("message");
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            // do MVC in here

            // ensure that the username does not contain loading and tailing spaces
            String username = StringUtils.trim(request.getParameter("username")); // from query
            String password = request.getParameter("password");
            String cpassword = request.getParameter("cpassword");

            UserService userService = UserService.getInstance();
            User user = userService.getUserByUsername(username);

            String errorMessage = null;
            // check if username is valid
            if (user == null) {
                // user does not exist
                errorMessage = String.format("user %s does not exist", username);
            }
            // check if password is valid
            else if (StringUtils.isBlank(password)) {
                // password cannot be blank
                errorMessage = "password cannot be blank";
            }
            // check if confirmed password is match with the password
            else if (!StringUtils.equals(password, cpassword)) {
                // confirmed password does not match
                errorMessage = "confirmed password does not match";
            }

            if (errorMessage != null) {
                request.getSession().setAttribute("hasError", true);
                request.getSession().setAttribute("message", errorMessage);
            } else {
                // update new user
                try {
                    userService.changePassword(username, password);
                    request.getSession().setAttribute("hasError", false);
                    request.getSession().setAttribute("message", String.format("password for user %s has been changed successfully", username));
                    // if success, then redirect
                    response.sendRedirect("/");
                    return;
                } catch (Exception e) {
                    request.getSession().setAttribute("hasError", true);
                    request.getSession().setAttribute("message", e.getMessage());
                }
            }

            // if failed, then reach here
            // prefill the form
            request.setAttribute("username", username);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/password.jsp");
            rd.include(request, response);
            // flash session: remove the attributes right after they are used
            request.getSession().removeAttribute("hasError");
            request.getSession().removeAttribute("message");
        } else {
            request.getSession().removeAttribute("hasError");
            request.getSession().removeAttribute("message");
            response.sendRedirect("/login");
        }
    }

}