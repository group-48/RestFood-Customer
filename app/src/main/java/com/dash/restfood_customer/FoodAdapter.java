package com.dash.restfood_customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class FoodAdapter extends FirestoreRecyclerAdapter<Food, FoodAdapter.FoodHolder> {

    private OnItemClickListener listener;


    public FoodAdapter(@NonNull FirestoreRecyclerOptions<Food> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodHolder foodHolder, int i, @NonNull Food food) {
        foodHolder.name.setText(food.getFoodName());
        Picasso.get().load(food.getImage()).into(foodHolder.image);
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);
        return new FoodHolder(v);
    }

    class FoodHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;

        public FoodHolder( View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.food_name);
            image=itemView.findViewById(R.id.food_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }

    }

    public  interface OnItemClickListener{
        void  onItemClick(DocumentSnapshot documentSnapshot,int position);
    }

    public  void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
