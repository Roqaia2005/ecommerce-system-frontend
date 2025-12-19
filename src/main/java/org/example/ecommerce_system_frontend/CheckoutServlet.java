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
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final String INVENTORY_URL =
            "http://localhost:5002/api/products/check/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String productId = request.getParameter("product_id");

        if (productId == null) {
            response.sendRedirect("products");
            return;
        }

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(INVENTORY_URL + productId))
                    .GET()
                    .build();

            HttpResponse<String> res =
                    client.send(req, HttpResponse.BodyHandlers.ofString());

            JSONObject product = new JSONObject(res.body());
            request.setAttribute("product", product);
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
