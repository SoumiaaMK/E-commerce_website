<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="view.Product" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product List</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <style>
        .container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
        }

        .pdt-card {
            width: calc(25% - 20px);
            margin: 10px;
            border: 2px solid grey;
            padding: 15px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            background-color: white;
        }

        .pdt-img {
            width: 100%;
            height: 200px;
            overflow: hidden;
            margin-bottom: 10px;
        }

        .pdt-img img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .price {
            font-size: 24px;
        }

        .price::before {
            content: "Rs.";
        }

        .name {
            font-size: 22px;
            font-weight: bold;
            color: black;
            margin-bottom: 10px;
        }

        .detail {
            overflow: hidden;
            max-height: 60px;
            transition: max-height 0.3s ease;
            margin-bottom: 10px;
        }

        .read-more {
            display: block;
            cursor: pointer;
            color: blue;
            margin-bottom: 10px;
        }

        .logout-link {
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 10px 20px;
            background-color: #ff3333;
            color: white;
            border-radius: 5px;
            font-weight: bold;
            cursor: pointer;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="container mt-5">
    <% 
        List<Product> products = (List<Product>) request.getAttribute("products");
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
    %>
    <div class="pdt-card">
        <div class="name"><%= product.getName() %></div>
        <div class="code"><%= product.getCode() %></div>
        <div class="pdt-img">
            <img src="<%= product.getImgPath() %>" alt="<%= product.getName() %>">
        </div>
        <div class="detail">
            <%= product.getDetail() %>
        </div>
        <div class="read-more" onclick="toggleDetail(this)">Read More</div>
        <div class="price"><%= product.getPrice() %></div>
        <form action="AddtoCartServlet" method="post">
            <input type="hidden" name="pid" value="<%= product.getPid() %>">
            <div class="text-center">
                <button class="btn btn-warning">Add to Cart</button>
            </div>
        </form>
    </div>
    <% 
            }
        } else { 
    %>
    <div class="text-center">
        <p>No products available.</p>
    </div>
    <% } %>
</div>

<div class="logout-link" onclick="window.location.href='LogoutServlet'">Logout</div>

<script>
    function toggleDetail(button) {
        var detail = button.previousElementSibling;
        if (detail.style.maxHeight) {
            detail.style.maxHeight = null;
            button.innerHTML = 'Read More';
        } else {
            detail.style.maxHeight = detail.scrollHeight + 'px';
            button.innerHTML = 'Read Less';
        }
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>
