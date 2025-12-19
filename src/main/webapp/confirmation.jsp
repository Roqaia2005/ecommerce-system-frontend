<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Order Confirmation</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<h1>Order Status</h1>

<% Boolean success = (Boolean) request.getAttribute("orderSuccess"); %>

<% if (success != null && success) { %>
<p>✅ Your order has been placed successfully!</p>
<p>Order ID: <b><%= request.getAttribute("orderId") %></b></p>
<% } else { %>
<p>❌ Failed to place order.</p>
<p>Error: <%= request.getAttribute("error") %></p>
<% } %>

<a href="products">Back to Products</a>

</body>
</html>
