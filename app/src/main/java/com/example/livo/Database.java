package com.example.livo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

//import com.example.livo.company.order.OrderModelClass;
import com.example.livo.company.CompanyProductsModel;
import com.example.livo.company.order.OrderModelClass;
import com.example.livo.customer.CustomerOrderModelClass;
import com.example.livo.customer.CustomerSession;

import com.example.livo.customer.ProductModel;
import com.example.livo.customer.Products.CartItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                "user_id INTEGER, " +
                "name TEXT, " +
                "address TEXT, " +
                "profile_pic BLOB, " +
                "contact TEXT, " +
                "CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES userLogin_data(user_id) ON DELETE CASCADE" +
                ")";
        db.execSQL(createTableQuery);

        // Create orders table with foreign key reference to userLogin_data(user_id)
        String createTableQuery2 = "CREATE TABLE orders (" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "total_price DOUBLE, " +
                "order_status TEXT, " +
                "company_email TEXT," +
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

        String createTableQuery4 = "CREATE TABLE products (" +
                "product_id INTEGER PRIMARY KEY, " +
                "companyID TEXT, " +
                "product_name TEXT, " +
                "description TEXT, " +
                "quantity INTEGER, " +
                "price DOUBLE, " +
                "imageUrl TEXT, " +
                "modelUrl TEXT, " +
                "CONSTRAINT fk_companyID FOREIGN KEY (companyID) REFERENCES companies(companyID) ON DELETE CASCADE" +
                ")";
        db.execSQL(createTableQuery4);

        String createTableQuery5 = "CREATE TABLE companies (" +
                "companyID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "companyEmail TEXT, " +
                "password TEXT, " +
                "status TEXT);";
        db.execSQL(createTableQuery5);

        String createTableQuery6 = "CREATE TABLE companyData (" +
                "companyEmail TEXT PRIMARY KEY, " +
                "companyName TEXT, " +
                "companyAddress TEXT, " +
                "companyContactNo TEXT, " +
                "companyID INTEGER," +
                "imageUrl TEXT," +
                "CONSTRAINT fk_companyID FOREIGN KEY (companyID) REFERENCES companies(companyID) ON DELETE CASCADE" +
                ");";
        db.execSQL(createTableQuery6);
        String createTableQuery7 = "CREATE TABLE Reviews ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "company_name TEXT, "+
                "user_id INTEGER, " +
                "review TEXT,"+
                "CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES userLogin_data(user_id) ON DELETE CASCADE" +
                ")";
        db.execSQL(createTableQuery7);



    }
    public boolean insertReview(String companyName, String review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("company_name", companyName);
        contentValues.put("review", review);

        long result = db.insert("Reviews", null, contentValues);
        return result != -1;
    }


    public void fetchProductsFromFirebaseAndInsert(final String companyID) {
        firestore.collection("companyData")
                .document(companyID)
                .collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        SQLiteDatabase db = this.getWritableDatabase(); // Open the database once
                        db.beginTransaction(); // Begin a transaction for batch inserts
                        try {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Extract product fields from Firebase
                                String productId = document.getId();
                                String productName = document.getString("name");
                                double price = document.contains("price") ? document.getDouble("price") : 0.0;
                                String description = document.getString("description");
                                int quantity = document.contains("quantity") ? document.getLong("quantity").intValue() : 0;
                                String imageUrl = document.getString("imageUrl");
                                String modelUrl = document.getString("modelUrl");

                                // Insert product into SQLite
                                insertProductIntoSQLite(db, productId, productName, price, description, quantity, imageUrl, modelUrl);
                            }
                            db.setTransactionSuccessful(); // Mark transaction as successful
                        } catch (Exception e) {
                            Log.e("SQLiteError", "Error inserting products: ", e);
                        } finally {
                            db.endTransaction(); // End the transaction
                            db.close(); // Close the database
                        }
                    } else {
                        Log.d(TAG, "Error getting products: ", task.getException());
                    }
                });
    }

    private void insertProductIntoSQLite(SQLiteDatabase db, String productId, String productName, double price, String description, int quantity, String imageUrl, String modelUrl) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("product_id", productId);
        contentValues.put("product_name", productName);
        contentValues.put("price", price);
        contentValues.put("description", description);
        contentValues.put("quantity", quantity);
        contentValues.put("image_url", imageUrl);
        contentValues.put("model_url", modelUrl);

        // Insert the product data into the products table
        long result = db.insertWithOnConflict("products", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        if (result == -1) {
            Log.d("SQLiteError", "Failed to insert product: " + productId);
        } else {
            Log.d("SQLiteSuccess", "Product inserted successfully: " + productId);
        }
    }

