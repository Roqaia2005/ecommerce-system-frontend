<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.json.JSONObject" %>

<%
    JSONObject product = (JSONObject) request.getAttribute("product");
%>

<html>
<head>
    <title>Checkout</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<h1>Checkout</h1>

<% if (product != null) { %>
<p><b>Product:</b> <%= product.getString("name") %></p>
<p><b>Price:</b> $<%= product.getDouble("price") %></p>

<form action="submitOrder" method="post">
    <input type="hidden" name="product_id" value="<%= product.getInt("product_id") %>">
    <input type="hidden" name="total_price" value="<%= product.getDouble("price") %>">
    <label for="quantity">Quantity:</label>
    <input type="number" id="quantity" name="quantity" value="1" min="1" max="<%= product.getInt("quantity") %>" required>
    <button type="submit">Place Order</button>
</form>
<% } else { %>
<p>Product not found!</p>
<% } %>

</body>
</html>
