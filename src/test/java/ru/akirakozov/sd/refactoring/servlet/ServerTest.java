package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.*;
import ru.akirakozov.sd.refactoring.Main;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@TestMethodOrder(MethodOrderer.MethodName.class)
class ServerTest {
    private static Thread serverThread;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String lineSeparator = System.lineSeparator();

    @BeforeAll
    public static void startServer() {
        serverThread = new Thread(() -> {
            try {
                Main.main(null);
            } catch (Exception e) {
                throw new RuntimeException("Unable to start the server: " + e.getMessage());
            }
        });
        serverThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Exception while initializing server:" + e.getMessage());
        }
    }

    @AfterAll
    public static void stopServer() {
        try {
            serverThread.interrupt();
            serverThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Exception while stopping server thread: " + e.getMessage());
        }
    }

    @Test
    public void test01_initializeTest() {
        testRequestWrappedHtml("get-products", "");
        testRequestWrappedHtml("query?command=sum", "Summary price: " + lineSeparator + "0" + lineSeparator);
        testRequestWrappedHtml("query?command=max", "<h1>Product with max price: </h1>" + lineSeparator);
    }

    @Test
    public void test02_addProductTest() {
        testRequestWrappedHtml("get-products", "");
        testRequest("add-product?name=iphone6&price=30", "OK" + lineSeparator);
        testRequestWrappedHtml("get-products", "iphone6\t30</br>" + lineSeparator);
    }

    @Test
    public void test03_queryTest() {
        testRequestWrappedHtml("query?command=max", "<h1>Product with max price: </h1>" + lineSeparator + "iphone6\t30</br>" + lineSeparator);
        testRequestWrappedHtml("query?command=min", "<h1>Product with min price: </h1>" + lineSeparator + "iphone6\t30</br>" + lineSeparator);
        testRequestWrappedHtml("query?command=sum", "Summary price: " + lineSeparator + "30" + lineSeparator);
        testRequestWrappedHtml("query?command=count", "Number of products: " + lineSeparator + "1" + lineSeparator);
    }

    private void testRequest(String request, String expectedResponseBody) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI("http://localhost:8081/" + request)).build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertEquals(expectedResponseBody, response.body());
        } catch (Exception e) {
            throw new RuntimeException("Exception while sending request to server: " + e.getMessage());
        }
    }

    private void testRequestWrappedHtml(String request, String expectedResponseBody) {
        String newExpectedResponse = "<html><body>" +
                lineSeparator +
                expectedResponseBody +
                "</body></html>" +
                lineSeparator;
        testRequest(request, newExpectedResponse);
    }

}