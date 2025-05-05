package com.pam.lab2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public interface OnEventActionListener {
        void onEdit(Event event);

        void onDelete(Event event);
    }

    private final List<Event> eventList;
    private final OnEventActionListener actionListener;

    public EventAdapter(List<Event> events, OnEventActionListener listener) {
        this.eventList = events;
        this.actionListener = listener;
    }

    public void updateList(List<Event> newEvents) {
        eventList.clear();
        eventList.addAll(newEvents);
        notifyDataSetChanged();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.titleText.setText(event.title);
        holder.dateText.setText(event.date);

        holder.cardView.setCardBackgroundColor(getCategoryColor(event.category));

        holder.buttonEdit.setOnClickListener(v -> actionListener.onEdit(event));
        holder.buttonDelete.setOnClickListener(v -> actionListener.onDelete(event));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, dateText;
        ImageButton buttonEdit, buttonDelete;
        CardView cardView;

        public EventViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.textViewTitle);
            dateText = itemView.findViewById(R.id.textViewDate);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            cardView = (CardView) itemView;
        }
    }

    private int getCategoryColor(String category) {
        switch (category) {
            case "Work":
                return Color.parseColor("#E0F7FA");
            case "Study":
                return Color.parseColor("#E8F5E9");
            case "Personal":
                return Color.parseColor("#FCE4EC");
            case "Sport":
                return Color.parseColor("#FFF3E0");
            case "Meet":
                return Color.parseColor("#EDE7F6");
            case "Other":
                return Color.parseColor("#F5F5F5");
            default:
                return Color.WHITE;
        }
    }
}
