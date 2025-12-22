package org.example.ecommerce_system_frontend;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest flaskRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5002/api/inventory/products"))
                .GET()
                .build();

        try {
            HttpResponse<String> flaskResponse =
                    client.send(flaskRequest, HttpResponse.BodyHandlers.ofString());

            request.setAttribute("productsJson", flaskResponse.body());

            request.getRequestDispatcher("index.jsp")
                    .forward(request, response);

        } catch (InterruptedException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Inventory service not available");
        }
    }
}