<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Ecommerce Website</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <style>
        body {
            background-color: #f8f9fa;
        }
        .vendor-form {
            background-color: #ffc107;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .vendor-form .form-control, 
        .vendor-form .btn {
            margin-top: 15px;
        }
        .logout-link {
            display: inline-block;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <%
        String name = null;
        boolean loginStatus = false;
        if (request.getAttribute("urname") != null) {
            name = (String) request.getAttribute("urname");
        }
        if (request.getAttribute("login_status") != null) {
            loginStatus = (boolean) request.getAttribute("login_status");
        }
        if (loginStatus) {
        %>
        <div class="mb-4 text-center">
            <h3>Welcome Vendor, <%= name %></h3>
            <div class="d-flex justify-content-center">
                <button class="btn btn-primary me-2" onclick="window.location.href='ViewUploadedServlet?username=<%= name %>'">View Uploads</button>
                <a href="LogoutServlet" class="btn btn-danger">Logout</a>
            </div>
        </div>
        <div class="d-flex justify-content-center align-items-center vh-100">
            <form action="UploadProductServlet?username=<%= name %>" method="post" enctype="multipart/form-data" class="w-50 vendor-form">
                <input class="form-control" type="text" placeholder="Product Name" name="name" required>
                <input class="form-control" type="text" placeholder="Product Price" name="price" required>
                <textarea class="form-control" rows="5" placeholder="Product Details" name="detail" required></textarea>
                <input class="form-control" type="text" placeholder="Product Code" name="code" required>
                <label for="category" class="mt-3">Category</label>
                <select class="form-control" id="category" name="category" required>
                    <option value="electronics">Electronics</option>
                    <option value="home applications">Home Applications</option>
                    <option value="fashion">Fashion</option>
                    <option value="books">Books</option>
                </select>
                <label for="availability" class="mt-3">Availability</label>
                <select class="form-control" id="availability" name="availability" required>
                    <option value="yes">Yes</option>
                    <option value="no">No</option>
                </select>
                <label for="pdt_img" class="mt-3">Product Image</label>
                <input class="form-control" type="file" id="pdt_img" name="pdt_img" accept=".jpg, .jpeg, .png" required>
                <button type="submit" class="btn btn-success w-100">Submit</button>
            </form>
        </div>
        <% } else { %>
        <div class="text-center">
            <p class="text-danger">Login Failed</p>
            <a href="login.html" class="btn btn-primary">Try again</a>
        </div>
        <% } %>
    </div>
</body>
</html>