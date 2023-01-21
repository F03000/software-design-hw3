package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductRepository;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static ru.akirakozov.sd.refactoring.util.HtmlUtil.formHtmlResponse;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final ProductRepository productRepository;

    public GetProductsServlet(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            formHtmlResponse(response, productRepository.findAll().stream()
                    .map(Product::toString)
                    .toList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
