package com.kinsight.kinsightmultiplatform.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kinsight.kinsightmultiplatform.BuildConfig
import com.kinsight.kinsightmultiplatform.models.IdeaModel
import com.kinsight.kinsightmultiplatform.repository.IdeaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IdeaCreateViewModel : ViewModel() {
    private val serverApiUrl = BuildConfig.url

    private val repository by lazy { IdeaRepository(serverApiUrl) }

    fun saveIdea(ideaModel : IdeaModel) {
        Log.i("APP", "saving idea: $ideaModel")
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveIdea(ideaModel)
        }
    }
}