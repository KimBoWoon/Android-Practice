package com.bowoon.android.aac_practice_kotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.aac_practice_kotlin.databinding.MemoItemBinding
import java.util.*


class MemoListAdapter(private var clickListener: MemoItemClick) :
    RecyclerView.Adapter<MemoListAdapter.Companion.MemoViewHolder>() {
    private var items: List<Memo>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoListAdapter.Companion.MemoViewHolder {
        val binding = DataBindingUtil
            .inflate<MemoItemBinding>(
                LayoutInflater.from(parent.context), R.layout.memo_item,
                parent, false
            )
        binding.callback = clickListener
        return MemoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else {
            items!!.size
        }
    }

    override fun onBindViewHolder(holder: MemoListAdapter.Companion.MemoViewHolder, position: Int) {
        holder.binding.memo = items!![position]
        holder.binding.executePendingBindings()
    }

    fun setMemoList(memoList: List<Memo>?) {
        if (this.items == null) {
            this.items = memoList
            notifyItemRangeInserted(0, memoList!!.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                override fun getOldListSize(): Int {
                    return items!!.size
                }

                override fun getNewListSize(): Int {
                    return memoList!!.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return items!![oldItemPosition].id == memoList!![newItemPosition].id
                }

                override fun areContentsTheSame(newItemPosition: Int, oldItemPosition: Int): Boolean {
                    val newProduct = memoList!![oldItemPosition]
                    val oldProduct = items!![newItemPosition]
                    return (newProduct.id == oldProduct.id
                            && Objects.equals(newProduct.content, oldProduct.content)
                            && Objects.equals(newProduct.time, oldProduct.time))
                }
            })
            items = memoList
            result.dispatchUpdatesTo(this)
        }
    }

    companion object {
        class MemoViewHolder(val binding: MemoItemBinding) : RecyclerView.ViewHolder(binding.root)
    }
}