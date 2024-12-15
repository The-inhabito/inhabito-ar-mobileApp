package com.example.livo.admin.adminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.livo.CustomerModel;
import com.example.livo.Database;
import com.example.livo.R;

import java.util.List;

public class AdminViewCustomerFragment extends Fragment {

    private Spinner emailSpinner;
    private TextView nameTextView, addressTextView, contactTextView;
    private Button deleteButton;
    private Database database;
    private List<String> emailList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_view_customer, container, false);

        emailSpinner = view.findViewById(R.id.user_email_spinner);
        nameTextView = view.findViewById(R.id.user_name_text);
        addressTextView = view.findViewById(R.id.user_address_text);
        contactTextView = view.findViewById(R.id.user_contact_text);
        deleteButton = view.findViewById(R.id.delete_user_button);

        database = new Database(getContext());

        loadEmails();

        emailSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedEmail = emailList.get(position);
                loadCustomerDetails(selectedEmail);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                clearCustomerDetails();
            }
        });

        deleteButton.setOnClickListener(v -> {
            String selectedEmail = (String) emailSpinner.getSelectedItem();
            deleteCustomer(selectedEmail);
        });

        return view;
    }

    private void loadEmails() {
        emailList = database.getAllCustomerEmails();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, emailList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emailSpinner.setAdapter(adapter);
    }

    private void loadCustomerDetails(String email) {
        CustomerModel customer = database.getCustomerByEmail(email);
        if (customer != null) {
            nameTextView.setText("Name: " + customer.getName());
            addressTextView.setText("Address: " + customer.getAddress());
            contactTextView.setText("Contact: " + customer.getContact());
        } else {
            clearCustomerDetails();
        }
    }

    private void clearCustomerDetails() {
        nameTextView.setText("Name: ");
        addressTextView.setText("Address: ");
        contactTextView.setText("Contact: ");
    }

    private void deleteCustomer(String email) {
        boolean isDeleted = database.deleteCustomerByEmail(email);
        if (isDeleted) {
            Toast.makeText(getContext(), "Customer deleted successfully!", Toast.LENGTH_SHORT).show();
            loadEmails(); // Refresh spinner
            clearCustomerDetails();
        } else {
            Toast.makeText(getContext(), "Failed to delete customer.", Toast.LENGTH_SHORT).show();
        }
    }
}
