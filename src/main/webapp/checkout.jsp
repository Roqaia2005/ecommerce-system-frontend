<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>

<html>
<head>
    <title>Checkout</title>
    <link rel="stylesheet" href="style.css?v=1.0">

</head>
<body>

<h2>Order Review</h2>

<%
    String pricingJson = (String) request.getAttribute("pricingResult");
    String productsJson = (String) request.getAttribute("products");

    JSONObject pricing = new JSONObject(pricingJson);
    JSONArray items = pricing.getJSONArray("details");
    double total = pricing.getDouble("grand_total");
%>

<table border="1" cellpadding="5">
    <tr>
        <th>Product ID</th>
        <th>Quantity</th>
        <th>Discount</th>
        <th>Subtotal (EGP)</th>
    </tr>

    <%
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
    %>
    <tr>
        <td><%= item.getInt("product_id") %></td>
        <td><%= item.getInt("quantity") %></td>
        <td><%= item.getString("discount") %></td>
        <td><%= item.getDouble("subtotal") %></td>
    </tr>
    <%
        }
    %>
</table>

<h3>Total Amount: <%= total %> EGP</h3>

<form action="confirmOrder" method="post">

    <input type="hidden" name="products" value='<%= productsJson %>'>
    <input type="hidden" name="total_amount" value="<%= total %>">

    <label for="customer_id">Customer ID:</label>
    <input type="number" id="customer_id" name="customer_id" value="1" required>

    <br><br>

    <button type="submit">Confirm Order</button>
    <a href="products">Cancel</a>

</form>

</body>
</html>