package ru.akirakozov.sd.refactoring.util;

import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HtmlUtil {
    public static void formProductResponse(HttpServletResponse response, String annotationString, Product product) throws IOException {
        List<String> responseBody = new ArrayList<>(Collections.singletonList(annotationString));
        if (product != null) {
            responseBody.add(product.toString());
        }
        formHtmlResponse(response, responseBody);
    }

    public static void formHtmlResponse(HttpServletResponse response, List<String> body) throws IOException {
        response.getWriter().println("<html><body>");
        for (String responseString : body) {
            response.getWriter().println(responseString);
        }
        response.getWriter().println("</body></html>");
    }
}
