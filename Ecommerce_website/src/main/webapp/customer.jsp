<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ecommerce Website</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <style>
        .container {
            max-width: 600px;
        }
        .btn-primary, .btn-danger {
            width: 100%;
            margin-bottom: 10px;
        }
        .text-center {
            margin-top: 20px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <%
        String name = (String) request.getAttribute("urname");
        boolean loginStatus = (request.getAttribute("login_status") != null) && (boolean) request.getAttribute("login_status");
        if (loginStatus) {
        %>
        <div class="text-center">
            <h3>Welcome Customer, <%= name %></h3>
            <a href="ViewProductServlet" class="btn btn-primary">View Products</a>
            <a href="LogoutServlet" class="btn btn-danger">Logout</a>
        </div>
        <% } else { %>
        <div class="text-center">
            <h3>Login Failed</h3>
            <a href="login.html" class="btn btn-primary">Try Again</a>
        </div>
        <% } %>
    </div>
</body>
</html>
