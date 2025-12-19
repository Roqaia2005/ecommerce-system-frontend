<%@ page contentType="text/html;charset=UTF-8" %>

<%
    Boolean success = (Boolean) request.getAttribute("orderSuccess");
    Integer orderId = (Integer) request.getAttribute("orderId");
    String error = (String) request.getAttribute("error");
%>

<html>
<head>
    <title>Confirmation</title>
    <link rel="stylesheet" href="style.css">

</head>
<body>

<% if (success != null && success) { %>
<h1>Order Confirmed</h1>
<p>Order ID: <%= orderId %></p>
<a href="products">Continue Shopping</a>
<% } else { %>
<h1>Order Failed</h1>
<p><%= error %></p>
<a href="products">Back</a>
<% } %>

</body>
</html>
