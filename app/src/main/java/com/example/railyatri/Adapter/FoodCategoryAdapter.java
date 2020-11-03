package com.example.railyatri.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Activity.FoodListActivity;
import com.example.railyatri.Model.FoodCategoryModel;
import com.example.railyatri.R;
import com.example.railyatri.Utils.Const;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.FoodCategoryViewHolder> {

    private List<FoodCategoryModel.FoodCategoryDataModel> categoryList;
    private Activity activity;
    private String shopId;

    public FoodCategoryAdapter(Activity activity, String shopId, List<FoodCategoryModel.FoodCategoryDataModel> categoryList) {
        this.categoryList = categoryList;
        this.activity = activity;
        this.shopId = shopId;
    }

    public class FoodCategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private ImageView img_item;
        private CardView layout_main;

        public FoodCategoryViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            img_item = view.findViewById(R.id.img_item);
            layout_main = view.findViewById(R.id.layout_main);
        }
    }

    @NonNull
    @Override
    public FoodCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food_category, parent, false);
        return new FoodCategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FoodCategoryViewHolder holder, final int position) {

        holder.tv_name.setText(categoryList.get(position).getName());

        Picasso.get().load(Const.URL + categoryList.get(position).getPhoto()).into(holder.img_item);

        holder.layout_main.setOnClickListener(view -> {
            Intent i = new Intent(activity, FoodListActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            i.putExtra("shopId", shopId);
            i.putExtra("categoryId", categoryList.get(position).getId());
            i.putExtra("categoryName", categoryList.get(position).getName());
            activity.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
