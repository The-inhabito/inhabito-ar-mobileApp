package com.example.livo.company;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.livo.Database;
import com.example.livo.R;
import com.example.livo.company.Chart.LowStockAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyChartFragment extends Fragment {

    private TextView tvProductCount, tvOrderCount, tvFeedbackCount;
    private ListView lvLowStock;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference productRef;
    private Database database;  // Initialize this object

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_chart, container, false);

        // Initialize views
        tvProductCount = view.findViewById(R.id.tv_product_count);
        tvOrderCount = view.findViewById(R.id.tv_order_count);
        tvFeedbackCount = view.findViewById(R.id.tv_feedback_count);
        lvLowStock = view.findViewById(R.id.lv_low_stock);

        sessionClass session = sessionClass.getInstance(getActivity());
        String companyId = session.getEmail();

        firebaseDatabase = FirebaseDatabase.getInstance();
        productRef = firebaseDatabase.getReference("products");

        database = new Database(getContext());  // Properly initialize the Database object

        // Fetch data
        fetchProductData(companyId);
        fetchOrderData();
      //  fetchFeedbackData();

        return view;
    }

    private void fetchOrderData() {
        // Retrieve the email (companyID) from the session
        sessionClass session = sessionClass.getInstance(getActivity());
        String companyID = session.getEmail();

        int orderCount = database.getOrderCountByCompanyEmail(companyID);  // Call the method on the initialized database object
        tvOrderCount.setText("Number of Orders: " + orderCount);
    }


    private void fetchProductData(String email) {
        productRef.orderByChild("companyID").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int productCount = 0;
                List<String> lowStockList = new ArrayList<>();

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Map<String, Object> product = (Map<String, Object>) productSnapshot.getValue();
                    if (product != null) {
                        productCount++;
                        int stock = Integer.parseInt(product.get("quantity").toString());
                        if (stock < 2) { // Updated threshold is 2
                            lowStockList.add(product.get("name").toString());
                        }
                    }
                }

                tvProductCount.setText("Number of Products: " + productCount);

                // Populate low stock list
                if (getActivity() != null) {
                    lvLowStock.setAdapter(new LowStockAdapter(getActivity(), lowStockList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error fetching product data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Other methods...
}



