package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class noteAdapter extends FirestoreRecyclerAdapter<notemodel,noteAdapter.NoteViewHolder> {
    Context context;


    public noteAdapter(@NonNull FirestoreRecyclerOptions<notemodel> options,Context context) {

        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int i, @NonNull notemodel notemodel) {
        holder.titleview.setText(notemodel.title);
        holder.contentview.setText(notemodel.content);
        holder.timeview.setText(utility.timetotimestam(notemodel.timestamp));
        holder.itemView.setOnClickListener((v)->{
            Intent intent=new Intent(context,note_details_activity.class);
            intent.putExtra("title",notemodel.title);
            intent.putExtra("content",notemodel.content);
            String docid=this.getSnapshots().getSnapshot(i).getId();
            intent.putExtra("docid",docid);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_notes_view,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleview,contentview,timeview;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleview=itemView.findViewById(R.id.note_title_view);
            contentview=itemView.findViewById(R.id.note_cntent_view);
            timeview=itemView.findViewById(R.id.note_time_view);
        }
    }
}
