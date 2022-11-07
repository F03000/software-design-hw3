package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DBUtil;
import ru.akirakozov.sd.refactoring.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        Map<String, Consumer<HttpServletResponse>> commands = Map.of(
                "sum", this::sumCommand,
                "max", this::maxCommand,
                "min", this::minCommand,
                "count", this::countCommand);
        if (commands.containsKey(command)) {
            commands.get(command).accept(response);
        } else {
            unknownCommand(response, command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void maxCommand(HttpServletResponse response) {
        try {
            String query = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
            ResultSet rs = DBUtil.getProductsByQuery(query);
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with max price: </h1>");

            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                response.getWriter().println(name + "\t" + price + "</br>");
            }
            response.getWriter().println("</body></html>");

            rs.close();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void minCommand(HttpServletResponse response) {
        try {
            String query = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
            List<Product> productList = DBUtil.getProductsByQuery(query);

            PrintWriter writer = response.getWriter();

            writer.println("<html><body>");
            writer.println("<h1>Product with min price: </h1>");

            productList.forEach(product -> writer.println(product.getName() + "\t" + product.getPrice() + "</br>");

            writer.println("</body></html>");

        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void sumCommand(HttpServletResponse response) {
        try {
            String query = "SELECT SUM(price) FROM PRODUCT";
            Integer sum = DBUtil.getNumberByQuery(query);
            response.getWriter().println("<html><body>");
            response.getWriter().println("Summary price: ");

            if (sum != null) {
                response.getWriter().println(sum);
            }
            response.getWriter().println("</body></html>");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void countCommand(HttpServletResponse response) {
        try {
            String query = "SELECT COUNT(*) FROM PRODUCT";
            ResultSet rs = DBUtil.getProductsByQuery(query);
            response.getWriter().println("<html><body>");
            response.getWriter().println("Number of products: ");

            if (rs.next()) {
                response.getWriter().println(rs.getInt(1));
            }
            response.getWriter().println("</body></html>");

            rs.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void unknownCommand(HttpServletResponse response, String command) throws IOException {
        response.getWriter().println("Unknown command: " + command);
    }

}
