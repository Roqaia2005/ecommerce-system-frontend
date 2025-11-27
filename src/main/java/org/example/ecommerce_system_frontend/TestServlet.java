package org.example.ecommerce_system_frontend;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@WebServlet("/test-servlet")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Flask service URL
        String flaskUrl = "http://localhost:5004/";

        URL url = new URL(flaskUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Read Flask response
        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder flaskResponse = new StringBuilder();
        while (scanner.hasNext()) {
            flaskResponse.append(scanner.nextLine());
        }
        scanner.close();

        // Display result in browser
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>Hello from Servlet!</h1>");
        out.println("<p>Flask says: " + flaskResponse + "</p>");
    }
}
