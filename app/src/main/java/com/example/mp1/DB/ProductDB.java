package com.example.mp1.DB;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Product.class}, version = 1)
public abstract class ProductDB extends RoomDatabase {

    public abstract ProductDAO productDao();

    private static volatile ProductDB INSTANCE;

    /*private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback(){

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db){
                super.onOpen(db);
            }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>{
        private final ProductDAO dao;

        PopulateDbAsync(ProductDB db){
            dao = db.productDao();
        }

        @Override
        protected Void doInBackground(final Void... params){
            Product p = new Product(1, "pro1", 10, false);
            dao.insert(p);
            return null;
        }
    }*/

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

    /*public void initDB(){
        new PopulateDbAsync(INSTANCE).execute();
    }*/
}
