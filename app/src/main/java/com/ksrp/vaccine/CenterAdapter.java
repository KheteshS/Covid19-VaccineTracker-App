package com.ksrp.vaccine;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ksrp.vaccine.api.CenterData;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterViewHolder> {
    private Context context;
    private List<CenterData> list;

    public CenterAdapter(Context context, List<CenterData> list) {
        this.context = context;
        this.list = list;
    }

    public CenterAdapter(List<CenterData> centerList) {
    }

    @NonNull
    @NotNull
    @Override
    public CenterAdapter.CenterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.center_rv_item, parent, false);
        return new CenterAdapter.CenterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CenterAdapter.CenterViewHolder holder, int position) {

        CenterData data = list.get(position);
        holder.centerNameTV.setText(data.getCenterName());
        holder.centerAddressTV.setText(data.getCenterAddress());
        holder.centerTimingTV.setText("From : "+data.getCenterFromTime()+" To : "+data.getCenterToTime());
        holder.vaccineNameTV.setText(data.getVaccineName());
        holder.vaccineFeesTV.setText(data.getFeeType());
        holder.ageLimitTV.setText("Age Limit : "+data.getAgeLimit());
        holder.availabilityTV.setText("Availability : "+data.getAvailableCapacity());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CenterViewHolder extends RecyclerView.ViewHolder {

        private TextView centerNameTV;
        private TextView centerAddressTV;
        private TextView centerTimingTV;
        private TextView vaccineNameTV;
        private TextView vaccineFeesTV;
        private TextView ageLimitTV;
        private TextView availabilityTV;

        public CenterViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            centerNameTV = itemView.findViewById(R.id.idTVCenterName);
            centerAddressTV = itemView.findViewById(R.id.idTVCenterLocation);
            centerTimingTV = itemView.findViewById(R.id.idTVCenterTimings);
            vaccineNameTV = itemView.findViewById(R.id.idTVVaccineName);
            vaccineFeesTV = itemView.findViewById(R.id.idTVVaccineFees);
            ageLimitTV = itemView.findViewById(R.id.idTVAgeLimit);
            availabilityTV = itemView.findViewById(R.id.idTVAvailability);


        }
    }

    public void filterList(List<CenterData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }
}
