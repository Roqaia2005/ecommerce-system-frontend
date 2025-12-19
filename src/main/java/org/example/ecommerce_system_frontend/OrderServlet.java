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

@WebServlet("/submitOrder")
public class OrderServlet extends HttpServlet {

    private static final String ORDER_URL = "http://localhost:5001/api/orders/create";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String productId = request.getParameter("product_id");
            String quantity = request.getParameter("quantity");

            JSONObject product = new JSONObject();
            product.put("product_id", Integer.parseInt(productId));
            product.put("quantity", Integer.parseInt(quantity));

            JSONArray products = new JSONArray();
            products.put(product);

            JSONObject payload = new JSONObject();
            payload.put("customer_id", 1); // example customer_id
            payload.put("products", products);
            payload.put("total_amount", Double.parseDouble(request.getParameter("total_price")));

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(ORDER_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            JSONObject orderRes = new JSONObject(res.body());

            request.setAttribute("orderSuccess", orderRes.getString("status").equals("success"));
            request.setAttribute("orderId", orderRes.optJSONObject("order") != null ? orderRes.getJSONObject("order").getString("order_id") : null);
            request.getRequestDispatcher("/confirmation.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("orderSuccess", false);
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/confirmation.jsp").forward(request, response);
        }
    }
}
