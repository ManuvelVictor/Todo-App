package com.victor.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.victor.todoapp.adapter.OnTodoClickListener;
import com.victor.todoapp.adapter.RecyclerViewAdapter;
import com.victor.todoapp.databinding.ActivityMainBinding;
import com.victor.todoapp.model.SharedViewModel;
import com.victor.todoapp.model.Task;
import com.victor.todoapp.model.TaskViewModel;


public class MainActivity extends AppCompatActivity implements OnTodoClickListener {
    private ActivityMainBinding binding;
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication()).create(TaskViewModel.class);

        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedViewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);

        binding.fab.setOnClickListener(v -> {
            showBottomSheetDialog();
        });

        taskViewModel.getAllTask().observe(this, tasks -> {
            recyclerViewAdapter = new RecyclerViewAdapter(tasks, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });
    }

    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTodoClick(Task task) {
        sharedViewModel.selectItem(task);
        sharedViewModel.setIsEdit(true);
        showBottomSheetDialog();
    }

    @Override
    public void onToDoRadioButtonClick(Task task) {
        TaskViewModel.delete(task);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}