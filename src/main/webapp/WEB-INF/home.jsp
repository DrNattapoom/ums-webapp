<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>

    <head>
        <title>
            UMS Webapp
        </title>
        <!-- CSS only -->
        <link href = "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
              rel = "stylesheet"
              integrity = "sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
              crossorigin = "anonymous">
        <!-- JavaScript Bundle with Popper -->
        <script src = "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
                crossorigin="anonymous"></script>
        <link href = "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel = "stylesheet" >
    </head>

    <body>

        <div class = "container">
            <div class = "navbar navbar-light bg-light">
                <div class = "container-fluid">
                    <a class = "navbar-brand">SSC UMS-Webapp</a>
                    <a class = "btn btn-light pull-right" type = "button" href = "/logout">
                        <i class = "fa fa-sign-out"></i> &nbsp; Logout
                    </a>
                </div>
            </div>
            <h3 class = "my-4"> Welcome, ${username} </h3>
            <table class = "table table-dark table-striped table-bordered">
                <thead>
                <tr>
                    <th class = "py-3">ID</th>
                    <th class = "py-3">Username</th>
                    <th class = "py-3">Display Name</th>
                    <th class = "py-3">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var = "user" items = "${users}">
                    <tr>
                        <td class = "py-3">${user.id}</td>
                        <td class = "py-3">${user.username}</td>
                        <td class = "py-3">${user.displayName}</td>
                        <td class = "align-middle">
                            <button class = "btn btn-warning btn-sm" type = "button"><i class = "fa fa-pencil"></i></button>
                            <button class = "btn btn-danger btn-sm" type = "button"><i class = "fa fa-trash"></i></button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

    </body>
</html>
