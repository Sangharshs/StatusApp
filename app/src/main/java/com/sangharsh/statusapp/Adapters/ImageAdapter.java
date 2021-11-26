package com.sangharsh.statusapp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sangharsh.statusapp.FullImageActivity;
import com.sangharsh.statusapp.Model.Image_Model;
import com.sangharsh.statusapp.R;

import java.util.List;

import static com.sangharsh.statusapp.FullImageActivity.image;
import static com.sangharsh.statusapp.FullImageActivity.imageView;
import static com.sangharsh.statusapp.api.GET_IMAGE_FOLDER;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Viewholder> {
    List<Image_Model> image_modelList;

    public ImageAdapter(List<Image_Model> image_modelList) {
        this.image_modelList = image_modelList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
    holder.textView.setText(image_modelList.get(position).getCategory());
    String img = image_modelList.get(position).getImage();
        Glide.with(holder.imageView.getContext()).load(GET_IMAGE_FOLDER + img).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FullImageActivity.class);
                intent.putExtra("position",holder.getAdapterPosition());
                intent.putExtra("image",image_modelList.get(position).getImage());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return image_modelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ImageView);
            textView = itemView.findViewById(R.id.category);
        }
    }
}
