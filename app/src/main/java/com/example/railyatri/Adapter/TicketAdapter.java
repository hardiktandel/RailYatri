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

import com.example.railyatri.Model.TicketListModel;
import com.example.railyatri.R;
import com.example.railyatri.Utils.Utils;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private Context context;
    private List<TicketListModel.TicketModel> tickets;

    public TicketAdapter(Context context, List<TicketListModel.TicketModel> tickets) {

        this.context = context;
        this.tickets = tickets;
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_to, tv_from, tv_price, tv_date, tv_qty, tv_type;
        private ImageView iv_token_qr;

        public TicketViewHolder(View view) {
            super(view);

            tv_to = view.findViewById(R.id.tv_to);
            tv_from = view.findViewById(R.id.tv_from);
            tv_date = view.findViewById(R.id.tv_date);
            tv_price = view.findViewById(R.id.tv_price);
            tv_qty = view.findViewById(R.id.tv_qty);
            tv_type = view.findViewById(R.id.tv_type);
            iv_token_qr = view.findViewById(R.id.iv_token_qr);
        }
    }

    @NonNull
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_ticket, parent, false);
        return new TicketViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(TicketViewHolder holder, final int position) {

        holder.tv_from.setText(tickets.get(position).getFrom());
        holder.tv_to.setText(tickets.get(position).getTo());
        holder.tv_date.setText(Utils.getTimeStamp(tickets.get(position).getCreated()));
        holder.tv_price.setText("₹ " + tickets.get(position).getTotal());
        holder.tv_qty.setText("No of Passenger: " + tickets.get(position).getQty());
        holder.tv_type.setText(tickets.get(position).getType());
        if (!tickets.get(position).getToken().isEmpty()) {
            holder.iv_token_qr.setImageBitmap(Utils.generateQRCode(tickets.get(position).getToken()));
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}
