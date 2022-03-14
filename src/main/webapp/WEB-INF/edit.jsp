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
                    <a class = "navbar-brand" href = "/">SSC UMS-Webapp</a>
                    <a class = "btn btn-light pull-right" type = "button" href = "/logout">
                        <i class = "fa fa-sign-out"></i> &nbsp; Logout
                    </a>
                </div>
            </div>
            <c:if test = "${not empty message}">
                <c:choose>
                    <c:when test = "${hasError}">
                        <div class = "alert alert-danger" role = "alert">
                                ${message}
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class = "alert alert-success" role = "alert">
                                ${message}
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <div class = "row justify-content-md-center">
                <div class = "col-sm-12 col-md-6 col-lg-4 mt-5">
                    <h2 class = "text-center mb-4">Edit User (${user.username})</h2>
                    <p>${error}</p>
                    <form action = "/user/edit?username=${user.username}" method = "post" autocomplete = "off">
                        <div class = "input-group mb-4 input-group-md">
                            <span class = "input-group-text" id = "displayName" style = "width: 40px">
                                <i class = "fa fa-user"></i>
                            </span>
                            <input type = "text" class = "form-control" name = "displayName" placeholder = "Display Name" aria-label = "displayName" aria-describedby = "displayName" value = "${displayName}">
                        </div>
                        <div class = "d-grid gap-2">
                            <button class = "btn btn-success" type = "submit">
                                <i class = "fa fa-save"></i> &nbsp; Save
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </body>

</html>