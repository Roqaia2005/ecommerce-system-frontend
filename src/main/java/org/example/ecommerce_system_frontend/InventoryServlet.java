package org.example.ecommerce_system_frontend;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.*;

@WebServlet("/products")
public class InventoryServlet extends HttpServlet {

    private static final String INVENTORY_URL =
            "http://localhost:5002/api/products";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, Object>> productsList = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(INVENTORY_URL))
                    .GET()
                    .build();

            HttpResponse<String> res =
                    client.send(req, HttpResponse.BodyHandlers.ofString());


            JSONObject json = new JSONObject(res.body());
            JSONArray products = json.getJSONArray("products");

            for (int i = 0; i < products.length(); i++) {
                JSONObject p = products.getJSONObject(i);

                Map<String, Object> product = new HashMap<>();
                product.put("id", p.getInt("product_id"));
                product.put("name", p.getString("name"));
                product.put("price", p.getDouble("price"));
                product.put("quantity", p.getInt("quantity"));

                productsList.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace(); // مهم
            request.setAttribute("error", "Failed to load products");
        }

        request.setAttribute("productsList", productsList);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
