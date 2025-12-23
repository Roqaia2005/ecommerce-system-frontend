<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>

<html>
<head>
    <title>Orders History</title>
    <link rel="stylesheet" href="style.css?v=1.0">
</head>
<body>

<h2>Your Orders History</h2>

<%
    String ordersJson = (String) request.getAttribute("ordersList");

    if (ordersJson != null) {
        JSONArray orders = new JSONArray(ordersJson);

        if (orders.length() > 0) {
%>
<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>Order ID</th>
        <th>Date</th>
        <th>Total Amount (EGP)</th>
        <th>Products</th>
    </tr>
    <%
        for (int i = 0; i < orders.length(); i++) {
            JSONObject order = orders.getJSONObject(i);
            JSONArray products = order.getJSONArray("products");
    %>
    <tr>
        <td><%= order.getString("order_id") %></td>
        <td><%= order.getString("created_at") %></td>
        <td><%= order.getDouble("total_amount") %></td>
        <td>
            <ul>
                <%
                    for (int j = 0; j < products.length(); j++) {
                        JSONObject p = products.getJSONObject(j);
                %>
                <li>Product ID: <%= p.getInt("product_id") %>, Quantity: <%= p.getInt("quantity") %></li>
                <%
                    }
                %>
            </ul>
        </td>
    </tr>
    <%
        }
    %>
</table>
<%
} else {
%>
<p>No orders found for this customer.</p>
<%
    }
} else {
%>
<p>Unable to retrieve order history.</p>
<%
    }
%>

<hr>

<a href="products">Back to Products</a> |
<a href="profile?customer_id=1">View Profile</a>

</body>
</html>