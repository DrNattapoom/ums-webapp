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

public class EditUserServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/user/edit";
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
            request.setAttribute("displayName", user.getDisplayName());

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/edit.jsp");
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

            // user is only allowed to edit display name
            // ensure that the username and the display name do not contain loading and tailing spaces
            String username = StringUtils.trim(request.getParameter("username")); // from query
            String displayName = StringUtils.trim(request.getParameter("displayName"));

            UserService userService = UserService.getInstance();
            User user = userService.getUserByUsername(username);

            String errorMessage = null;
            // check if username is valid
            if (user == null) {
                // user does not exist
                errorMessage = String.format("user %s does not exist", username);
            }
            // check if display name is valid
            else if (StringUtils.isBlank(displayName)) {
                // display name cannot be blank
                errorMessage = "display name cannot be blank";
            }

            if (errorMessage != null) {
                request.getSession().setAttribute("hasError", true);
                request.getSession().setAttribute("message", errorMessage);
            } else {
                // update new user
                try {
                    userService.editUserByUsername(username, displayName);
                    request.getSession().setAttribute("hasError", false);
                    request.getSession().setAttribute("message", String.format("user %s has been updated successfully", username));
                    // if success, then redirect
                    response.sendRedirect("/users");
                    return;
                } catch (Exception e) {
                    request.getSession().setAttribute("hasError", true);
                    request.getSession().setAttribute("message", e.getMessage());
                }
            }

            // if failed, then reach here
            // prefill the form
            request.setAttribute("username", username);
            request.setAttribute("displayName", displayName);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/edit.jsp");
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