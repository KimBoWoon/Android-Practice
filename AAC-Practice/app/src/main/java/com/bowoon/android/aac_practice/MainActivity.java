package com.bowoon.android.aac_practice;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.bowoon.android.aac_practice.databinding.ActivityMainBinding;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MemoListAdapter adapter;
    private MemoViewModel memoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViewModel();
        initView();
    }

    private void initViewModel() {
        memoViewModel = ViewModelProviders.of(this).get(MemoViewModel.class);
        memoViewModel.getAllMemo().observe(this, new Observer<List<Memo>>() {
            @Override
            public void onChanged(@Nullable List<Memo> memos) {
                if (memos != null) {
                    adapter.setMemoList(memos);
                }

                binding.executePendingBindings();
            }
        });
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        adapter = new MemoListAdapter(click);
        binding.recyclerView.setAdapter(adapter);
        binding.setCallback(mainButtonClick);
    }

    MemoItemClick click = new MemoItemClick() {
        @Override
        public void onClick(Memo memo) {
            Toast.makeText(getApplicationContext(), memo.getContent(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDeleteClick(Memo memo) {
            memoViewModel.deleteMemo(memo);
        }
    };

    MainButtonClick mainButtonClick = new MainButtonClick() {
        @Override
        public void onAddClick() {
            EditText editText = findViewById(R.id.main_edit_text);
            String content = editText.getText().toString();
            if (content.length() == 0) {
                return;
            }
            memoViewModel.addMemo(new Memo(content));
        }
    };
}
