package com.example.integration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.integration.R;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.example.integration.adapter.MyArticleAdapter;
import com.example.integration.listener.ICartLoadListener;
import com.example.integration.listener.IArticleLoadListener;
import com.example.integration.model.ArticleModel;
import com.example.integration.model.CartModel;
import com.example.integration.utils.SpaceItemDecoration;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.nex3z.notificationbadge.NotificationBadge;
import android.widget.FrameLayout;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class BoutiqueActivity extends AppCompatActivity implements IArticleLoadListener, ICartLoadListener {
    @BindView(R.id.recycler_articles)
    RecyclerView recyclerArticles;
    @BindView(R.id.mainBoutique)
    RelativeLayout mainBoutique;
    @BindView(R.id.badge)
    NotificationBadge badge;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;


    IArticleLoadListener articleLoadListener;
    ICartLoadListener cartLoadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique);

        init();
        loadArticleFromFirebase();
    }

    private void loadArticleFromFirebase() {
        List<ArticleModel> articleModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("articles-boutique")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot articleSnapshot:snapshot.getChildren()){
                                ArticleModel articleModel = articleSnapshot.getValue(ArticleModel.class);
                                articleModel.setKey(articleSnapshot.getKey());
                                articleModels.add(articleModel);
                            }
                            articleLoadListener.onArticleLoadSuccess(articleModels);
                        }
                        else
                            articleLoadListener.onArticleLoadFailed("Ne trouve pas l'article");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
articleLoadListener.onArticleLoadFailed(error.getMessage());
                    }
                });
    }

    private void init() {
        ButterKnife.bind(this);

        articleLoadListener = this;
        cartLoadListener = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerArticles.setLayoutManager(gridLayoutManager);
        recyclerArticles.addItemDecoration(new SpaceItemDecoration());
    }

    @Override
    public void onArticleLoadSuccess(@Nullable List<ArticleModel> ArticleModelList) {
        MyArticleAdapter adapter = new MyArticleAdapter(this,ArticleModelList);
        recyclerArticles.setAdapter(adapter);
    }

    @Override
    public void onArticleLoadFailed(@Nullable String message) {
        Snackbar.make(mainBoutique, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadSuccess(@Nullable List<CartModel> CartModelList) {

    }

    @Override
    public void onCartLoadFailed(@Nullable String message) {

    }
}

