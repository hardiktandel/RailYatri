package com.example.railyatri.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Activity.FoodCategoryActivity;
import com.example.railyatri.Model.ShopListModel;
import com.example.railyatri.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.railyatri.Utils.Const.URL;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder> {

    private Activity activity;
    private List<ShopListModel.ShopListDataModel> shopList;
    private ArrayList<ShopListModel.ShopListDataModel> arrayList;

    public ShopListAdapter(Activity activity, List<ShopListModel.ShopListDataModel> shopList) {
        this.shopList = shopList;
        this.activity = activity;
        arrayList = new ArrayList<>();
        arrayList.addAll(shopList);
    }

    public class ShopListViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_address, tv_contact;
        private ShapeableImageView img_shop_icon;
        private CardView layout_main;

        public ShopListViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_address = view.findViewById(R.id.tv_address);
            tv_contact = view.findViewById(R.id.tv_contact);
            img_shop_icon = view.findViewById(R.id.img_shop_icon);
            layout_main = view.findViewById(R.id.layout_main);
        }
    }

    @NonNull
    @Override
    public ShopListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shop_list, parent, false);
        return new ShopListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopListViewHolder holder, final int position) {

        if (shopList.get(position).getPhoto().equals("")) {
            holder.img_shop_icon.setImageResource(R.drawable.restaurant);
        } else {
            Picasso.get().load(URL + shopList.get(position).getPhoto()).into(holder.img_shop_icon);
        }
        holder.tv_name.setText(shopList.get(position).getName());
        holder.tv_address.setText(shopList.get(position).getAddress());
        holder.tv_contact.setText(shopList.get(position).getContact());

        holder.layout_main.setOnClickListener(view -> {
            Intent i = new Intent(activity, FoodCategoryActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            i.putExtra("shopId", shopList.get(position).getId());
            activity.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public void filter(String text) {
        shopList.clear();
        if (TextUtils.isEmpty(text.trim())) {
            shopList.addAll(arrayList);
        } else {
            text = text.toLowerCase(Locale.getDefault());
            for (ShopListModel.ShopListDataModel item : arrayList) {
                if (item.getStation().toLowerCase(Locale.getDefault()).contains(text)) {
                    shopList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