//    public List<ProductModel> getProductsByCompanyId(String companyId) {
//        List<ProductModel> productList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = null;
//
//        try {
//            String query = "SELECT * FROM products WHERE companyID = ?";
//            cursor = db.rawQuery(query, new String[]{companyId});
//
//            if (cursor.moveToFirst()) {
//                do {
//                    String productId = cursor.getString(cursor.getColumnIndexOrThrow("productId"));
//                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
//                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
//                    String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
//                    String modelUrl = cursor.getString(cursor.getColumnIndexOrThrow("modelUrl"));
//                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
//
//                    ProductModel product = new ProductModel(productId, name, price, description, imageUrl, modelUrl,quantity);
//                    productList.add(product);
//                } while (cursor.moveToNext());
//            }
//        } catch (Exception e) {
//            Log.e("DatabaseError", "Error fetching products: ", e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            db.close();
//        }
//
//        return productList;
//    }


    private void insertCompanyIntoSQLite(SQLiteDatabase db, String companyEmail, String companyName, String imageUrl) {
        ContentValues values = new ContentValues();
        values.put("company_id", companyEmail);
        values.put("name", companyName);
        values.put("email", imageUrl);

        long result = db.insertWithOnConflict("companyData", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (result == -1) {
            Log.d("SQLiteError", "Failed to insert company: " + companyName);
        }
    }

    public void fetchCompaniesFromFirebaseAndInsert(final String companyID) {
        firestore.collection("companyData")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        SQLiteDatabase db = this.getWritableDatabase();
                        db.beginTransaction();
                        try {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String companyEmail = document.getString("companyEmail");
                                String companyName = document.getString("companyName");
                                String cImage = document.getString("imageUrl");

                                // Insert into SQLite
                                insertCompanyIntoSQLite(db, companyEmail, companyName, cImage);
                            }
                            db.setTransactionSuccessful();
                        } catch (Exception e) {
                            Log.e(TAG, "Error inserting companies: ", e);
                        } finally {
                            db.endTransaction();
                            db.close();
                        }
                    } else {
                        Log.e(TAG, "Error fetching companies: ", task.getException());
                    }
                });

    }



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
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

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

    public boolean insertUserdata(String name, String address, Bitmap image, String contact, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            // Retrieve email from CustomerSession
            CustomerSession customerSession = CustomerSession.getInstance(context);
            String email = customerSession.getEmail();

            if (email == null || email.isEmpty()) {
                Log.e(TAG, "No email found in session");
                return false; // Email is required for the foreign key check
            }

            // Fetch user_id for the given email
            cursor = db.rawQuery("SELECT user_id FROM userLogin_data WHERE email = ?", new String[]{email});
            if (cursor.moveToFirst()) {
                int userId = cursor.getInt(0); // Get the user_id

                // Compress the Bitmap image
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] imageInBytes = outputStream.toByteArray();

                // Prepare values for insertion
                ContentValues values = new ContentValues();
                values.put("user_id", userId);
                values.put("name", name);
                values.put("address", address);
                values.put("profile_pic", imageInBytes);
                values.put("contact", contact);

                // Insert data into userData table
                long result = db.insert("userData", null, values);
                if (result != -1) {
                    Log.d(TAG, "Data inserted successfully");
                    return true;
                } else {
                    Log.e(TAG, "Error inserting user data");
                    return false;
                }
            } else {
                Log.e(TAG, "Email does not exist in userLogin_data table");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inserting user data: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close(); // Close cursor to avoid memory leaks
            }
        }
    }

    public boolean updateUserData(int userId, Bitmap profileImg, String cusName, String newEmail, String address, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        db.beginTransaction(); // Begin a transaction to ensure atomicity
        try {
            // Update userData table
            ContentValues userDataValues = new ContentValues();

            // Convert Bitmap to byte array if provided
            if (profileImg != null) {
                profileImg.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] imageInBytes = outputStream.toByteArray();
                userDataValues.put("profile_pic", imageInBytes);
            }

            // Add values to update in userData
            if (cusName != null && !cusName.isEmpty()) {
                userDataValues.put("name", cusName);
            }
            if (address != null && !address.isEmpty()) {
                userDataValues.put("address", address);
            }
            if (contact != null && !contact.isEmpty()) {
                userDataValues.put("contact", contact);
            }

            // Update the userData table
            int userDataRowsUpdated = db.update("userData", userDataValues, "user_id = ?", new String[]{String.valueOf(userId)});

            // Update email in userLogin_data table
            if (newEmail != null && !newEmail.isEmpty()) {
                ContentValues userLoginValues = new ContentValues();
                userLoginValues.put("email", newEmail);

                int userLoginRowsUpdated = db.update("userLogin_data", userLoginValues, "user_id = ?", new String[]{String.valueOf(userId)});

                // Check if userLogin_data update failed
                if (userLoginRowsUpdated <= 0) {
                    throw new Exception("Failed to update email in userLogin_data.");
                }
            }

            // Commit the transaction if all updates succeed
            db.setTransactionSuccessful();
            return userDataRowsUpdated > 0;

        } catch (Exception e) {
            Log.e("Database", "Error updating user data: " + e.getMessage());
            return false;
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                Log.e("Database", "Error closing stream: " + e.getMessage());
            }
            db.endTransaction(); // End the transaction
        }
    }

    public Cursor getUserData(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT u.name, u.address, u.profile_pic, u.contact, l.email " +
                        "FROM userData u INNER JOIN userLogin_data l ON u.user_id = l.user_id " +
                        "WHERE u.user_id = ?",
                new String[]{String.valueOf(userId)}
        );
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
    public List<String> getAllCustomerEmails() {
        List<String> emailList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT email FROM userLogin_data";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    emailList.add(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error fetching customer emails: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return emailList;
    }
    public CustomerModel getCustomerByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        CustomerModel customer = null;

        Cursor cursor = null;
        try {
            String query = "SELECT ud.name, ud.address, ud.contact, ud.profile_pic " +
                    "FROM userData ud " +
                    "JOIN userLogin_data ul ON ud.user_id = ul.user_id " +
                    "WHERE ul.email = ?";
            cursor = db.rawQuery(query, new String[]{email});

            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String contact = cursor.getString(cursor.getColumnIndexOrThrow("contact"));
                byte[] profilePicBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("profile_pic"));

                Bitmap profilePic = null;
                if (profilePicBytes != null) {
                    profilePic = BitmapFactory.decodeByteArray(profilePicBytes, 0, profilePicBytes.length);
                }

                customer = new CustomerModel(name, address, contact, email, profilePic);
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error fetching customer details: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return customer;
    }
    public boolean deleteCustomerByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isDeleted = false;

        try {
            // Delete from `userLogin_data` first due to foreign key constraints
            int loginRows = db.delete("userLogin_data", "email = ?", new String[]{email});

            // Then delete from `userData`
            if (loginRows > 0) {
                int userRows = db.delete("userData", "user_id = (SELECT user_id FROM userLogin_data WHERE email = ?)", new String[]{email});
                isDeleted = userRows > 0;
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error deleting customer: ", e);
        } finally {
            db.close();
        }

        return isDeleted;
    }


    public long saveOrder(int userId, double totalPrice, String createdAt, String companyEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", userId); // Add user ID here
        values.put("total_price", totalPrice);
        values.put("order_status", "Pending"); // Default status
        values.put("company_email", companyEmail); // Null initially
        values.put("created_at", createdAt);

        long orderId = db.insert("orders", null, values); // Insert into orders table
        db.close();
        return orderId;
    }


    public void updateOrderWithCompanyEmail(long orderId, String companyEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("company_email", companyEmail);
        db.update("orders", values, "order_id = ?", new String[]{String.valueOf(orderId)});
    }

    // Comapny side db
    public List<OrderModelClass> getOrdersByCompanyEmail(String companyEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<OrderModelClass> orderList = new ArrayList<>();
        Log.d("Database", "Fetching orders for company email: " + companyEmail);

        String query = "SELECT " +
                "o.order_id, o.created_at AS order_date, o.total_price, o.order_status, " +
                "oi.product_id, oi.product_name, oi.quantity, oi.price AS product_price " +
                "FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "WHERE o.company_email = ?";

        Cursor cursor = db.rawQuery(query, new String[]{companyEmail});
        Log.d("Database", "Query executed. Cursor count: " + (cursor != null ? cursor.getCount() : 0));

        if (cursor != null) {
            try {
                Map<String, OrderModelClass> orderMap = new HashMap<>(); // Use String for order_id to match your query
                if (cursor.moveToFirst()) {
                    do {
                        // Retrieve order data
                        @SuppressLint("Range") String orderID = cursor.getString(cursor.getColumnIndex("order_id"));
                        @SuppressLint("Range") String orderDate = cursor.getString(cursor.getColumnIndex("order_date"));
                        @SuppressLint("Range") double totalPrice = cursor.getDouble(cursor.getColumnIndex("total_price"));
                        @SuppressLint("Range") String orderStatus = cursor.getString(cursor.getColumnIndex("order_status"));

                        // Retrieve product data
                        @SuppressLint("Range") String productID = cursor.getString(cursor.getColumnIndex("product_id"));
                        @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("product_name"));
                        @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                        @SuppressLint("Range") double productPrice = cursor.getDouble(cursor.getColumnIndex("product_price"));

                        // Check if order already exists in the map
                        OrderModelClass order = orderMap.get(orderID);
                        if (order == null) {
                            // Create new order if it doesn't exist
                            order = new OrderModelClass(orderID, orderDate, totalPrice, orderStatus);
                            orderMap.put(orderID, order); // Map by orderID
                            Log.d("Database", "Created new order: " + orderID);
                        }

                        // Add product to the existing or newly created order
                        CompanyProductsModel product = new CompanyProductsModel(productID, productName, String.valueOf(quantity), String.valueOf(productPrice));
                        order.getProducts().add(product); // Append to the products list
                        Log.d("Database", "Added product to order: " + productName);

                    } while (cursor.moveToNext());
                }

                // Convert map values to a list of unique orders
                orderList.addAll(orderMap.values());
            } catch (Exception e) {
                Log.e("Database", "Error fetching orders: " + e.getMessage(), e);
            } finally {
                cursor.close();
            }
        }

        Log.d("Database", "Total unique orders fetched: " + orderList.size());
        return orderList;
    }
    public void updateOrderStatus(String orderID, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_status", newStatus);

        // Update the order status where the order_id matches
        db.update("orders", values, "order_id = ?", new String[]{orderID});
        db.close();
    }


    public void saveOrderItem(long orderId, CartItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_id", orderId);
        values.put("product_id", item.getProductId());
        values.put("product_name", item.getProductName());
        values.put("quantity", item.getQuantity());
        values.put("price", item.getPrice());
        values.put("created_at", java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));
        db.insert("order_items", null, values);
    }


    @SuppressLint("Range")
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // Default to -1 if not found

        // Query to fetch user_id based on email
        String query = "SELECT user_id FROM userLogin_data WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("user_id"));
        }

        cursor.close();
        db.close();

        return userId; // Return the found user_id or -1 if not found
    }

    public int getOrderCountByCompanyEmail(String companyEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        int orderCount = 0;  // Default to 0 if no orders found

        // Query to fetch the count of orders for the company based on company_email
        String query = "SELECT COUNT(*) FROM orders WHERE company_email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{companyEmail});

        if (cursor.moveToFirst()) {
            orderCount = cursor.getInt(0);  // Get the count from the first column
        }

        cursor.close();
        db.close();

        return orderCount;  // Return the order count
    }


    @SuppressLint("Range")
    public List<CustomerOrderModelClass> getOrdersByCustomerEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<CustomerOrderModelClass> orderList = new ArrayList<>();
        Log.d("Database", "Fetching orders for customer email: " + email);

        // Step 1: Get the user_id from userLogin_data table based on the email
        String userIdQuery = "SELECT user_id FROM userLogin_data WHERE email = ?";
        Cursor userCursor = db.rawQuery(userIdQuery, new String[]{email});

        String userId = null;
        if (userCursor.moveToFirst()) {
            userId = userCursor.getString(userCursor.getColumnIndex("user_id"));
        }
        userCursor.close();

        if (userId != null) {
            // Step 2: Use the retrieved user_id to fetch orders
            String orderQuery = "SELECT o.order_id, o.created_at, o.order_status, o.total_price " +
                    "FROM orders o WHERE o.user_id = ?";
            Cursor cursor = db.rawQuery(orderQuery, new String[]{userId});

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String orderID = cursor.getString(cursor.getColumnIndex("order_id"));
                    @SuppressLint("Range") String orderDate = cursor.getString(cursor.getColumnIndex("created_at"));
                    @SuppressLint("Range") String orderStatus = cursor.getString(cursor.getColumnIndex("order_status"));
                    @SuppressLint("Range") double totalPrice = cursor.getDouble(cursor.getColumnIndex("total_price"));

                    CustomerOrderModelClass order = new CustomerOrderModelClass(orderID, orderDate, totalPrice, orderStatus);
                    orderList.add(order);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            Log.d("Database", "No user found with the provided email.");
        }

        return orderList;
    }


}
