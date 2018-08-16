package unikom.skripsi.angga.masyarakat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import unikom.skripsi.angga.masyarakat.R;
import unikom.skripsi.angga.masyarakat.model.SungaiModel;

public class SungaiAdapter extends RecyclerView.Adapter<SungaiAdapter.SungaiViewHolder> implements Filterable{
    private Context context;
    private List<SungaiModel> list;
    private List<SungaiModel> listTemp;

    public SungaiAdapter(Context context, List<SungaiModel> list) {
        this.context = context;
        this.list = list;
        listTemp = list;
    }

    @NonNull
    @Override
    public SungaiAdapter.SungaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_sungai, null);
        return new SungaiAdapter.SungaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SungaiAdapter.SungaiViewHolder holder, int position) {
        SungaiModel model = listTemp.get(position);
        holder.textViewSungai.setText(model.getSungai());
        holder.textViewTma.setText(model.getTma());
        holder.textViewTanggal.setText(model.getTanggal());
        holder.textViewJam.setText(model.getJam());
    }

    @Override
    public int getItemCount() {
        return listTemp.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()){
                    listTemp = list;
                }else {
                    ArrayList<SungaiModel> filterList = new ArrayList<>();
                    for (SungaiModel sungaiModel : list){
                        if (sungaiModel.getSungai().toLowerCase().contains(charString.toLowerCase())){
                            filterList.add(sungaiModel);
                        }
                    }
                    listTemp = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listTemp;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listTemp = (ArrayList<SungaiModel>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class SungaiViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewSungai;
        private TextView textViewTma;
        private TextView textViewTanggal;
        private TextView textViewJam;
        public SungaiViewHolder(View itemView) {
            super(itemView);
            textViewSungai  = itemView.findViewById(R.id.row_sungai_nama);
            textViewTma     = itemView.findViewById(R.id.row_sungai_tma);
            textViewTanggal = itemView.findViewById(R.id.row_sungai_tanggal);
            textViewJam     = itemView.findViewById(R.id.row_sungai_Jam);
        }
    }

    public void replaceData(List<SungaiModel> list){
        this.list = list;
        listTemp = list;
        notifyDataSetChanged();
    }
}
