package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductRepository {
    List<Product> findAll() throws SQLException;

    void addProduct(Product product) throws SQLException;

    Product findProductWithMinPrice() throws SQLException;

    Product findProductWithMaxPrice() throws SQLException;

    long getProductSum() throws SQLException;

    long getProductCount() throws SQLException;
}
