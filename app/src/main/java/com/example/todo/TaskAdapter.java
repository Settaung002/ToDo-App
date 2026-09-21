package com.example.todo;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    public interface TaskActionListener {
        void onTaskClick(Task task);
        void onStatusChanged(Task task, boolean isDone);
        void onEdit(Task task);
        void onDelete(Task task);
    }

    private final List<Task> tasks = new ArrayList<>();
    private final TaskActionListener listener;

    public TaskAdapter(TaskActionListener listener) {
        this.listener = listener;
    }

    public void setTasks(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBoxStatus;
        private final TextView textTaskName;
        private final TextView textDueDate;
        private final TextView textStatus;
        private final ImageButton buttonEdit;
        private final ImageButton buttonDelete;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxStatus = itemView.findViewById(R.id.checkBoxStatus);
            textTaskName = itemView.findViewById(R.id.textTaskName);
            textDueDate = itemView.findViewById(R.id.textDueDate);
            textStatus = itemView.findViewById(R.id.textStatus);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        void bind(final Task task) {
            textTaskName.setText(task.getName());

            if (task.getDueDate() == null || task.getDueDate().isEmpty()) {
                textDueDate.setText("No due date");
            } else {
                textDueDate.setText("Due " + task.getDueDate());
            }

            checkBoxStatus.setOnCheckedChangeListener(null);
            checkBoxStatus.setChecked(task.isDone());

            if (task.isDone()) {
                textStatus.setText(R.string.done);
                textStatus.setTextColor(itemView.getContext().getColor(R.color.done_green));
                textStatus.setBackgroundResource(R.drawable.bg_status_done);
                textTaskName.setPaintFlags(textTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                textStatus.setText(R.string.todo);
                textStatus.setTextColor(itemView.getContext().getColor(R.color.todo_gray));
                textStatus.setBackgroundResource(R.drawable.bg_status_todo);
                textTaskName.setPaintFlags(textTaskName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaskClick(task);
                }
            });

            checkBoxStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onStatusChanged(task, isChecked);
                }
            });

            buttonEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(task);
                }
            });

            buttonDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(task);
                }
            });
        }
    }
}