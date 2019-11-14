package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dash.restfood_customer.Interface.ItemClickListener;
import com.dash.restfood_customer.MainActivity;
import com.dash.restfood_customer.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;



    public MenuViewHolder(View itemView){
        super(itemView);

        txtMenuName=(TextView)itemView.findViewById(R.id.tvTitle);
        imageView=(ImageView)itemView.findViewById(R.id.ivImage);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onclick(view,getAdapterPosition(),false);

    }
}
