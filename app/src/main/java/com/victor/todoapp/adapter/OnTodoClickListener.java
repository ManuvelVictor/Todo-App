package com.victor.todoapp.adapter;

import com.victor.todoapp.model.Task;

public interface OnTodoClickListener {
    void onTodoClick(Task task);

    void onToDoRadioButtonClick(Task task);
}
