package com.example.gia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PodTheoryAdapter extends RecyclerView.Adapter<PodTheoryAdapter.UserViewHolder> {

    private List<PodTheory> podtheoryList;
    private OnTheoryClickListener onTheoryClickListener;

    public PodTheoryAdapter(List<PodTheory> theoryList, OnTheoryClickListener listener) {
        this.podtheoryList = theoryList;
        this.onTheoryClickListener = listener;
    }

    // Метод для обновления данных в адаптере
    public void updateData(List<PodTheory> newList) {
        this.podtheoryList = newList;
        notifyDataSetChanged();
    }



    public void sortDataById() {
        Collections.sort(podtheoryList, new Comparator<PodTheory>() {
            @Override
            public int compare(PodTheory theory1, PodTheory theory2) {
                // Сравнение идентификаторов как целочисленных значений
                return Integer.compare(Integer.parseInt(theory1.getId()), Integer.parseInt(theory2.getId()));
            }
        });
        notifyDataSetChanged(); // Обновить RecyclerView после сортировки
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_podtheory, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        PodTheory theory = podtheoryList.get(position);

        if (theory != null) {
            holder.name_view.setText(theory.getId() + ". " + theory.getName());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTheoryClickListener != null) {
                        onTheoryClickListener.onTheoryClick(theory.getId());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return podtheoryList != null ? podtheoryList.size() : 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView name_view, body_view;

        public UserViewHolder(View itemView) {
            super(itemView);
            name_view = itemView.findViewById(R.id.name_view);

        }
    }

    public interface OnTheoryClickListener {
        void onTheoryClick(String theoryId);
    }
}
