package com.bowoon.android.aac_practice;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bowoon.android.aac_practice.databinding.MemoItemBinding;

import java.util.List;
import java.util.Objects;

public class MemoListAdapter extends RecyclerView.Adapter<MemoListAdapter.MemoViewHolder> {
    private List<? extends Memo> items;
    private MemoItemClick click;

    public MemoListAdapter(MemoItemClick click) {
        this.click = click;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MemoItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.memo_item,
                        viewGroup, false);
        binding.setCallback(click);
        return new MemoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder memoViewHolder, int i) {
        memoViewHolder.binding.setMemo(items.get(i));
        memoViewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return (items == null) ? 0 : items.size();
    }

    public void setMemoList(final List<? extends Memo> memoList) {
        if (this.items == null) {
            this.items = memoList;
            notifyItemRangeInserted(0, memoList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {

                @Override
                public int getOldListSize() {
                    return items.size();
                }

                @Override
                public int getNewListSize() {
                    return memoList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return items.get(oldItemPosition).getId() ==
                            memoList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int newItemPosition, int oldItemPosition) {
                    Memo newProduct = memoList.get(oldItemPosition);
                    Memo oldProduct = items.get(newItemPosition);
                    return newProduct.getId() == oldProduct.getId()
                            && Objects.equals(newProduct.getContent(), oldProduct.getContent())
                            && Objects.equals(newProduct.getTime(), oldProduct.getTime());
                }
            });
            items = memoList;
            result.dispatchUpdatesTo(this);
        }
    }

    static class MemoViewHolder extends RecyclerView.ViewHolder {
        final MemoItemBinding binding;

        public MemoViewHolder(MemoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
