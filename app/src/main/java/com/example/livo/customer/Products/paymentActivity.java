package com.example.livo.customer.Products;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.livo.customer.CustomerSession;

import com.example.livo.Database;
import com.example.livo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class paymentActivity extends AppCompatActivity {

    private EditText cardNumber, cardExpiry, cardCVC;
    private Button payButton;

    private Database database;
    private ArrayList<CartItem> cartItems; // Received cart items
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardNumber = findViewById(R.id.card_number);
        cardExpiry = findViewById(R.id.card_expiry);
        cardCVC = findViewById(R.id.card_cvc);
        payButton = findViewById(R.id.pay_button);

        database = new Database(this);

        // Retrieve cart data and total amount
        cartItems = getIntent().getParcelableArrayListExtra("cartItems");
        totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);

        payButton.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        String number = cardNumber.getText().toString();
        String expiry = cardExpiry.getText().toString();
        String cvc = cardCVC.getText().toString();

        if (number.isEmpty() || expiry.isEmpty() || cvc.isEmpty()) {
            Toast.makeText(paymentActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Simulate payment process
            boolean paymentSuccessful = Math.random() > 0.2; // 80% success rate
            if (paymentSuccessful) {
                saveOrderToDatabase();
                Toast.makeText(paymentActivity.this, "Payment successful! Order saved.", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                Toast.makeText(paymentActivity.this, "Payment failed! Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveOrderToDatabase() {
        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "No items to save.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get email from session
        String email = CustomerSession.getInstance(this).getEmail();
        if (email == null) {
            Toast.makeText(this, "User is not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get userId from userData table using the email
        int userId = getUserIdFromEmail(email);
        if (userId == -1) {
            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get companyEmail from the first product in the cart (assuming all products in the cart are from the same company)
        String companyEmail = getCompanyEmailFromProduct(cartItems.get(0).getProductId());
        if (companyEmail == null) {
            Toast.makeText(this, "Company not found for the product.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Order status and timestamp
        String orderStatus = "Pending"; // Status can be updated based on payment results
        String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Save order details to database
        boolean isSaved = database.saveOrder(userId, totalAmount, orderStatus, companyEmail, createdAt);

        // If order saved successfully, save order items
        if (isSaved) {
            // Save the individual order items
            for (CartItem item : cartItems) {
                boolean isItemSaved = database.saveOrderItem(item.getProductId(), item.getQuantity(), item.getPrice(), item.getTotalPrice(), createdAt);
                if (!isItemSaved) {
                    Toast.makeText(this, "Failed to save some order items.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(this, "Order and items saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save order.", Toast.LENGTH_SHORT).show();
        }
    }

    private int getUserIdFromEmail(String email) {
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "SELECT user_id FROM userLogin_data WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
            cursor.close();
            return userId;
        }

        if (cursor != null) {
            cursor.close();
        }
        return -1; // Return -1 if no user is found
    }

    public String getCompanyEmailFromProduct(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a reference to the products collection
        db.collection("products")
                .whereEqualTo("product_id", productId) // Query for the product with the given ID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot product = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);

                        // Retrieve the company ID from the product document
                        String companyId = product.getString("company_id");

                        // Now, query the companyData collection using the company ID
                        getCompanyEmailByCompanyId(companyId);
                    } else {
                        // Handle error or empty result
                        Log.e("Firestore", "No product found with the provided productId.");
                    }
                });

        return null;
    }

    // Helper method to get the company email from companyData
    private void getCompanyEmailByCompanyId(String companyId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("companyData")
                .whereEqualTo("company_id", companyId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot company = task.getResult().getDocuments().get(0);
                        String companyEmail = company.getString("company_email");

                        Log.d("Firestore", "Company Email: " + companyEmail);
                    } else {
                        // Handle error or empty result
                        Log.e("Firestore", "No company found with the provided companyId.");
                    }
                });
    }

}
