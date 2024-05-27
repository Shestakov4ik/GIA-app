package com.example.gia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.example.gia.TheoryAdapter.OnTheoryClickListener;


public class TheoryAdapter extends RecyclerView.Adapter<TheoryAdapter.UserViewHolder> {

    private List<Theory> theoryList;

    private OnTheoryClickListener onTheoryClickListener;

    public TheoryAdapter(List<Theory> theoryList, OnTheoryClickListener listener) {
        this.theoryList = theoryList;
        this.onTheoryClickListener = listener;
    }

    public void sortDataById() {
        Collections.sort(theoryList, new Comparator<Theory>() {
            @Override
            public int compare(Theory theory1, Theory theory2) {

                return Integer.compare(Integer.parseInt(theory1.getId()), Integer.parseInt(theory2.getId()));
            }
        });
        notifyDataSetChanged();
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theory, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Theory theory = theoryList.get(position);
        holder.name_view.setText(theory.getId() + ". " + theory.getName());
        holder.body_view.setText(theory.getShort_body());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTheoryClickListener != null) {
                    onTheoryClickListener.onTheoryClick(theory.getId());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return theoryList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView name_view, body_view;

        public UserViewHolder(View itemView) {
            super(itemView);
            name_view = itemView.findViewById(R.id.name_view);
            body_view = itemView.findViewById(R.id.body_view);
        }
    }

    public interface OnTheoryClickListener {
        void onTheoryClick(String theoryId);
    }

}