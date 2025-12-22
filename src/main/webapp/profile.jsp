<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.json.JSONObject" %>

<html>
<head>
    <title>Profile</title>
    <link rel="stylesheet" href="style.css?v=1.0">
</head>
<body>

<h2>Customer Profile</h2>

<%
    String customerJson = (String) request.getAttribute("customer");
    if (customerJson != null) {
        JSONObject customer = new JSONObject(customerJson);
%>
<p><b>Name:</b> <%= customer.getString("name") %></p>
<p><b>Email:</b> <%= customer.getString("email") %></p>
<p><b>Phone:</b> <%= customer.getString("phone") %></p>
<p><b>Loyalty Points:</b> <%= customer.getInt("loyalty_points") %></p>
<%
} else {
%>
<p>No customer data found.</p>
<%
    }
%>

<hr>

<a href="products">Back to Products</a> |
<a href="orderHistory?customer_id=<%= new JSONObject(customerJson).getInt("customer_id") %>">Orders History</a>

</body>
</html>