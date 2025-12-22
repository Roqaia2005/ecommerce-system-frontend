<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>

<html>
<head>
    <title>Product Catalog</title>
    <link rel="stylesheet" href="style.css?v=1.0">
</head>
<body>

<h2>Available Products</h2>

<a href="profile?customer_id=1">Profile</a> |
<a href="orderHistory?customer_id=1">Orders History</a>

<hr>

<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<div style="color: red; font-weight: bold; margin-bottom: 15px;">
    ERROR: <%= error %>
</div>
<%
    }
%>

<form action="prepareOrder" method="post">

    <%
        String productsJson = (String) request.getAttribute("productsJson");

        if (productsJson != null) {
            JSONObject responseObj = new JSONObject(productsJson);  // ✅ Parse as object first
            JSONArray products = responseObj.getJSONArray("products");  // ✅ Extract array

            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                int quantityAvailable = product.getInt("quantity_available");

                if (quantityAvailable > 0) {
    %>
    <div style="margin-bottom: 15px;">
        <b><%= product.getString("product_name") %></b><br>
        Price: <%= product.getDouble("unit_price") %> EGP<br>
        Available: <%= quantityAvailable %><br>

        Quantity:
        <input type="number"
               name="quantity_<%= product.getInt("product_id") %>"
               min="0"
               max="<%= quantityAvailable %>"
               value="0">

        <input type="hidden"
               name="product_id"
               value="<%= product.getInt("product_id") %>">
    </div>
    <%
                }
            }
        }
    %>

    <hr>

    <button type="submit">Make Order</button>

</form>

</body>
</html>