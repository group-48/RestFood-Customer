package com.dash.restfood_customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        reserveHolder.shop_id.setText(reserve.getShopId());
        reserveHolder.booking_date.setText(reserve.getDate());
        reserveHolder.booking_time.setText(reserve.getTime());

    }

    @NonNull
    @Override
    public ReserveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlebooking,parent,false);
        return new ReserveHolder(v) ;
    }


    class ReserveHolder extends RecyclerView.ViewHolder{

        TextView booking_id,shop_id,booking_date,booking_time;

        public ReserveHolder( View itemView) {
            super(itemView);
            booking_id=itemView.findViewById(R.id.tv_booking_id);
            shop_id=itemView.findViewById(R.id.booking_shop_id);
            booking_date=itemView.findViewById(R.id.booking_date);
            booking_time=itemView.findViewById(R.id.booking_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION & listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

        }
    }

    public  interface OnItemClickListener{
        void  onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public  void setOnItemClickListener(ReserveAdapter.OnItemClickListener listener){
        this.listener=listener;
    }



}
