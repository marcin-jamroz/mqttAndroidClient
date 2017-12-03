package com.example.marcin.mqttclient.supp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marcin.mqttclient.R;

import java.util.List;

/**
 * Created by marcin on 02.12.17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<String> history;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView historyElement;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            historyElement = v.findViewById(R.id.history_element);
        }
    }

    public void add(int position, String item) {
        history.add(position, item);
        notifyItemInserted(position);
    }

    public HistoryAdapter(List<String> dataset) {
        history = dataset;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.history_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        String element = history.get(position);
        holder.historyElement.setText(element);
    }

    @Override
    public int getItemCount() {
        return history.size();
    }
}
