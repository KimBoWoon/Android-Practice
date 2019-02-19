package com.bowoon.android.aac_practice_kotlin

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bowoon.android.aac_practice_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MemoListAdapter
    private lateinit var memoViewModel: MemoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initView()
    }

    private fun initViewModel() {
        memoViewModel = ViewModelProviders.of(this).get(MemoViewModel::class.java)
        memoViewModel.getAllMemo().observe(this, object : Observer<List<Memo>> {
            override fun onChanged(@Nullable memos: List<Memo>?) {
                if (memos != null) {
                    adapter.setMemoList(memos)
                }

                binding.executePendingBindings()
            }
        })
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        adapter = MemoListAdapter(clickListener)
        binding.recyclerView.adapter = adapter
        binding.callback = mainButtonClick
    }

    private var clickListener: MemoItemClick = object : MemoItemClick {
        override fun onClick(memo: Memo) {
            Toast.makeText(applicationContext, memo.content, Toast.LENGTH_SHORT).show()
        }

        override fun onDeleteClick(memo: Memo) {
            memoViewModel.deleteMemo(memo)
        }
    }

    private var mainButtonClick: MainButtonClick = object : MainButtonClick {
        override fun onAddClick() {
            val editText = findViewById<EditText>(R.id.main_edit_text)
            val content = editText.text.toString()
            if (content.isEmpty()) {
                Toast.makeText(applicationContext, "내용을 입력 하세요!", Toast.LENGTH_SHORT).show()
                return
            }
            editText.setText("")
            memoViewModel.addMemo(Memo(content))
        }
    }
}
