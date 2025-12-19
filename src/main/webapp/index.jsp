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

<div class="container">
    <h1>🛒 Products</h1>

    <div class="products-grid">
        <% if (productsList != null && !productsList.isEmpty()) {
            for (Map<String, Object> product : productsList) {
                int quantity = (int) product.get("quantity");
        %>
        <div class="product-card">
            <div><%= product.get("name") %></div>
            <div>$<%= product.get("price") %></div>

            <form action="checkout" method="get">
                <input type="hidden" name="product_id"
                       value="<%= product.get("id") %>">
                <button <%= quantity == 0 ? "disabled" : "" %>>
                    Buy
                </button>
            </form>
        </div>
        <% } } else { %>
        <p>No products available</p>
        <% } %>
    </div>
</div>

</body>
</html>
