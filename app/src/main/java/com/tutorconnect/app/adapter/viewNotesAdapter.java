package com.tutorconnect.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorconnect.app.PdfViewActivity;
import com.tutorconnect.app.R;
import com.tutorconnect.app.model.Notes;

import java.util.ArrayList;

public class viewNotesAdapter extends RecyclerView.Adapter<viewNotesAdapter.viewholder> {
    Context context;
    ArrayList<Notes> viewNotes;
    Boolean showMarkAsDone;

    public viewNotesAdapter(Context context, ArrayList<Notes> viewNotes, boolean showMarkAsDone) {
        this.context = context;
        this.viewNotes = viewNotes;
        this.showMarkAsDone = showMarkAsDone;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_layout, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Notes notes = viewNotes.get(position);
        Log.d("TAG1", "onBindViewHolder: "  + notes);
        holder.textView.setText(notes.getName());

        // download pdf
        holder.relativeLayout.setOnClickListener((v) -> {
            PdfViewActivity.start(context, notes.getUrl());
        });

        if(showMarkAsDone) {
            holder.doneAction.setVisibility(View.VISIBLE);
        } else {
            holder.doneAction.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return viewNotes.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView doneAction;
        RelativeLayout relativeLayout;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textview);
            doneAction = itemView.findViewById(R.id.btn_markAsDone);
            relativeLayout = itemView.findViewById(R.id.relative_layout);
        }
    }
}
