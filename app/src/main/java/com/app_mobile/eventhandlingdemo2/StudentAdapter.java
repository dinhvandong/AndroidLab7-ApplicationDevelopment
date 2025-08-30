package com.app_mobile.eventhandlingdemo2;
import com.app_mobile.eventhandlingdemo2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private Context context;
    private List<String> studentList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onLongClick(int position, View view);
        void onDelete(int position);
    }

    public StudentAdapter(Context context, List<String> studentList, OnItemClickListener listener) {
        this.context = context;
        this.studentList = studentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        String student = studentList.get(position);
        holder.txtName.setText(student);

        // Popup Menu
        holder.imgMore.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.imgMore);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_edit) {
                    Toast.makeText(context, "Edit " + student, Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.action_delete) {
                    studentList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Deleted " + student, Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.action_share) {
                    Toast.makeText(context, "Share " + student, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;

            });
            popupMenu.show();
        });

        // Long Press for Context Menu (Event Chaining)
        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(position, v);
            return false; // allow context menu to show
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imgMore;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtStudentName);
            imgMore = itemView.findViewById(R.id.imgMore);
        }
    }
}
