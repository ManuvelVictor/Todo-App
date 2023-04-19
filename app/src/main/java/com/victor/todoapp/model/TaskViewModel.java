package com.victor.todoapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.victor.todoapp.data.Repository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    public static Repository repository;
    public final LiveData<List<Task>> allTask;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allTask = repository.getAllTask();
    }

    public LiveData<List<Task>> getAllTask() {
        return allTask;
    }

    public static void insert(Task task) {
        repository.insert(task);
    }

    public LiveData<Task> get(long id) {
        return repository.get(id);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }
}
