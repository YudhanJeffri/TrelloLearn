package com.yudhanproject.trelloclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.models.Board
import kotlinx.android.synthetic.main.item_board.view.*

class BoardAdapter(private val list: List<Board>) : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_board,
        parent, false)
        return BoardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val currentItem = list[position]
        holder.image.setImageResource(currentItem.imageView)
        holder.textview.text = currentItem.text
    }

    override fun getItemCount() = list.size

    class BoardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.img
        val textview: TextView = itemView.board_text
    }

}