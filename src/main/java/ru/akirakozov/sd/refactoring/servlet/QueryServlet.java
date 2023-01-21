package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductRepository;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final ProductRepository productRepository;

    public QueryServlet(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        switch (command) {
            case "max" -> {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Product with max price: </h1>");
                Product product;
                try {
                    product = productRepository.findProductWithMaxPrice();
                } catch (SQLException e) {
                    throw new RuntimeException("Exception while executing query 'max': " + e.getMessage());
                }
                if (product != null) {
                    response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
                }
                response.getWriter().println("</body></html>");
            }
            case "min" -> {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Product with min price: </h1>");
                Product product;
                try {
                    product = productRepository.findProductWithMaxPrice();
                } catch (SQLException e) {
                    throw new RuntimeException("Exception while executing query 'min': " + e.getMessage());
                }
                if (product != null) {
                    response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
                }
                response.getWriter().println("</body></html>");
            }
            case "sum" -> {
                response.getWriter().println("<html><body>");
                response.getWriter().println("Summary price: ");
                try {
                    response.getWriter().println(productRepository.getProductSum());
                } catch (SQLException e) {
                    throw new RuntimeException("Exception while executing query 'sum': " + e.getMessage());
                }
                response.getWriter().println("</body></html>");
            }
            case "count" -> {
                response.getWriter().println("<html><body>");
                response.getWriter().println("Number of products: ");
                try {
                    response.getWriter().println(productRepository.getProductCount());
                } catch (SQLException e) {
                    throw new RuntimeException("Exception while executing query 'count': " + e.getMessage());
                }
                response.getWriter().println("</body></html>");
            }
            default -> response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
