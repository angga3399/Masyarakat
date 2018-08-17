package unikom.skripsi.angga.masyarakat.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Response;
import unikom.skripsi.angga.masyarakat.R;
import unikom.skripsi.angga.masyarakat.activity.ShowNotificationActivity;
import unikom.skripsi.angga.masyarakat.adapter.HistoryAdapter;
import unikom.skripsi.angga.masyarakat.model.HistoryModel;
import unikom.skripsi.angga.masyarakat.service.APIClient;
import unikom.skripsi.angga.masyarakat.service.APIServices;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements HistoryAdapter.Listener {
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.history_notif);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        historyAdapter = new HistoryAdapter(getActivity(),new ArrayList<HistoryModel>(), this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));
        recyclerView.setAdapter(historyAdapter);

        loadHistory();
    }

    private void loadHistory() {
        retrofit2.Call<HistoryModel.HistoryDataModel> api = APIClient.getClient().create(APIServices.class).getHistory();
        api.enqueue(new Callback<HistoryModel.HistoryDataModel>() {
            @Override
            public void onResponse(retrofit2.Call<HistoryModel.HistoryDataModel> call, Response<HistoryModel.HistoryDataModel> response) {
                if (response.isSuccessful()){
                    if(response.body().getResults() != null) {
                        historyAdapter.replaceData(response.body().getResults());
                        recyclerView.scrollToPosition(response.body().getResults().size() - 1);
                    }else{
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<HistoryModel.HistoryDataModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void clickItem(String title, String timestamp, String message, String image, String lat, String lng) {
        Intent intent = new Intent(getActivity(), ShowNotificationActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("timestamp", timestamp);
        intent.putExtra("message", message);
        intent.putExtra("image", image);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        startActivity(intent);
    }
}
