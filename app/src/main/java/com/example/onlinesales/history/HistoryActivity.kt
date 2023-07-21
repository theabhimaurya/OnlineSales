package com.example.onlinesales.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.onlinesales.R
import com.example.onlinesales.adapter.StringAdapter
import com.example.onlinesales.databaseHelper.MyDatabaseHelper
import com.example.onlinesales.databinding.ActivityHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var myDatabaseHelper: MyDatabaseHelper
    lateinit var coroutineScope : CoroutineScope
    private var resultList = arrayListOf<String>()
    private var expressionList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coroutineScope = CoroutineScope(Dispatchers.IO)
        myDatabaseHelper = MyDatabaseHelper(this@HistoryActivity)

        coroutineScope.launch {
            // Retrieve the lists from the database
            val retrievedLists = myDatabaseHelper.getStringLists()

            for (item in retrievedLists) {
                expressionList.addAll(item.list1)
                resultList.addAll(item.list2)
            }
            binding.rvHistory.adapter = StringAdapter(expressionList,resultList)

            checkEmptyList()
        }
        binding.textView.setOnClickListener {
            onBackPressed()
        }

        expressionList.clear()
        resultList.clear()


    }

    private fun checkEmptyList() {
        if (resultList.isEmpty()){
            Toast.makeText(this@HistoryActivity, "Empty History", Toast.LENGTH_SHORT).show()
        }
    }
}