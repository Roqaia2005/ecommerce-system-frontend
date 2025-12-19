package org.example.ecommerce_system_frontend;



import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/customers/*")
public class CustomerServlet extends HttpServlet {
    
    private static final String CUSTOMER_SERVICE_URL = "http://localhost:5004/api/customers";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = CUSTOMER_SERVICE_URL;
            
            // Parse URL path
            if (pathInfo != null && !pathInfo.equals("/")) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length >= 2) {
                    String customerId = pathParts[1];
                    url += "/" + customerId;
                    
                    // Check if requesting orders
                    if (pathParts.length >= 3 && pathParts[2].equals("orders")) {
                        url += "/orders";
                    }
                }
            }
            
            HttpRequest customerRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            
            HttpResponse<String> customerResponse = client.send(customerRequest, 
                    HttpResponse.BodyHandlers.ofString());
            
            if (customerResponse.statusCode() == 200) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(customerResponse.body());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject error = new JSONObject();
                error.put("error", "Customer not found");
                response.getWriter().write(error.toString());
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject error = new JSONObject();
            error.put("error", "Service communication interrupted");
            response.getWriter().write(error.toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject error = new JSONObject();
            error.put("error", e.getMessage());
            response.getWriter().write(error.toString());
        }
    }
}