package com.tutorconnect.app.adapter.tutor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorconnect.app.R;
import com.tutorconnect.app.model.ParentTutor;

import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.viewHolder> {

    private final Context mContext;
    private List<ParentTutor> parentList;

    private final ProgressDialog progressDialog;

    public ParentAdapter(Context mContext, List<ParentTutor> parentList, String tutorId) {
        this.mContext = mContext;
        this.parentList = parentList;
        progressDialog = new ProgressDialog(mContext);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_parent, parent, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ParentTutor model = parentList.get(position);
        holder.tv_parent_name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return parentList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_parent_name;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_parent_name = itemView.findViewById(R.id.tv_parent_name);
        }
    }

}
