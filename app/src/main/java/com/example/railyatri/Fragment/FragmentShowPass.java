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

import com.example.railyatri.Adapter.PassAdapter;
import com.example.railyatri.Model.PassListModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.BottomOffsetDecoration;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentShowPass extends Fragment {

    private View mFragmentView;
    private Activity activity;
    private SwipeRefreshLayout swipe_to_refresh;
    private RecyclerView recyclerView;
    private ApiInterface apiInterface;
    private SharedPref pref;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.fragment_show_pass, null);
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

        swipe_to_refresh.setOnRefreshListener(this::getPassDetails);
        getPassDetails();
    }

    private void getPassDetails() {
        swipe_to_refresh.setRefreshing(true);
        if (Connectivity.getInstance(activity).isOnline()) {
            getPass(pref.getID(), pref.getTYPE());
        } else {
            swipe_to_refresh.setRefreshing(false);
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPass(String userId, String type) {

//        swipe_to_refresh.setRefreshing(true);

        Call<PassListModel> call = apiInterface.getPass(userId, type);
        call.enqueue(new Callback<PassListModel>() {
            public void onResponse(@NonNull Call<PassListModel> call, @NonNull Response<PassListModel> response) {

                swipe_to_refresh.setRefreshing(false);
                try {
                    assert response.body() != null;
                    PassAdapter adapter = new PassAdapter(activity, response.body().getData());
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PassListModel> call, @NonNull Throwable t) {
                swipe_to_refresh.setRefreshing(false);
                call.cancel();
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}