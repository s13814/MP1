package com.example.mp1.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 1)
public abstract class ProductDB extends RoomDatabase {

    public abstract ProductDAO productDao();

    private static volatile ProductDB INSTANCE;

    public static ProductDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProductDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProductDB.class, "word_database")
                            //.addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
