package com.example.railyatri.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Activity.AddFoodActivity;
import com.example.railyatri.Model.CommonModel;
import com.example.railyatri.Model.FoodListModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.railyatri.Utils.Const.URL;

public class ShopFoodAdapter extends RecyclerView.Adapter<ShopFoodAdapter.ShopFoodViewHolder> {

    private Activity activity;
    private List<FoodListModel.FoodModel> list;
    private ApiInterface apiInterface;
    private Utils utils;

    public ShopFoodAdapter(Activity activity, List<FoodListModel.FoodModel> list) {
        this.activity = activity;
        this.list = list;
    }

    public class ShopFoodViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_description, tv_price, tv_price2, tv_price3;
        private TextView tv_category, tv_delete, tv_edit;
        private CircleImageView img_food_icon;
        private LinearLayout layout_edit;
        private CardView layout_main;

        public ShopFoodViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_description = view.findViewById(R.id.tv_description);
            tv_price = view.findViewById(R.id.tv_price);
            tv_price2 = view.findViewById(R.id.tv_price2);
            tv_price3 = view.findViewById(R.id.tv_price3);
            tv_category = view.findViewById(R.id.tv_category);
            tv_edit = view.findViewById(R.id.tv_edit);
            tv_delete = view.findViewById(R.id.tv_delete);
            img_food_icon = view.findViewById(R.id.img_food_icon);
            layout_main = view.findViewById(R.id.layout_main);
            layout_edit = view.findViewById(R.id.layout_edit);
        }
    }

    @NonNull
    @Override
    public ShopFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.row_shop_food, parent, false);
        return new ShopFoodViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ShopFoodViewHolder holder, final int position) {

        Picasso.get().load(URL + list.get(position).getPhoto()).into(holder.img_food_icon);

        holder.tv_name.setText(list.get(position).getName());
        holder.tv_description.setText(list.get(position).getDescription());

        if (list.get(position).getCategoryId().equalsIgnoreCase("1")) {
            holder.tv_price.setText("S: ₹ " + list.get(position).getPrice());
            holder.tv_price2.setText("M: ₹ " + list.get(position).getPrice2());
            holder.tv_price3.setText("L: ₹ " + list.get(position).getPrice3());
            holder.tv_category.setText(activity.getResources().getStringArray(R.array.food_category)[0]);
        } else {
            holder.tv_price.setText("₹ " + list.get(position).getPrice());
            holder.tv_price2.setText("");
            holder.tv_price3.setText("");
            switch (list.get(position).getCategoryId()) {
                case "2":
                    holder.tv_category.setText(activity.getResources().getStringArray(R.array.food_category)[1]);
                    break;
                case "3":
                    holder.tv_category.setText(activity.getResources().getStringArray(R.array.food_category)[2]);
                    break;
                case "4":
                    holder.tv_category.setText(activity.getResources().getStringArray(R.array.food_category)[3]);
                    break;
                case "5":
                    holder.tv_category.setText(activity.getResources().getStringArray(R.array.food_category)[4]);
                    break;
                default:
                    holder.tv_category.setText(activity.getResources().getStringArray(R.array.food_category)[5]);
                    break;
            }
        }

        holder.tv_edit.setOnClickListener(v -> {
            Intent i = new Intent(activity, AddFoodActivity.class);
            i.putExtra("id", list.get(position).getId());
            i.putExtra("name", list.get(position).getName());
            i.putExtra("description", list.get(position).getDescription());
            i.putExtra("price", list.get(position).getPrice());
            i.putExtra("price2", list.get(position).getPrice2());
            i.putExtra("price3", list.get(position).getPrice3());
            i.putExtra("category", list.get(position).getCategoryId());
            i.putExtra("photo", list.get(position).getPhoto());
            i.putExtra("update", true);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.img_food_icon, "foodImg");
            activity.startActivity(i, options.toBundle());
        });

        holder.tv_delete.setOnClickListener(v -> deleteDialog(position));

        if (list.get(position).isExpanded) {
            holder.layout_edit.setVisibility(View.VISIBLE);
        } else {
            holder.layout_edit.setVisibility(View.GONE);
        }

        holder.layout_main.setOnClickListener(v -> {
            if (list.get(position).isExpanded) {
                list.get(position).isExpanded = false;
                Utils.collapseView(holder.layout_edit);
            } else {
                list.get(position).isExpanded = true;
                Utils.expandView(holder.layout_edit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void deleteDialog(final int position) {

        final Dialog dialogActivate = new Dialog(activity);
        dialogActivate.setContentView(R.layout.dialog_delete);
        Objects.requireNonNull(dialogActivate.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        dialogActivate.getWindow().setGravity(17);
        dialogActivate.show();

        Button btn_yes = dialogActivate.findViewById(R.id.btn_yes);
        Button btn_no = dialogActivate.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(view -> {
            dialogActivate.dismiss();
            if (Connectivity.getInstance(activity).isOnline()) {
                deleteFood(list.get(position).getId(), position);
            } else {
                Toast.makeText(activity, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

        btn_no.setOnClickListener(view -> dialogActivate.dismiss());
    }

    private void deleteFood(String id, final int position) {

        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.deleteFood(id);
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    Toast.makeText(activity, "Deleted Successfully", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                call.cancel();
                utils.stopProgressDialog();
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}