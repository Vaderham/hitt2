package com.example.reaganharper.hiittrainer02;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class IntervalAdapter extends RecyclerView.Adapter<IntervalAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView intervalName;
        public TextView intervalTime;

        public ViewHolder(View itemView) {
            super(itemView);
            intervalName = (TextView) itemView.findViewById(R.id.name);
            intervalTime = (TextView) itemView.findViewById(R.id.timeAmount);
        }
    }

    private List<Interval> mIntervals;
    private Context mContext;

    public IntervalAdapter(Context context, List<Interval> intervals){
            mContext = context;
            mIntervals = intervals;
    }

    private Context getmContext(){
        return mContext;
    }

    @Override
    public IntervalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View intervalView = inflater.inflate(R.layout.intervallistitem, parent, false);

        ViewHolder viewholder = new ViewHolder(intervalView);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(IntervalAdapter.ViewHolder viewHolder, int position) {
        Interval interval = mIntervals.get(position);

        TextView nameView = viewHolder.intervalName;
        nameView.setText(interval.getName());

        TextView timeView = viewHolder.intervalTime;
        String convertedTime = convertTime(interval.getIntervalTime());
        timeView.setText(convertedTime);
    }

    public String convertTime(long mills){
        long seconds = (mills / 1000) % 60;
        long minutes = ((mills / (1000 * 60)) % 60);
        String convertedSeconds = String.format("%02d", seconds);
        String convertedMinutes = String.format("%02d", minutes);
        return convertedMinutes + ":" + convertedSeconds;
    }

    @Override
    public int getItemCount() {
        return mIntervals.size();
    }
}
