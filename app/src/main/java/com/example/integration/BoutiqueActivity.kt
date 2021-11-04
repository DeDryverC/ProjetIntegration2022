package com.example.integration

import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import com.example.integration.R
import androidx.recyclerview.widget.RecyclerView
import android.widget.RelativeLayout
import com.nex3z.notificationbadge.NotificationBadge
import android.widget.FrameLayout
import android.os.Bundle

class BoutiqueActivity : AppCompatActivity() {
    @BindView(R.id.recycler_articles)
    var recyclerArticles: RecyclerView? = null

    @BindView(R.id.mainBoutique)
    var mainBoutique: RelativeLayout? = null

    @BindView(R.id.badge)
    var badge: NotificationBadge? = null

    @BindView(R.id.btnCart)
    var btnCart: FrameLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boutique)
    }
}