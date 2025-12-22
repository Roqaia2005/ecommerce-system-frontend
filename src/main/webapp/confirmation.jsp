<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.json.JSONObject" %>

<html>
<head>
    <title>Order Confirmation</title>
    <link rel="stylesheet" href="style.css?v=1.0">
</head>
<body>

<h2>✅ Order Confirmed!</h2>

<%
    String orderJson = (String) request.getAttribute("orderDetails");
    JSONObject order = new JSONObject(orderJson);
%>

<div style="background-color: #e8f5e9; padding: 15px; border-radius: 5px;">
    <p><b>Order ID:</b> <%= order.getString("order_id") %></p>
    <p><b>Customer ID:</b> <%= order.getInt("customer_id") %></p>
    <p><b>Total Amount:</b> <%= order.getDouble("total_amount") %> EGP</p>
    <p><b>Timestamp:</b> <%= order.getString("timestamp") %></p>
</div>

<p>Thank you for your purchase! A confirmation email has been sent.</p>

<hr>

<a href="products">Back to Products</a> |
<a href="profile?customer_id=<%= order.getInt("customer_id") %>">View Profile</a> |
<a href="orderHistory?customer_id=<%= order.getInt("customer_id") %>">View Order History</a>

</body>
</html>