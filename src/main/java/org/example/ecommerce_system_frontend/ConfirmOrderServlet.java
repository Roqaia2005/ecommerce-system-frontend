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

@WebServlet("/confirmOrder")
public class ConfirmOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpClient client = HttpClient.newHttpClient();

        String customerIdStr = request.getParameter("customer_id");
        String productsJson = request.getParameter("products");
        String totalAmountStr = request.getParameter("total_amount");

        int customerId = Integer.parseInt(customerIdStr);
        double totalAmount = Double.parseDouble(totalAmountStr);
        JSONArray products = new JSONArray(productsJson);

        // Step 1: Create Order
        JSONObject orderPayload = new JSONObject();
        orderPayload.put("customer_id", customerId);
        orderPayload.put("products", products);
        orderPayload.put("total_amount", totalAmount);

        HttpRequest orderRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5001/api/orders/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(orderPayload.toString()))
                .build();

        try {
            HttpResponse<String> orderResponse =
                    client.send(orderRequest, HttpResponse.BodyHandlers.ofString());


            JSONObject orderResult = new JSONObject(orderResponse.body());
            String orderId = orderResult.getString("order_id");

            // Step 2: Update Inventory (CRITICAL - WAS MISSING)
            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);

                JSONObject inventoryPayload = new JSONObject();
                inventoryPayload.put("product_id", product.getInt("product_id"));
                inventoryPayload.put("quantity", product.getInt("quantity"));

                HttpRequest inventoryRequest = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:5002/api/inventory/update"))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(inventoryPayload.toString()))
                        .build();

                HttpResponse<String> inventoryResponse =
                        client.send(inventoryRequest, HttpResponse.BodyHandlers.ofString());

                if (inventoryResponse.statusCode() != 200) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Failed to update inventory for product " + product.getInt("product_id"));
                    return;
                }
            }

            // Step 3: Update Loyalty Points
            JSONObject loyaltyPayload = new JSONObject();
            loyaltyPayload.put("points", (int) (totalAmount / 10));

            HttpRequest loyaltyRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5004/api/customers/" + customerId + "/loyalty"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(loyaltyPayload.toString()))
                    .build();

            client.send(loyaltyRequest, HttpResponse.BodyHandlers.ofString());

            // Step 4: Send Notification
            JSONObject notificationPayload = new JSONObject();
            notificationPayload.put("order_id", orderId);

            HttpRequest notificationRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5005/api/notifications/send"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(notificationPayload.toString()))
                    .build();

            client.send(notificationRequest, HttpResponse.BodyHandlers.ofString());

            request.setAttribute("orderDetails", orderResult.toString());
            request.getRequestDispatcher("confirmation.jsp").forward(request, response);

        } catch (InterruptedException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to complete order");
        }
    }
}