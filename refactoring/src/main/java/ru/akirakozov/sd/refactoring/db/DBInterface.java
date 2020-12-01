package ru.akirakozov.sd.refactoring.db;

import java.sql.ResultSet;

public interface DBInterface {

    void insertProduct(String name, long price);

    ResultSet selectAllProducts();
    ResultSet selectAllProductsByPriceAsc();
    ResultSet selectAllProductsByPriceDesc();

    long selectProductsPricesSum();
    long selectProductsCount();

}
