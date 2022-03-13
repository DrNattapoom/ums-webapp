<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <body>

        <h2>Welcome, ${username}</h2>
        <h2>${users}</h2>

        <c:forEach var = "user" items = "${users}">
            Item <c:out value = "${user.username}"/><p>
        </c:forEach>

    </body>
</html>
