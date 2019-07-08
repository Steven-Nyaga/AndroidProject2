package com.brok.patapata;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class request_adapter extends RecyclerView.Adapter<request_adapter.MyViewHolder> {
    private static final String TAG = "request_adapter";
    private Button yes;
    private Button no;

    private DatabaseReference mReq;
    LinearLayout parentLayout;
    Context context;
    ArrayList<POJO_requests> requests;

    public request_adapter(Context c, ArrayList<POJO_requests> reqs) {
        context = c;
        requests = reqs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.notification_rows, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.litres.setText(requests.get(position).getLitres());
        holder.Userid.setText(requests.get(position).getUserid());
        holder.parentLayout.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick() {
                //Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, driverMaps.class);
                intent.putExtra("User ID", requests.get(position).getUserid());
                context.startActivity(intent);
            }
        });
        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Toast.makeText(context, "No", Toast.LENGTH_SHORT).show();
                removeItem(position);
                return true;
            }
        });

    }
public void removeItem(int position){
        requests.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, requests.size());
        //notifyDataSetChanged();
          final String userid = requests.get(position).getUserid();
       final String driverid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    mReq = FirebaseDatabase.getInstance().getReference().child("requests");
    mReq.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
               String user_id = snapshot.child("userid").getValue(String.class);
                String driver_id = snapshot.child("driverid").getValue(String.class);
               if(driver_id==driverid) {
                   if (userid==user_id){
                       //push_key=snapshot.getKey();
                       //mReq.child(push_key).removeValue();
                       snapshot.getRef().removeValue();
                   }
               }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView litres;
        TextView Userid;
        LinearLayout parentLayout;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            litres = (TextView) itemView.findViewById(R.id.litres);
            Userid = (TextView) itemView.findViewById(R.id.id);
            parentLayout = itemView.findViewById(R.id.parentLayout);
//yes = (Button) itemView.findViewById(R.id.yes);
//            no= (Button) itemView.findViewById(R.id.no);
        }









}
}
