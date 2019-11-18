package com.kinsight.kinsightmultiplatform

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kinsight.kinsightmultiplatform.ViewModels.IdeasViewModel
import com.kinsight.kinsightmultiplatform.extensions.getViewModel
import com.kinsight.kinsightmultiplatform.models.IdeaModel
import com.kinsight.kinsightmultiplatform.notifications.NotificationHelper
import com.kinsight.kinsightmultiplatform.views.*
import kotlinx.android.synthetic.main.customprogress.*
import kotlinx.android.synthetic.main.ideas_layout.*
import kotlinx.android.synthetic.main.loading.*


class MainActivity : FullScreenActivity(), OnItemClickListener {

    private lateinit var adapter: RecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel: IdeasViewModel by lazy {
        getViewModel { IdeasViewModel(application, "dmitri") }
    }

    override fun onItemClicked(idea: IdeaModel) {
        Toast.makeText(this,"Idea ${idea.securityName} \n Alpha: ${idea.alpha}", Toast.LENGTH_LONG)
            .show()
        Log.i("IDEA_", idea.securityName)

        val intent = Intent(this, IdeaActivity::class.java)
        intent.putExtra("ideaCompanyName", idea.securityName)
        intent.putExtra("ideaAlpha", idea.alpha.toString())
        startActivity(intent)

      //  NotificationHelper.sendNotification(this, "${idea.securityName} Idea Alert", "Price objective of ${idea.targetPrice} ${idea.stockCurrency} achieved", "Price objective of ${idea.targetPrice} ${idea.stockCurrency} achieved", false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ideas_layout)
        initRecyclerView()
        initViewModelListener()
        NotificationHelper.createNotificationChannel(this, 1, true, "channel", "channel")

        fab.setOnClickListener {
            val intent = Intent(this, IdeaCreateActivity::class.java)
            intent.putExtra("nextId", viewModel.nextId() + 2)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        swiperefresh.setOnRefreshListener {
           // swiperefresh.isRefreshing = true
            loading.isVisible = true
           initViewModelListener()
        }
    }

    private fun initViewModelListener() {
        viewModel.getIdeas().observe(
            this,
            Observer<List<IdeaModel>> { ideas ->
                Log.i("APP", "Ideas observed: $ideas")
                adapter = RecyclerAdapter(ideas, this)
                ideasRecyclerView.adapter = adapter
                swiperefresh.isRefreshing = false
                loading.isVisible = false
            }
        )
    }

    private fun initRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ideasRecyclerView.layoutManager = linearLayoutManager
    }
}
