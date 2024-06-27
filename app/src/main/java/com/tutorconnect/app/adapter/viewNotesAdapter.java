package com.tutorconnect.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorconnect.app.Model.NotesModel;
import com.tutorconnect.app.R;

import java.util.ArrayList;

public class viewNotesAdapter extends RecyclerView.Adapter<viewNotesAdapter.viewholder> {
    Context context;
    ArrayList<NotesModel> viewNotes;

    public viewNotesAdapter(Context context, ArrayList<NotesModel> viewNotes) {
        this.context = context;
        this.viewNotes = viewNotes;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_layout, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        NotesModel notes = viewNotes.get(position);
        Log.d("TAG1", "onBindViewHolder: "  + notes);
        holder.textView.setText(notes.getName());
    }

    @Override
    public int getItemCount() {
        return viewNotes.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView textView;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textview);
        }
    }
}
