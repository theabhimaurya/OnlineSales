package com.example.onlinesales

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.onlinesales.adapter.StringAdapter
import com.example.onlinesales.databaseHelper.MyDatabaseHelper
import com.example.onlinesales.databinding.ActivityMainBinding
import com.example.onlinesales.history.HistoryActivity
import com.example.onlinesales.model.ListData
import com.example.onlinesales.model.RequestModel
import com.example.onlinesales.viewModel.SolveViewModel
import com.example.onlinesales.webServices.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SolveViewModel
    private var resultList = arrayListOf<String>()
    private var expressionList = arrayListOf<String>()
    private lateinit var myDatabaseHelper: MyDatabaseHelper

    lateinit var coroutineScope : CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coroutineScope = CoroutineScope(Dispatchers.IO)
        viewModel = ViewModelProvider(this)[SolveViewModel::class.java]
        myDatabaseHelper = MyDatabaseHelper(this)


        initListener()
        initObserver()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.solveResponse.collect {
                when (it.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        resultList.clear()
                        it.data?.let { it1 ->
                            resultList.addAll(it1.result!!)
                            binding.rvResult.adapter = StringAdapter(expressionList ,resultList)

                            // Store the lists in the database
                            val id = 0L
                            val stringLists = ListData(id,expressionList ,resultList)

                            coroutineScope.launch {
                                myDatabaseHelper.insertStringLists(stringLists)

                            }

                        }

                    }

                    else -> {
                        if (Status.NONE != it.status) {
                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


    private fun initListener() {

        binding.buttonHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.button.setOnClickListener {
            if (binding.editTextExpression.text.toString().trim().isNotEmpty()){
                val inputString = binding.editTextExpression.text
                val list = inputString?.split("\n")
                if (list != null) {
                    expressionList.addAll(list)
                }
                val requestModel = RequestModel(expressionList)

                coroutineScope.launch {
                    viewModel.solve(requestModel)
                }
                binding.editTextExpression.setText("")
            }else{
                Toast.makeText(this, "please enter expression...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}