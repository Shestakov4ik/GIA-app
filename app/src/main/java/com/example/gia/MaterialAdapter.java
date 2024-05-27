package com.example.gia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.gia.TheoryAdapter.OnTheoryClickListener;


public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.UserViewHolder> {

    private List<Material> materialList;

    public MaterialAdapter(List<Material> materalList) {
        this.materialList = materalList;
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_material, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Material material = materialList.get(position);
        holder.name_view.setText(material.getId() + ". " + material.getName());
        holder.body_view.setText(material.getBody());

    }


    @Override
    public int getItemCount() {
        return materialList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView name_view, body_view;

        public UserViewHolder(View itemView) {
            super(itemView);
            name_view = itemView.findViewById(R.id.name_view);
            body_view = itemView.findViewById(R.id.body_view);
        }
    }



}
