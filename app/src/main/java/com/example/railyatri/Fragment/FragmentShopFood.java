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

import com.example.railyatri.Adapter.ShopFoodAdapter;
import com.example.railyatri.Model.FoodListModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.BottomOffsetDecoration;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentShopFood extends Fragment {

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
            mFragmentView = inflater.inflate(R.layout.fragment_shop_food, null);
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

        swipe_to_refresh.setOnRefreshListener(this::getFoodDetails);
        getFoodDetails();
    }

    private void getFoodDetails() {
        if (Connectivity.getInstance(activity).isOnline()) {
            getFoods(pref.getID());
        } else {
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFoods(String shopId) {

        swipe_to_refresh.setRefreshing(true);

        Call<FoodListModel> call = apiInterface.getShopFood(shopId);
        call.enqueue(new Callback<FoodListModel>() {
            public void onResponse(@NonNull Call<FoodListModel> call, @NonNull Response<FoodListModel> response) {

                swipe_to_refresh.setRefreshing(false);
                try {
                    assert response.body() != null;
                    ShopFoodAdapter adapter = new ShopFoodAdapter(activity, response.body().getData());
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FoodListModel> call, @NonNull Throwable t) {
                call.cancel();
                swipe_to_refresh.setRefreshing(false);
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}