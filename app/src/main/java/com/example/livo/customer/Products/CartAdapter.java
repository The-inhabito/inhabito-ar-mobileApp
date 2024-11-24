package com.example.livo.customer.Products;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.livo.R;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder>{
    private List<CartItem> cartItems;
    private TextView totalAmountTextView;

    public CartAdapter(List<CartItem> cartItems, TextView totalAmountTextView) {
        this.cartItems = cartItems;
        this.totalAmountTextView = totalAmountTextView;
        updateTotalAmount();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        // Bind data to views
        holder.productName.setText(cartItem.getName());
        holder.productPrice.setText("Rs " + cartItem.getPrice());
        holder.productImage.setImageBitmap(BitmapFactory.decodeByteArray(cartItem.getImage(), 0, cartItem.getImage().length));
        holder.productQuantity.setText(String.valueOf(cartItem.getQuantity()));

        // Set click listeners
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition(); // Get the latest position
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    cartItems.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(adapterPosition, cartItems.size());
                    updateTotalAmount();
                }
            }
        });

        holder.btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition(); // Get the latest position
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    CartItem currentItem = cartItems.get(adapterPosition);
                    int newQuantity = currentItem.getQuantity() + 1;
                    currentItem.setQuantity(newQuantity);
                    holder.productQuantity.setText(String.valueOf(newQuantity));
                    notifyItemChanged(adapterPosition);
                    updateTotalAmount();
                }
            }
        });

        holder.btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition(); // Get the latest position
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    CartItem currentItem = cartItems.get(adapterPosition);
                    int newQuantity = currentItem.getQuantity() - 1;
                    if (newQuantity > 0) {
                        currentItem.setQuantity(newQuantity);
                        holder.productQuantity.setText(String.valueOf(newQuantity));
                        notifyItemChanged(adapterPosition);
                        updateTotalAmount();
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateCartItems(List<CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        double totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += Double.parseDouble(item.getPrice()) * item.getQuantity();
        }
        totalAmountTextView.setText("Total: Rs " + totalAmount);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public ImageButton btnDecrement;
        public TextView productQuantity;
        public ImageButton btnIncrement;
        public ImageButton btnRemove;

        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            btnDecrement = itemView.findViewById(R.id.btn_decrement);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            btnIncrement = itemView.findViewById(R.id.btn_increment);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }
}
