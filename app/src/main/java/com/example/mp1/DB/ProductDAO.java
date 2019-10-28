package com.example.mp1.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDAO {

    @Query("SELECT * FROM Product")
    LiveData<List<Product>> getAllProducts();

    @Insert
    void insert(Product product);

    @Delete
    void delete(Product product);
}
