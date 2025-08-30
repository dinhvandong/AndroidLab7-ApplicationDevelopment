package com.app_mobile.eventhandlingdemo2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StudentAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    StudentAdapter adapter;
    List<String> studentList = new ArrayList<>();
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        studentList.add("Alice");
        studentList.add("Bob");
        studentList.add("Charlie");
        studentList.add("David");
        studentList.add("Eva");

        adapter = new StudentAdapter(this, studentList, this);
        recyclerView.setAdapter(adapter);

        // Register for Context Menu
        registerForContextMenu(recyclerView);

        // Swipe Gesture to delete
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                String name = studentList.get(position);
                studentList.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "Swiped: " + name, Toast.LENGTH_SHORT).show();
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    // Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Action");
        menu.add(0, 1, 0, "Edit");
        menu.add(0, 2, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (selectedPosition != -1) {
            switch (item.getItemId()) {
                case 1:
                    Toast.makeText(this, "Edit: " + studentList.get(selectedPosition), Toast.LENGTH_SHORT).show();
                    return true;
                case 2:
                    // Event Chaining: Confirm Delete → Remove Item → Show Toast
                    showDeleteConfirmation(selectedPosition);
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteConfirmation(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you want to delete " + studentList.get(position) + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    studentList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    // From Adapter callback
    @Override
    public void onLongClick(int position, View view) {
        selectedPosition = position;
    }

    @Override
    public void onDelete(int position) {
        studentList.remove(position);
        adapter.notifyItemRemoved(position);
        Toast.makeText(this, "Deleted: " + position, Toast.LENGTH_SHORT).show();
    }
}
