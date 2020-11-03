package com.example.railyatri.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Model.PassListModel;
import com.example.railyatri.R;
import com.example.railyatri.Utils.Utils;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.railyatri.Utils.Const.URL;

public class PassAdapter extends RecyclerView.Adapter<PassAdapter.PassViewHolder> {

    private Context context;
    private List<PassListModel.PassModel> pass;

    public PassAdapter(Context context, List<PassListModel.PassModel> pass) {
        this.context = context;
        this.pass = pass;
    }

    public class PassViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_age, tv_gender, tv_from_station, tv_to_station, tv_validity, tv_price, tv_type, tv_status;
        private ShapeableImageView iv_photo;
        private ImageView iv_token_qr;

        public PassViewHolder(View view) {
            super(view);

            iv_photo = view.findViewById(R.id.iv_photo);
            iv_token_qr = view.findViewById(R.id.iv_token_qr);
            tv_name = view.findViewById(R.id.tv_name);
            tv_age = view.findViewById(R.id.tv_age);
            tv_gender = view.findViewById(R.id.tv_gender);
            tv_from_station = view.findViewById(R.id.tv_from_station);
            tv_to_station = view.findViewById(R.id.tv_to_station);
            tv_validity = view.findViewById(R.id.tv_validity);
            tv_price = view.findViewById(R.id.tv_price);
            tv_type = view.findViewById(R.id.tv_type);
            tv_status = view.findViewById(R.id.tv_status);

        }
    }

    @NonNull
    public PassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_pass, parent, false);
        return new PassViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(PassViewHolder holder, int position) {

        PassListModel.PassModel passInfo = pass.get(position);

        Picasso.get().load(URL + passInfo.getPhoto()).into(holder.iv_photo);

        if ((!passInfo.getToken().equals("")) && passInfo.getStatus().equals("1")) {
            holder.iv_token_qr.setImageBitmap(Utils.generateQRCode(passInfo.getToken()));
        }

        holder.tv_name.setText(passInfo.getName());
        holder.tv_age.setText("Age: " + passInfo.getAge());
        holder.tv_gender.setText(passInfo.getGender());

        if (passInfo.getDuration().equalsIgnoreCase("1 Month")) {
            try {
                String fromDate = Utils.getTimeStamp(passInfo.getCreated());
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Date date = format.parse(fromDate);
                Calendar cal = Calendar.getInstance();
                assert date != null;
                cal.setTime(date);
                cal.add(Calendar.MONTH, 1);
                String nDate = format.format(cal.getTime());
                holder.tv_validity.setText("Valid From " + Utils.getTimeStamp(passInfo.getCreated()) + " to " + nDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String fromDate = Utils.getTimeStamp(passInfo.getCreated());
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Date date = format.parse(fromDate);
                Calendar cal = Calendar.getInstance();
                assert date != null;
                cal.setTime(date);
                cal.add(Calendar.MONTH, 3);
                String nDate = format.format(cal.getTime());
                holder.tv_validity.setText("Valid From " + Utils.getTimeStamp(passInfo.getCreated()) + " to " + nDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.tv_from_station.setText(passInfo.getFrom());
        holder.tv_to_station.setText(passInfo.getTo());
        holder.tv_price.setText("₹ " + passInfo.getPayment());
        holder.tv_type.setText(passInfo.getPassType());

        switch (passInfo.getStatus()) {
            case "0":
                holder.tv_status.setText("Pending");
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.sign_up));
                break;
            case "1":
                holder.tv_status.setText("Approved");
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case "2":
                holder.tv_status.setText("Rejected");
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.red));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (pass == null) {
            return 0;
        }
        return pass.size();
    }
}

