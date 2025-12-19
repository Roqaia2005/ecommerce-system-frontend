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

<p><b>Product:</b> <%= product.getString("name") %></p>
<p><b>Price:</b> $<%= product.getDouble("price") %></p>

<form action="submitOrder" method="post">
    <input type="hidden" name="product_id"
           value="<%= product.getInt("product_id") %>">

    <label>Quantity:</label>
    <input type="number" name="quantity"
           min="1"
           max="<%= product.getInt("quantity") %>"
           required>

    <button type="submit">Confirm Purchase</button>
</form>

</body>
</html>
