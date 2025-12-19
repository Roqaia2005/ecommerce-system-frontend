package org.example.ecommerce_system_frontend;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final String PRODUCT_URL = "http://localhost:5002/api/products/";

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
                    .uri(URI.create(PRODUCT_URL + productId))
                    .GET()
                    .build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            JSONObject resJson = new JSONObject(res.body());

            if (!resJson.getString("status").equals("success")) {
                request.setAttribute("error", "Product not found");
                request.getRequestDispatcher("/products").forward(request, response);
                return;
            }

            JSONObject product = resJson.getJSONObject("product");
            request.setAttribute("product", product);
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
