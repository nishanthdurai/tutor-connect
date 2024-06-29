package com.tutorconnect.app.adapter.parent;

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

import com.tutorconnect.app.model.StudentParent;
import com.tutorconnect.app.R;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.viewHolder>{

    private final Context mContext;
    private final List<StudentParent> mList;
    private ProgressDialog progressDialog;

    public StudentAdapter(Context mContext, List<StudentParent> mList) {
        this.mContext = mContext;
        this.mList = mList;
        progressDialog = new ProgressDialog(mContext);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.student_layout_for_parent, parent, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        StudentParent model = mList.get(position);
        holder.tv_studentName.setText(model.getStudentName());
        holder.tv_studentSubject.setText(model.getStudentSubject());
        holder.tv_todayDate.setText(model.getDate());
        holder.tv_present.setText(model.getPresent());
        holder.tv_remarks.setText(model.getRemarks());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_studentName;
        private final TextView tv_studentSubject;
        private final TextView tv_todayDate;
        private final TextView tv_present;
        private final TextView tv_remarks;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_studentName = itemView.findViewById(R.id.tv_studentName);
            tv_studentSubject = itemView.findViewById(R.id.tv_studentSubject);
            tv_todayDate = itemView.findViewById(R.id.tv_todayDate);
            tv_present = itemView.findViewById(R.id.tv_present);
            tv_remarks = itemView.findViewById(R.id.tv_remarks);
        }
    }

}
