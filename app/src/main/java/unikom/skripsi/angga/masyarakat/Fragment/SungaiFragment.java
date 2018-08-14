package unikom.skripsi.angga.masyarakat.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Callback;
import retrofit2.Response;
import unikom.skripsi.angga.masyarakat.R;
import unikom.skripsi.angga.masyarakat.adapter.HistoryAdapter;
import unikom.skripsi.angga.masyarakat.adapter.SungaiAdapter;
import unikom.skripsi.angga.masyarakat.model.HistoryModel;
import unikom.skripsi.angga.masyarakat.model.SungaiModel;
import unikom.skripsi.angga.masyarakat.service.APIClient;
import unikom.skripsi.angga.masyarakat.service.APIServices;

/**
 * A simple {@link Fragment} subclass.
 */
public class SungaiFragment extends Fragment {
    private RecyclerView recyclerView;
    private SungaiAdapter sungaiAdapter;

    public SungaiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sungai, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list_sungai);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sungaiAdapter = new SungaiAdapter(getActivity(), new ArrayList<SungaiModel>());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(sungaiAdapter);

        loadData();
    }


    private void loadData() {
        retrofit2.Call<SungaiModel.SungaiDataModel> api = APIClient.getClient().create(APIServices.class).getSungai();
        api.enqueue(new Callback<SungaiModel.SungaiDataModel>() {
            @Override
            public void onResponse(retrofit2.Call<SungaiModel.SungaiDataModel> call, Response<SungaiModel.SungaiDataModel> response) {
                if (response.isSuccessful()) {
                    sungaiAdapter.replaceData(response.body().getResults());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<SungaiModel.SungaiDataModel> call, Throwable t) {

            }
        });
    }

    public void searchSungai(String search){
        sungaiAdapter.getFilter().filter(search);
    }
}