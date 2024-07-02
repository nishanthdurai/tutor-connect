//package com.example.androidprojecttutor;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewParent;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//
//public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
//
//    private final Context mContext;
//    private List<studenttutor> studentList;
//    private final String tutorId;
//    private final ProgressDialog progressDialog;
//
//    public StudentAdapter(Context mContext, List<studenttutor> studentList, String tutorEmail, String tutorId) {
//        this.mContext = mContext;
//        this.studentList = studentList;
//        this.tutorId = tutorId;
//        progressDialog = new ProgressDialog(mContext);
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.layoutstudent, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        studenttutor model = studentList.get(position);
//        holder.tv_studentName.setText(model.getName());
//        holder.tv_studentSubject.setText(model.getSubject());
//
//        holder.btn_view_parents.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ViewParent.class);
//                intent.putExtra("tutorId", tutorId);
//                intent.putExtra("studentId", model.getId());
//                intent.putExtra("studentName", model.getName());
//                mContext.startActivity(intent);
//            }
//        });
//    }
//
//    private void markAsPresent(studenttutor model, String date) {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String userId = firebaseUser.getUid();
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(tutorsignup.ATTENDANCE).child(model.getName()).child(date);
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("id", userId);
//        hashMap.put("studentName", model.getName());
//        hashMap.put("email", model.getEmail());
//        hashMap.put("studentSubject", model.getSubject());
//
//        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                progressDialog.dismiss();
//                if (task.isSuccessful()) {
//                    Toast.makeText(mContext, "Successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return studentList.size();
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView tv_studentName;
//        private TextView tv_studentSubject;
//        private Button btn_view_parents;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tv_studentName = itemView.findViewById(R.id.tv_studentName);
//            tv_studentSubject = itemView.findViewById(R.id.tv_studentSubject);
//            btn_view_parents = itemView.findViewById(R.id.btn_view_parents);
//        }
//    }
//}
