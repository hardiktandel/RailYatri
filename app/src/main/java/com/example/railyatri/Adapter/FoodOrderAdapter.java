package com.example.railyatri.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Model.CommonModel;
import com.example.railyatri.Model.FoodOrderModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.Utils;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.railyatri.Utils.Const.URL;

public class FoodOrderAdapter extends RecyclerView.Adapter<FoodOrderAdapter.FoodOrderViewHolder> {

    private Activity activity;
    private List<FoodOrderModel.FoodOrder> list;
    private ApiInterface apiInterface;
    private Utils utils;

    public FoodOrderAdapter(Activity activity, List<FoodOrderModel.FoodOrder> list) {
        this.activity = activity;
        this.list = list;
    }

    public class FoodOrderViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_food_name, tv_size, tv_user_name, tv_contact, tv_total;
        private TextView tv_qty, tv_date, tv_status;
        private Button btn_cancel;
        private ShapeableImageView img_food_icon;
        private CardView layout_main;
        private RelativeLayout layout_status;

        public FoodOrderViewHolder(View view) {
            super(view);
            tv_food_name = view.findViewById(R.id.tv_food_name);
            tv_size = view.findViewById(R.id.tv_size);
            tv_user_name = view.findViewById(R.id.tv_user_name);
            tv_contact = view.findViewById(R.id.tv_contact);
            tv_total = view.findViewById(R.id.tv_total);
            tv_qty = view.findViewById(R.id.tv_qty);
            tv_date = view.findViewById(R.id.tv_date);
            tv_status = view.findViewById(R.id.tv_status);
            btn_cancel = view.findViewById(R.id.btn_cancel);
            img_food_icon = view.findViewById(R.id.img_food_icon);
            layout_main = view.findViewById(R.id.layout_main);
            layout_status = view.findViewById(R.id.layout_status);
        }
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.row_food_order, parent, false);
        return new FoodOrderViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(FoodOrderViewHolder holder, final int position) {

        Picasso.get().load(URL + list.get(position).getPhoto()).into(holder.img_food_icon);

        holder.tv_food_name.setText(list.get(position).getFoodName());
        holder.tv_user_name.setText(list.get(position).getName());
        holder.tv_contact.setText(list.get(position).getContact());
        holder.tv_total.setText("₹ " + list.get(position).getTotal());
        holder.tv_qty.setText("QTY : " + list.get(position).getQty());
        holder.tv_date.setText(Utils.getTimeStamp(list.get(position).getCreated()));

        if (list.get(position).getCategoryName().equalsIgnoreCase("pizza")) {
            holder.tv_size.setVisibility(View.VISIBLE);
            if (list.get(position).getSize().equals("1")) {
                holder.tv_size.setText("(Small Size)");
            } else if (list.get(position).getSize().equals("2")) {
                holder.tv_size.setText("(Medium Size)");
            } else {
                holder.tv_size.setText("(Large Size)");
            }
        } else {
            holder.tv_size.setVisibility(View.GONE);
        }

        if (list.get(position).getStatus().equals("0")) {
            holder.tv_status.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.VISIBLE);
        } else if (list.get(position).getStatus().equals("1")) {
            holder.tv_status.setVisibility(View.VISIBLE);
            holder.btn_cancel.setVisibility(View.GONE);
            holder.tv_status.setText("Completed");
            holder.tv_status.setTextColor(activity.getResources().getColor(R.color.green));
        } else {
            holder.tv_status.setVisibility(View.VISIBLE);
            holder.btn_cancel.setVisibility(View.GONE);
            holder.tv_status.setText("Cancelled");
            holder.tv_status.setTextColor(activity.getResources().getColor(R.color.red));
        }

        if (list.get(position).isExpanded) {
            holder.layout_status.setVisibility(View.VISIBLE);
        } else {
            holder.layout_status.setVisibility(View.GONE);
        }

        holder.btn_cancel.setOnClickListener(v -> updateDialog(position));

        holder.layout_main.setOnClickListener(v -> {
            if (list.get(position).isExpanded) {
                list.get(position).isExpanded = false;
                Utils.collapseView(holder.layout_status);
            } else {
                list.get(position).isExpanded = true;
                Utils.expandView(holder.layout_status);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void updateDialog(final int position) {

        final Dialog dialogActivate = new Dialog(activity);
        dialogActivate.setContentView(R.layout.dialog_delete);
        Objects.requireNonNull(dialogActivate.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        dialogActivate.getWindow().setGravity(17);
        dialogActivate.show();

        TextView tv_info = dialogActivate.findViewById(R.id.tv_info);
        Button btn_yes = dialogActivate.findViewById(R.id.btn_yes);
        Button btn_no = dialogActivate.findViewById(R.id.btn_no);

        tv_info.setText("Are you sure \nyou want to cancel this order?");

        btn_yes.setOnClickListener(view -> {
            dialogActivate.dismiss();
            if (Connectivity.getInstance(activity).isOnline()) {
                updateOrder(list.get(position).getId(), "2", position);
            } else {
                Toast.makeText(activity, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

        btn_no.setOnClickListener(view -> dialogActivate.dismiss());
    }

    public void updateOrder(String id, final String status, final int position) {

        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.updateFoodOrderStatus(id, status);
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    list.get(position).setStatus(status);
                    notifyDataSetChanged();
                    Toast.makeText(activity, "Update Successfully", Toast.LENGTH_LONG).show();
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