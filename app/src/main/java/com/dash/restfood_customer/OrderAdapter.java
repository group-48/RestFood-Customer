package com.dash.restfood_customer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dash.restfood_customer.models.Order;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends FirestoreRecyclerAdapter<Order, OrderAdapter.OrderHolder> {

    public OrderAdapter(@NonNull FirestoreRecyclerOptions<Order> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderHolder orderHolder, int i, @NonNull Order order) {
        orderHolder.tv_orderId.setText(order.getStatus());
        Log.d("Order",String.valueOf(order.getTotal()));
        orderHolder.tv_amount.setText(String.valueOf(order.getTotal()));
    }


    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order,parent,false);
        return new OrderHolder(v);
    }

    class OrderHolder extends RecyclerView.ViewHolder{
        //ui elements go here
        TextView tv_orderId;
        TextView tv_amount;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            tv_orderId=itemView.findViewById(R.id.tv_order_id);
            tv_amount=itemView.findViewById(R.id.tv_amount);

        }
    }
}
