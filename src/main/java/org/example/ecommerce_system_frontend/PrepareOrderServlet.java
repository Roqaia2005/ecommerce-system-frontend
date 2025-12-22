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

@WebServlet("/prepareOrder")
public class PrepareOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpClient client = HttpClient.newHttpClient();
        JSONArray selectedProducts = new JSONArray();

        String[] productIds = request.getParameterValues("product_id");

        if (productIds == null) {
            response.sendRedirect("products");
            return;
        }

        for (String productId : productIds) {
            String quantityParam = "quantity_" + productId;
            int quantity = Integer.parseInt(request.getParameter(quantityParam));

            if (quantity > 0) {
                HttpRequest stockRequest = HttpRequest.newBuilder()
                        .uri(URI.create(
                                "http://localhost:5002/api/inventory/check/" + productId))
                        .GET()
                        .build();

                try {
                    HttpResponse<String> stockResponse =
                            client.send(stockRequest, HttpResponse.BodyHandlers.ofString());

                    JSONObject stockJson = new JSONObject(stockResponse.body());
                    JSONObject productInfo = stockJson.getJSONObject("product");
                    int available = productInfo.getInt("quantity_available");

                    if (quantity > available) {
                        request.setAttribute("error",
                                "Not enough stock for product ID " + productId);
                        request.getRequestDispatcher("index.jsp")
                                .forward(request, response);
                        return;
                    }

                    JSONObject product = new JSONObject();
                    product.put("product_id", Integer.parseInt(productId));
                    product.put("quantity", quantity);
                    selectedProducts.put(product);

                } catch (InterruptedException e) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }
            }
        }

        JSONObject pricingPayload = new JSONObject();
        pricingPayload.put("products", selectedProducts);

        HttpRequest pricingRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/pricing/calculate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(pricingPayload.toString()))
                .build();

        try {
            HttpResponse<String> pricingResponse =
                    client.send(pricingRequest, HttpResponse.BodyHandlers.ofString());

            request.setAttribute("pricingResult", pricingResponse.body());
            request.setAttribute("products", selectedProducts.toString());

            request.getRequestDispatcher("checkout.jsp")
                    .forward(request, response);

        } catch (InterruptedException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}