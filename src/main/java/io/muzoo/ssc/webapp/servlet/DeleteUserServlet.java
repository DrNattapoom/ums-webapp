/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.muzoo.ssc.webapp.servlet;

import io.muzoo.ssc.webapp.Routable;
import io.muzoo.ssc.webapp.model.User;
import io.muzoo.ssc.webapp.service.SecurityService;
import io.muzoo.ssc.webapp.service.UserService;

import org.apache.commons.lang.StringUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteUserServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/user/delete";
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
            String username = (String) request.getSession().getAttribute("username");
            UserService userService = UserService.getInstance();
            try {
                User currentUser = userService.getUserByUsername(username);
                User toBeDeleted = userService.getUserByUsername(request.getParameter("username"));
                // although user cannot delete one's own account from the ui, the user can still send a request directly to the server
                // thus, we also have to prevent the user from deleting one's own account in here
                boolean sameAccount = StringUtils.equals(currentUser.getUsername(), toBeDeleted.getUsername());
                // if the sameAccount is true, then !userService.deleteUserByUsername(toBeDeleted.getUsername()) will not be evaluated and so the user will not be deleted
                boolean hasError = sameAccount || !userService.deleteUserByUsername(toBeDeleted.getUsername());
                String message = (sameAccount) ? "user cannot delete one's own account" : (hasError) ? "unable to delete user %s" : "user %s is successfully deleted";
                // note: the attributes are added to the session, and they will persist unless they are removed from the session
                // so, they are needed to be deleted when they are read next time
                // since in every case it will redirect to the home page, we will remove these attributes in the home servlet
                request.getSession().setAttribute("hasError", hasError);
                request.getSession().setAttribute("message", String.format(message, toBeDeleted.getUsername()));
            } catch (Exception e) {
                request.getSession().setAttribute("hasError", true);
                request.getSession().setAttribute("message", String.format("unable to delete user %s", request.getParameter("username")));
            }
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/login");
        }
    }

}