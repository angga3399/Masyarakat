package unikom.skripsi.angga.masyarakat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import unikom.skripsi.angga.masyarakat.R;
import unikom.skripsi.angga.masyarakat.model.HistoryModel;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context context;
    private List<HistoryModel> list;
    private HistoryAdapter.Listener listener;

    public HistoryAdapter(Context context, List<HistoryModel> list, Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_notif, null);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryModel model = list.get(position);
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewTanggal.setText(model.getTimestamp());
        holder.textViewMessage.setText(model.getMessage());
        holder.textViewPos.setText(model.getPos());
        holder.textViewNama.setText(model.getNama());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;
        private TextView textViewTanggal;
        private TextView textViewMessage;
        private TextView textViewPos;
        private TextView textViewNama;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            textViewPos = itemView.findViewById(R.id.row_pos);
            textViewTitle = itemView.findViewById(R.id.row_status);
            textViewTanggal = itemView.findViewById(R.id.row_tanggal);
            textViewMessage = itemView.findViewById(R.id.row_pesan);
            textViewNama = itemView.findViewById(R.id.row_nama);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.clickItem(list.get(getAdapterPosition()).getTitle(),
                    list.get(getAdapterPosition()).getTimestamp(), list.get(getAdapterPosition()).getMessage(),
                    list.get(getAdapterPosition()).getImage_name(),
                    list.get(getAdapterPosition()).getLat(), list.get(getAdapterPosition()).getLng());
        }
    }

    public void replaceData(List<HistoryModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface Listener {
        void clickItem(String title, String timestamp, String message, String image, String lat, String lng);
    }

}
