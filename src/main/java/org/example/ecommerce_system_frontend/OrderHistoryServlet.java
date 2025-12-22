package org.example.ecommerce_system_frontend;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet("/orderHistory")
public class OrderHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String customerIdStr = request.getParameter("customer_id");

        if (customerIdStr == null || customerIdStr.isEmpty()) {
            response.sendRedirect("products");
            return;
        }

        int customerId = Integer.parseInt(customerIdStr);
        HttpClient client = HttpClient.newHttpClient();
        JSONArray ordersList = new JSONArray();

        HttpRequest customerOrdersRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5004/api/customers/" + customerId + "/orders"))
                .GET()
                .build();

        try {
            HttpResponse<String> customerOrdersResponse =
                    client.send(customerOrdersRequest, HttpResponse.BodyHandlers.ofString());

            JSONObject customerOrders = new JSONObject(customerOrdersResponse.body());
            JSONArray orderArray = customerOrders.getJSONArray("orders");

            for (int i = 0; i < orderArray.length(); i++) {
                JSONObject orderObj = orderArray.getJSONObject(i);
                String orderId = orderObj.getString("order_id");

                HttpRequest orderRequest = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:5001/api/orders/" + orderId))
                        .GET()
                        .build();

                HttpResponse<String> orderResponse =
                        client.send(orderRequest, HttpResponse.BodyHandlers.ofString());

                JSONObject order = new JSONObject(orderResponse.body()).getJSONObject("order");
                ordersList.put(order);
            }

            request.setAttribute("ordersList", ordersList.toString());
            request.getRequestDispatcher("view_orders_history.jsp").forward(request, response);

        } catch (InterruptedException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Service not available");
        }
    }
}