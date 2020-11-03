package com.example.railyatri.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Activity.AddFoodOrderActivity;
import com.example.railyatri.Model.FoodListModel;
import com.example.railyatri.R;
import com.example.railyatri.Utils.Const;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodListViewHolder> {

    private List<FoodListModel.FoodModel> foodList;
    private Activity activity;
    private String category;
    private String shopId;

    public FoodListAdapter(Activity activity, String category, String shopId, List<FoodListModel.FoodModel> foodList) {
        this.foodList = foodList;
        this.activity = activity;
        this.category = category;
        this.shopId = shopId;
    }

    public class FoodListViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tv_description, tv_price, tv_price2, tv_price3;
        public ShapeableImageView img_item;
        public CardView layout_main;

        public FoodListViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_description = view.findViewById(R.id.tv_description);
            tv_price = view.findViewById(R.id.tv_price);
            tv_price2 = view.findViewById(R.id.tv_price2);
            tv_price3 = view.findViewById(R.id.tv_price3);
            img_item = view.findViewById(R.id.img_item);
            layout_main = view.findViewById(R.id.layout_main);
        }
    }

    @NonNull
    @Override
    public FoodListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_foods, parent, false);
        return new FoodListViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(FoodListViewHolder holder, final int position) {

        Picasso.get().load(Const.URL + foodList.get(position).getPhoto()).into(holder.img_item);

        holder.tv_name.setText(foodList.get(position).getName());
        holder.tv_description.setText(foodList.get(position).getDescription());
        holder.tv_price.setText("₹ " + foodList.get(position).getPrice());

        if (category.equalsIgnoreCase("pizza")) {
            holder.tv_price.setText("S : ₹ " + foodList.get(position).getPrice());
            holder.tv_price2.setText("M : ₹ " + foodList.get(position).getPrice2());
            holder.tv_price3.setText("L : ₹ " + foodList.get(position).getPrice3());
            holder.tv_price2.setVisibility(View.VISIBLE);
            holder.tv_price3.setVisibility(View.VISIBLE);
        } else {
            holder.tv_price2.setVisibility(View.GONE);
            holder.tv_price3.setVisibility(View.GONE);
        }

        holder.layout_main.setOnClickListener(view -> {
            Intent i = new Intent(activity, AddFoodOrderActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            i.putExtra("foodId", foodList.get(position).getId());
            i.putExtra("name", foodList.get(position).getName());
            i.putExtra("description", foodList.get(position).getDescription());
            i.putExtra("price", foodList.get(position).getPrice());
            i.putExtra("price2", foodList.get(position).getPrice2());
            i.putExtra("price3", foodList.get(position).getPrice3());
            i.putExtra("photo", foodList.get(position).getPhoto());
            i.putExtra("category", category);
            i.putExtra("shopId", shopId);
            Pair<View, String> p1 = Pair.create(holder.img_item, "foodImg");
            Pair<View, String> p2 = Pair.create(holder.tv_name, "name");
            Pair<View, String> p3 = Pair.create(holder.tv_description, "description");
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1, p2, p3);
            activity.startActivity(i, options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
