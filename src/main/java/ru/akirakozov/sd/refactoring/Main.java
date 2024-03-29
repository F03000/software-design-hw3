package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.dao.ProductRepository;
import ru.akirakozov.sd.refactoring.dao.impl.ProductRepositoryImpl;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {


        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ProductRepository productRepository = new ProductRepositoryImpl();

        context.addServlet(new ServletHolder(new AddProductServlet(productRepository)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(productRepository)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(productRepository)),"/query");

        server.start();
        server.join();
    }
}
