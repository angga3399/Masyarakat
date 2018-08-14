package unikom.skripsi.angga.masyarakat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import unikom.skripsi.angga.masyarakat.R;
import unikom.skripsi.angga.masyarakat.model.RekomendasiTempatModel;

public class ShowNotificationAdapter extends RecyclerView.Adapter<ShowNotificationAdapter.ShowNotificationViewHolder> {
    private Context context;
    private List<RekomendasiTempatModel> list;
    private ShowNotificationAdapter.Listener listener;

    public ShowNotificationAdapter(Context context, List<RekomendasiTempatModel> list, Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShowNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_show_notification_rekomendasi, null);
        return new ShowNotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowNotificationViewHolder holder, int position) {
        RekomendasiTempatModel model = list.get(position);
        holder.textViewTempat.setText(model.getTempat());
        holder.textViewLokasi.setText(model.getLokasi());
        holder.textViewJarak.setText(model.getJarakPengungsian() + " km");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ShowNotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTempat;
        private TextView textViewLokasi;
        private TextView textViewJarak;
        private Button buttonDirection;
        public ShowNotificationViewHolder(View itemView) {
            super(itemView);
            textViewJarak = itemView.findViewById(R.id.row_shownotif_jarak);
            textViewLokasi = itemView.findViewById(R.id.row_shownotif_lokasi);
            textViewTempat= itemView.findViewById(R.id.row_shownotif_tempat);
            buttonDirection = itemView.findViewById(R.id.row_shownotif_direction);

            buttonDirection.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.clickDirection(list.get(getAdapterPosition()).getLat(), list.get(getAdapterPosition()).getLng());
        }
    }

    public void replaceData(List<RekomendasiTempatModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public interface Listener{
        void clickDirection(String latitude, String longitude);
    }

}
