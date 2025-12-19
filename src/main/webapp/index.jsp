<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<%
    List<Map<String, Object>> productsList =
            (List<Map<String, Object>>) request.getAttribute("productsList");
%>

<html>
<head>
    <title>Products</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<h1>🛒 Products</h1>

<% if (productsList != null && !productsList.isEmpty()) { %>
<div class="products-grid">
    <% for (Map<String, Object> product : productsList) {
        int quantity = (int) product.get("quantity");
    %>
    <div class="product-card">
        <div><b><%= product.get("name") %></b></div>
        <div>Price: $<%= product.get("price") %></div>
        <div>Stock: <%= quantity %></div>
        <form action="checkout" method="get">
            <input type="hidden" name="product_id" value="<%= product.get("id") %>">
            <button <%= quantity == 0 ? "disabled" : "" %>>Buy</button>
        </form>
    </div>
    <% } %>
</div>
<% } else { %>
<p>No products available</p>
<% } %>

</body>
</html>
