package com.example.livo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME= "LIVO.db";
    private static final String TAG = "Database";
    private FirebaseFirestore firestore;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys=ON;");

        // Create userLogin_data table
        db.execSQL("CREATE TABLE userLogin_data (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "password TEXT)");

        // Create userData table with foreign key reference to userLogin_data(email)
        String createTableQuery = "CREATE TABLE userData (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "address TEXT, " +
                "profile_pic BLOB, " +
                "contact TEXT, " +
                "email TEXT, " +
                "CONSTRAINT fk_email FOREIGN KEY (email) REFERENCES userLogin_data(email) ON DELETE CASCADE" +
                ")";
        db.execSQL(createTableQuery);

        // Create orders table with foreign key reference to userLogin_data(user_id)
        String createTableQuery2 = "CREATE TABLE orders (" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "total_price DOUBLE, " +
                "order_status TEXT, " +
                "created_at TEXT, " +
                "CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES userLogin_data(user_id) ON DELETE CASCADE" +
                ")";
        db.execSQL(createTableQuery2);

        // Create order_items table with foreign key reference to orders(order_id)
        String createTableQuery3 = "CREATE TABLE order_items (" +
                "order_item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER, " +
                "product_id INTEGER, " +
                "product_name TEXT, " +
                "quantity INTEGER, " +
                "price DOUBLE, " +
                "created_at TEXT, " +
                "CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE" +
                ")";
        db.execSQL(createTableQuery3);
    }

    // Fetch product data from Firebase and insert into SQLite
//    public void fetchProductsFromFirebaseAndInsert(final String companyId) {
//        firestore.collection("companyData")
//                .document(companyId)
//                .collection("products")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            String productId = document.getId();
//                            String productName = document.getString("name");
//                            double price = document.getDouble("price");
//
//                            // Insert product into SQLite
//                            insertProductIntoSQLite(productId, productName, price);
//                        }
//                    } else {
//                        Log.d(TAG, "Error getting products: ", task.getException());
//                    }
//                });
//    }

    // Method to insert product into SQLite
//    private void insertProductIntoSQLite(String productId, String productName, double price) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("product_id", productId);
//        contentValues.put("product_name", productName);
//        contentValues.put("price", price);
//
//        long result = db.insert("order_items", null, contentValues);
//        if (result == -1) {
//            Log.d("SQLiteError", "Failed to insert data");
//        } else {
//            Log.d("SQLiteSuccess", "Data inserted successfully");
//        }
//        db.close();
//    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //userLogin_data TABLE OPERATIONS
    public boolean insertData(String email, String password){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = db.insert("userLogin_data", null, contentValues);
        return result != -1;
    }
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userLogin_data WHERE email = ?", new String[]{email});
        return cursor.getCount() > 0;
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userLogin_data WHERE email = ? AND password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    //userData TABLE OPERATIONS
    public boolean insertUserdata(String name, String address, Bitmap image, String contact, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Check if the email exists in the userLogin_data table
            Cursor cursor = db.rawQuery("SELECT * FROM userLogin_data WHERE email = ?", new String[]{email});
            if (cursor.getCount() == 0) {
                // Email does not exist in userLogin_data table, cannot insert into userData
                Log.e(TAG, "Email does not exist in userLogin_data table");
                return false;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] imageInBytes = outputStream.toByteArray();

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("address", address);
            values.put("profile_pic", imageInBytes);
            values.put("contact", contact);
            values.put("email", email); // Insert the email value into userData, which is a foreign key

            long result = db.insert("userData", null, values);
            if (result != -1) {
                Log.d(TAG, "Data inserted successfully");
                return true;
            } else {
                Log.e(TAG, "Error inserting user data");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inserting user data: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserData(String email, String name, String address, String contact, Bitmap newProfilePic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Convert the new profile picture (Bitmap) to byte array
        if (newProfilePic != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            newProfilePic.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] imageInBytes = outputStream.toByteArray();
            values.put("profile_pic", imageInBytes); // Add the new profile picture to the values
        }

        // Add the other fields to be updated
        values.put("name", name);
        values.put("address", address);
        values.put("contact", contact);

        // Update the user data based on the email (foreign key)
        int result = db.update("userData", values, "email=?", new String[]{email});

        return result > 0; // returns true if the update was successful
    }

    public Cursor getUserData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM userData WHERE email = ?", new String[]{email});
    }

    public boolean updateProfileImage(int userId, byte[] profileImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("profile_pic", profileImage);  // Insert the byte array into the BLOB column

        // Update the user's profile picture where the user ID matches
        int rowsAffected = db.update("userData", values, "user_id = ?", new String[]{String.valueOf(userId)});

        return rowsAffected > 0;  // Return true if the update was successful
    }


    //order TABLE OPERATIONS
    public boolean insertOrders(Double total_price, String order_status, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();

            values.put("total_price", total_price);
            values.put("order_status", order_status);
            values.put("created_at", created_at);

            // Insert the values into the 'orders' table
            long id = db.insert("orders", null, values);
            if (id != -1) {
                Log.d(TAG, "Order inserted successfully");
                return true;
            } else {
                Log.e(TAG, "Error inserting order");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inserting order: " + e.getMessage());
            return false;
        }
    }

    //orderItems TABLE OPERATIONS
    public boolean insertOrderItems(int quantity, String created_at){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();

            values.put("quantity", quantity);
            values.put("created_at", created_at);

            // Insert the values into the 'orders' table
            long id = db.insert("orders", null, values);
            if (id != -1) {
                Log.d(TAG, "Order inserted successfully");
                return true;
            } else {
                Log.e(TAG, "Error inserting order");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inserting order: " + e.getMessage());
            return false;
        }
    }
}
