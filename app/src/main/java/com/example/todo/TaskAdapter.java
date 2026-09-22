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


// Adapter class for displaying tasks in a RecyclerView
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    // Interface to handle click events on task items
    public interface TaskActionListener {
        void onTaskClick(Task task);
        void onStatusChanged(Task task, boolean isDone);
        void onEdit(Task task);
        void onDelete(Task task);
    }

    private final List<Task> tasks = new ArrayList<>();
    private final TaskActionListener listener;

    // Constructor accepting click listener instance
    public TaskAdapter(TaskActionListener listener) {
        this.listener = listener;
    }

    // Update list data and refresh RecyclerView
    public void setTasks(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    // Inflate item layout XML and create ViewHolder instance
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    // Bind data to ViewHolder for specific position
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    // Return total item count
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // ViewHolder inner class managing item view references
    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBoxStatus;
        private final TextView textTaskName;
        private final TextView textDueDate;
        private final TextView textStatus;
        private final ImageButton buttonEdit;
        private final ImageButton buttonDelete;

        // Find and bind layout views by ID
        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxStatus = itemView.findViewById(R.id.checkBoxStatus);
            textTaskName = itemView.findViewById(R.id.textTaskName);
            textDueDate = itemView.findViewById(R.id.textDueDate);
            textStatus = itemView.findViewById(R.id.textStatus);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        // Bind task data to item views and attach click listeners
        void bind(final Task task) {
            textTaskName.setText(task.getName());

            // Display due date or placeholder text
            if (task.getDueDate() == null || task.getDueDate().isEmpty()) {
                textDueDate.setText("No due date");
            } else {
                textDueDate.setText("Due " + task.getDueDate());
            }

            // Unbind listener before setting checked status to prevent recursive triggers
            checkBoxStatus.setOnCheckedChangeListener(null);
            checkBoxStatus.setChecked(task.isDone());

            // Style item based on completion status
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

            // Set item click listener for task details
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaskClick(task);
                }
            });

            // Set status change listener for checkbox toggle
            checkBoxStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onStatusChanged(task, isChecked);
                }
            });

            // Set click listener for edit button
            buttonEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(task);
                }
            });

            // Set click listener for delete button
            buttonDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(task);
                }
            });
        }
    }
}