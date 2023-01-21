package ru.akirakozov.sd.refactoring.dao.impl;

import ru.akirakozov.sd.refactoring.dao.ProductRepository;
import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    public ProductRepositoryImpl() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Exception while initializing database: " + e.getMessage());
        }
    }

    @Override
    public List<Product> findAll() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
            List<Product> productList = new ArrayList<>();

            while (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                productList.add(new Product(name, price));
            }
            rs.close();
            stmt.close();
            return productList;
        } catch (SQLException e) {
            throw new RuntimeException("Exception while getting products from database: " + e.getMessage());
        }
    }

    @Override
    public void addProduct(Product product) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Exception while getting product list from database: " + e.getMessage());
        }
    }

    @Override
    public Product findProductWithMaxPrice() {

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");

            Product productWithMaxPrice = null;
            while (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                productWithMaxPrice = new Product(name, price);
            }
            rs.close();
            stmt.close();
            return productWithMaxPrice;
        } catch (SQLException e) {
            throw new RuntimeException("Exception while executing query 'max': " + e.getMessage());
        }
    }

    @Override
    public Product findProductWithMinPrice() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

            Product productWithMaxPrice = null;
            while (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                productWithMaxPrice = new Product(name, price);
            }
            rs.close();
            stmt.close();
            return productWithMaxPrice;
        } catch (SQLException e) {
            throw new RuntimeException("Exception while executing query 'min': " + e.getMessage());
        }
    }

    @Override
    public long getProductSum() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
            long result;
            if (rs.next()) {
                result = rs.getLong(1);
            } else {
                throw new SQLException("Query did not return result");
            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Exception while executing query 'sum': " + e.getMessage());
        }
    }

    @Override
    public long getProductCount() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
            long result;
            if (rs.next()) {
                result = rs.getLong(1);
            } else {
                throw new SQLException("Query did not return result");
            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Exception while executing query 'count': " + e.getMessage());
        }
    }
}
