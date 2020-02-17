package com.dash.restfood_customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dash.restfood_customer.models.Reserve;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ReserveAdapter extends FirestoreRecyclerAdapter<Reserve,ReserveAdapter.ReserveHolder>{

    private OnItemClickListener listener;

    public ReserveAdapter(@NonNull FirestoreRecyclerOptions<Reserve> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReserveHolder reserveHolder, int i, @NonNull Reserve reserve) {

        reserveHolder.booking_id.setText(reserve.getBookingId());
        reserveHolder.shop_name.setText(reserve.getShopName());
        reserveHolder.booking_date.setText(reserve.getDate());
        reserveHolder.booking_time.setText(reserve.getTime());

    }

    @NonNull
    @Override
    public ReserveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlebooking,parent,false);
        return new ReserveHolder(v) ;
    }

    public void deleteItem(int position){

        getSnapshots().getSnapshot(position).getReference().delete();

    }

    /*public void  onsetItem(int position){
        getSnapshots().getSnapshot(position).getReference().set(10);
    }*/


    class ReserveHolder extends RecyclerView.ViewHolder{

        TextView booking_id,shop_name,booking_date,booking_time;
        Button cancel;

        public ReserveHolder( View itemView) {
            super(itemView);
            booking_id=itemView.findViewById(R.id.tv_booking_id);
            shop_name=itemView.findViewById(R.id.booking_shop_id);
            booking_date=itemView.findViewById(R.id.booking_date);
            booking_time=itemView.findViewById(R.id.booking_time);
            cancel=itemView.findViewById(R.id.btn_cancel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION & listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onDeleteClick(position);
                    }
                }
            });

        }
    }

    public  interface OnItemClickListener{
        void  onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onDeleteClick(int position);
    }

    public  void setOnItemClickListener(ReserveAdapter.OnItemClickListener listener){
        this.listener=listener;
    }



}
