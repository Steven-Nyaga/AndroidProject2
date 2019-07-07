package com.brok.patapata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class report_adapter extends RecyclerView.Adapter<report_adapter.MyViewHolder> {

    Context context;
    ArrayList<user_reports> reports;

    public report_adapter(Context c , ArrayList<user_reports> r){
        context = c;
        reports = r;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.report_rows,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     holder.report.setText(reports.get(position).getReports());
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
TextView report;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            report = (TextView) itemView.findViewById(R.id.actual_report);
        }
    }

}
