package io.muzoo.ssc.webapp;

import io.muzoo.ssc.webapp.servlet.HomeServlet;
import io.muzoo.ssc.webapp.service.SecurityService;
import io.muzoo.ssc.webapp.servlet.LoginServlet;
import io.muzoo.ssc.webapp.servlet.LogoutServlet;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

public class ServletRouter {

    private static final List<Class<? extends Routable>> routables = new ArrayList<>();

    static {
        routables.add(HomeServlet.class);
        routables.add(LoginServlet.class);
        routables.add(LogoutServlet.class);
    }

    private SecurityService securityService;

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public void init(Context ctx) {
        for (Class<? extends Routable> routableClass : routables) {
            try {
                Routable routable = routableClass.newInstance();
                routable.setSecurityService(securityService);
                String name = routable.getClass().getSimpleName();
                Tomcat.addServlet(ctx, name, (HttpServlet) routable);
                ctx.addServletMapping(routable.getMapping(), name);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}