package com.dash.restfood_customer.Interface;

import android.view.View;

public interface ItemClickListener {

    void onclick(View view, int position, boolean isLongClick);

    //void onclick(View view, int adapterPosition);
}
