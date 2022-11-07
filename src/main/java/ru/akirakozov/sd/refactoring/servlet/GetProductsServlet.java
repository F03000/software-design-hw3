package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DBUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String query = "SELECT * FROM PRODUCT";
        try (ResultSet rs = DBUtil.getProductsByQuery(query)) {
            formAnswer(rs, response);

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void formAnswer(ResultSet rs, HttpServletResponse response) throws IOException, SQLException {
        response.getWriter().println("<html><body>");

        while (rs.next()) {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            response.getWriter().println(name + "\t" + price + "</br>");
        }
        response.getWriter().println("</body></html>");
    }
}
