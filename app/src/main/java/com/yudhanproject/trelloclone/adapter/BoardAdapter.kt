package com.yudhanproject.trelloclone.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.activities.MainActivity
import com.yudhanproject.trelloclone.models.Board
import com.yudhanproject.trelloclone.models.BoardHighlighted
import kotlinx.android.synthetic.main.item_board.view.*

class BoardAdapter(private val list: List<Board>, private val highlighted: List<BoardHighlighted>,val a:String): RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {
    val umur = a

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_board,
                parent, false)
        return BoardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        Log.d("umurgan", "entity: $umur");
        val currentItem = list[position]
        val highlightedItem = highlighted[position]
        if (position in 0..umur.toInt()){
            holder.image.setImageResource(highlightedItem.imageViewHighlighted)
            holder.textview.text = highlightedItem.textHighlighted
        } else {
            holder.image.setImageResource(currentItem.imageView)
            holder.textview.text = currentItem.text
        }
    }

    override fun getItemCount() = list.size

    class BoardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.img
        val textview: TextView = itemView.board_text
    }

}