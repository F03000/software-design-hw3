package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import static ru.akirakozov.sd.refactoring.util.HtmlUtil.formHtmlResponse;
import static ru.akirakozov.sd.refactoring.util.HtmlUtil.formProductResponse;

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
        try {
            switch (command) {
                case "max" ->
                        formProductResponse(response, "<h1>Product with max price: </h1>", productRepository.findProductWithMaxPrice());
                case "min" ->
                        formProductResponse(response, "<h1>Product with min price: </h1>", productRepository.findProductWithMaxPrice());
                case "sum" ->
                        formHtmlResponse(response, Arrays.asList("Summary price: ", String.valueOf(productRepository.getProductSum())));
                case "count" ->
                        formHtmlResponse(response, Arrays.asList("Number of products: ", String.valueOf(productRepository.getProductCount())));
                default -> response.getWriter().println("Unknown command: " + command);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }



}
