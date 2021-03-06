package com.example.railyatri.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.railyatri.Adapter.TicketAdapter;
import com.example.railyatri.Model.TicketListModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.BottomOffsetDecoration;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentShowTicket extends Fragment {

    private View mFragmentView;
    private Activity activity;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe_to_refresh;
    private ApiInterface apiInterface;
    private SharedPref pref;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.fragment_show_ticket, null);
            init(mFragmentView);
        }
        return mFragmentView;
    }

    public void init(View view) {

        activity = getActivity();
        pref = new SharedPref(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);

        swipe_to_refresh = view.findViewById(R.id.swipe_to_refresh);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new BottomOffsetDecoration(150));

        swipe_to_refresh.setOnRefreshListener(this::getTicketDetails);
        getTicketDetails();
    }

    private void getTicketDetails() {
        if (Connectivity.getInstance(activity).isOnline()) {
            getTicket(pref.getID(), pref.getTYPE());
        } else {
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getTicket(String userId, String type) {

        swipe_to_refresh.setRefreshing(true);

        Call<TicketListModel> call = apiInterface.getTicket(userId, type);
        call.enqueue(new Callback<TicketListModel>() {
            public void onResponse(@NonNull Call<TicketListModel> call, @NonNull Response<TicketListModel> response) {

                swipe_to_refresh.setRefreshing(false);
                try {
                    assert response.body() != null;
                    TicketAdapter adapter = new TicketAdapter(activity, response.body().getData());
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TicketListModel> call, @NonNull Throwable t) {
                call.cancel();
                swipe_to_refresh.setRefreshing(false);
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}