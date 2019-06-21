package com.a4nesia.baso.smartaccess.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a4nesia.baso.smartaccess.R;
import com.a4nesia.baso.smartaccess.models.Access;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Access> accesses;
    private OnItemClickListener onItemClickListener;

    public HistoryAdapter(Context context, ArrayList<Access> accesses, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.accesses = accesses;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_timeline, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int type;
        if(position == 0){
            if(getItemCount() == 1)
                type = 3;
            else
                type = 1;
        }
        else if(position < getItemCount()-1){
            type = 4;
        }
        else {
            type = 2;
        }
        final Access access = getItem(position);
        ViewHolder viewHolder = new ViewHolder(holder.itemView,type);

        if(access.getSuccessStatus()==1)
            viewHolder.mTimelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.circle_green));
        else
            viewHolder.mTimelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.circle_red));

        viewHolder.txtDate.setText(access.getCreatedAt());
        String html = "Accessed <b>"+access.getRoom().getCode()+" - "+access.getRoom().getName()+"</b>";
        viewHolder.txtDesc.setText(Html.fromHtml(html));
        if(access.getSuccessStatus()==1){
            viewHolder.txtStatus.setText("SUCCESS");
            viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }
        else{
            viewHolder.txtStatus.setText("FAILED");
            viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(access);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accesses.size();
    }
    public Access getItem(int position){
        return accesses.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    public interface OnItemClickListener {
        void onItemClick(Access access);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TimelineView mTimelineView;
        public TextView txtDate;
        public TextView txtDesc;
        public TextView txtStatus;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            mTimelineView = itemView.findViewById(R.id.timeline);
            txtDate = itemView.findViewById(R.id.text_timeline_date);
            txtDesc = itemView.findViewById(R.id.text_timeline_title);
            txtStatus = itemView.findViewById(R.id.text_timeline_status);
            mTimelineView.initLine(viewType);


        }
    }
}
