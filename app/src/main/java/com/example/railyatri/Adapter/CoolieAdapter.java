package com.example.railyatri.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Activity.AddCoolieOrderActivity;
import com.example.railyatri.Model.CoolieListModel;
import com.example.railyatri.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.railyatri.Utils.Const.URL;

public class CoolieAdapter extends RecyclerView.Adapter<CoolieAdapter.CoolieViewHolder> {

    private Activity activity;
    private List<CoolieListModel.CoolieModel> list;
    private ArrayList<CoolieListModel.CoolieModel> arrayList;

    public CoolieAdapter(Activity activity, List<CoolieListModel.CoolieModel> list) {
        this.activity = activity;
        this.list = list;
        arrayList = new ArrayList<>();
        arrayList.addAll(list);
    }

    public class CoolieViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_contact, tv_status;
        private LinearLayout layout_main;
        private CircleImageView img_coolie_icon;

        public CoolieViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_contact = view.findViewById(R.id.tv_contact);
            tv_status = view.findViewById(R.id.tv_status);
            layout_main = view.findViewById(R.id.layout_main);
            img_coolie_icon = view.findViewById(R.id.img_icon);
        }
    }

    @NonNull
    @Override
    public CoolieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.row_coolie, parent, false);
        return new CoolieViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CoolieViewHolder holder, final int position) {

        if (list.get(position).getPhoto().equals("")) {
            holder.img_coolie_icon.setImageResource(R.drawable.people);
        } else {
            Picasso.get().load(URL + list.get(position).getPhoto()).into(holder.img_coolie_icon);
        }
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_contact.setText(list.get(position).getContact());

        if (list.get(position).getStatus().equals("1")) {
            holder.tv_status.setText("Available");
            holder.tv_status.setTextColor(activity.getResources().getColor(R.color.green));
        } else {
            holder.tv_status.setText("Not Available");
            holder.tv_status.setTextColor(activity.getResources().getColor(R.color.red));
        }

        holder.layout_main.setOnClickListener(v -> {

            if (list.get(position).getStatus().equals("0")) {
                Toast.makeText(activity, "This coolie is not available", Toast.LENGTH_LONG).show();
            } else {
                Intent i = new Intent(activity, AddCoolieOrderActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                i.putExtra("id", list.get(position).getId());
                i.putExtra("name", list.get(position).getName());
                i.putExtra("contact", list.get(position).getContact());
                i.putExtra("station", list.get(position).getStation());
                i.putExtra("photo", list.get(position).getPhoto());
                Pair<View, String> p1 = Pair.create(holder.img_coolie_icon, "coolieProfile");
                Pair<View, String> p2 = Pair.create(holder.tv_name, "name");
                Pair<View, String> p3 = Pair.create(holder.tv_contact, "contact");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1, p2, p3);
                activity.startActivity(i, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filter(String text) {
        list.clear();
        if (TextUtils.isEmpty(text.trim())) {
            list.addAll(arrayList);
        } else {
            text = text.toLowerCase(Locale.getDefault());
            for (CoolieListModel.CoolieModel item : arrayList) {
                if (item.getStation().toLowerCase(Locale.getDefault()).contains(text)) {
                    list.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}