package com.example.railyatri.Adapter;

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
import com.example.railyatri.Model.CoolieOrderListModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.View.CustomViewHolder;
import com.example.railyatri.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.railyatri.Utils.Const.URL;

public class CoolieOrderHistoryAdapter extends RecyclerView.Adapter<CoolieOrderHistoryAdapter.CoolieOrderHistoryViewHolder> {

    private Activity activity;
    private List<CoolieOrderListModel.CoolieOrderModel> list;
    private ApiInterface apiInterface;
    private Utils utils;

    public CoolieOrderHistoryAdapter(Activity activity, List<CoolieOrderListModel.CoolieOrderModel> list) {
        this.activity = activity;
        this.list = list;
    }

    public class CoolieOrderHistoryViewHolder extends CustomViewHolder {

        private TextView tv_name, tv_contact, tv_station, tv_place, tv_time, tv_status;
        private Button btn_cancel;
        private CircleImageView img_icon;
        private RelativeLayout layout_status;
        private CardView layout_main;

        public CoolieOrderHistoryViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_station = view.findViewById(R.id.tv_station);
            tv_place = view.findViewById(R.id.tv_place);
            tv_time = view.findViewById(R.id.tv_time);
            tv_contact = view.findViewById(R.id.tv_contact);
            tv_status = view.findViewById(R.id.tv_status);
            btn_cancel = view.findViewById(R.id.btn_cancel);
            img_icon = view.findViewById(R.id.img_icon);
            layout_status = view.findViewById(R.id.layout_status);
            layout_main = view.findViewById(R.id.layout_main);
        }
    }

    @NonNull
    @Override
    public CoolieOrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.row_coolie_order_history, parent, false);
        return new CoolieOrderHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CoolieOrderHistoryViewHolder holder, final int position) {
        if (list.get(position).getPhoto().equals("")) {
            holder.img_icon.setImageResource(R.drawable.people);
        } else {
            Picasso.get().load(URL + list.get(position).getPhoto()).into(holder.img_icon);
        }
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_contact.setText(list.get(position).getContact());
        holder.tv_station.setText(list.get(position).getStation());
        holder.tv_place.setText(list.get(position).getPlace());
        holder.tv_time.setText(Utils.getTimeStamp(list.get(position).getCreated()));

        switch (list.get(position).getStatus()) {
            case "1":
                holder.tv_status.setText("Completed");
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.green));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.GONE);
                break;
            case "2":
                holder.tv_status.setText("Canceled");
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.red));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.GONE);
                break;
            default:
                holder.btn_cancel.setVisibility(View.VISIBLE);
                holder.tv_status.setVisibility(View.GONE);
                break;
        }

        holder.btn_cancel.setOnClickListener(view -> cancelDialog(position));

        if (list.get(position).isExpanded) {
            holder.layout_status.setVisibility(View.VISIBLE);
        } else {
            holder.layout_status.setVisibility(View.GONE);
        }

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

    private void cancelDialog(final int position) {

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
                cancelOrder(list.get(position).getId(), "2", position);
            } else {
                Toast.makeText(activity, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

        btn_no.setOnClickListener(view -> dialogActivate.dismiss());
    }

    public void cancelOrder(String id, final String status, final int position) {

        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.updateCoolieOrderStatus(id, status);
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    list.get(position).setStatus(status);
                    notifyDataSetChanged();
                    Toast.makeText(activity, "Canceled Successfully", Toast.LENGTH_LONG).show();
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