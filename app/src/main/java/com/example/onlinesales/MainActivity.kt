package com.example.onlinesales

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.onlinesales.adapter.StringAdapter
import com.example.onlinesales.databinding.ActivityMainBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SolveViewModel::class.java]

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
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        binding.button.setOnClickListener {
            val inputString = binding.editTextExpression.text
            val list = inputString?.split("\n")
            if (list != null) {
                expressionList.addAll(list)
            }
            val requestModel = RequestModel(expressionList)

            coroutineScope.launch {
                viewModel.solve(requestModel)
            }

        }
    }
}