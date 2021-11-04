package com.example.integration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.integration.R;
import com.example.integration.model.ArticleModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyArticleAdapter extends RecyclerView.Adapter<MyArticleAdapter.MyArticleViewHolder> {

    private Context context;
    private List<ArticleModel> articleModelList;

    public MyArticleAdapter(Context context, List<ArticleModel> articleModelList) {
        this.context = context;
        this.articleModelList = articleModelList;
    }

    @NonNull
    @Override
    public MyArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyArticleViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.layout_article_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyArticleViewHolder holder, int position) {
        Glide.with(context)
                .load(articleModelList.get(position).getImageUrl())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("$").append(articleModelList.get(position).getPrix()));
        holder.txtName.setText(new StringBuilder().append(articleModelList.get(position).getNom()));
    }


    @Override
    public int getItemCount() {
        return articleModelList.size();
    }

    public class MyArticleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice)
        TextView txtPrice;

        private Unbinder unbinder;
        public MyArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
