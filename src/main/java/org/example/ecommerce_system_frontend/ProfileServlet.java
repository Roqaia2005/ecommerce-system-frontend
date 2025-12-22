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

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

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

        HttpRequest customerRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5004/api/customers/" + customerId))
                .GET()
                .build();

        try {
            HttpResponse<String> customerResponse =
                    client.send(customerRequest, HttpResponse.BodyHandlers.ofString());

            JSONObject customerObj = new JSONObject(customerResponse.body());
            JSONObject customer = customerObj.getJSONObject("customer");

            request.setAttribute("customer", customer.toString());
            request.getRequestDispatcher("profile.jsp").forward(request, response);

        } catch (InterruptedException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Customer service not available");
        }
    }
}