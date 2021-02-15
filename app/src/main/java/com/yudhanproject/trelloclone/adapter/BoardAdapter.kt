package com.yudhanproject.trelloclone.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mukesh.tinydb.TinyDB
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.activities.CreateTaskActivity
import com.yudhanproject.trelloclone.activities.TaskActivity
import com.yudhanproject.trelloclone.models.board.BoardDummy
import com.yudhanproject.trelloclone.models.board.BoardHighlighted
import kotlinx.android.synthetic.main.item_board.view.*

class BoardAdapter(private val list: List<BoardDummy>, private val highlighted: List<BoardHighlighted>, a:String, val context:Context): RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {
    val umur = a.toInt() - 1
    private val ITEM_DUMMY = 0
    private val ITEM_HIGHLIGHTED = 1
    private val ITEM_NOW = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        Log.d("itemposition",viewType.toString())
        return if (viewType == ITEM_HIGHLIGHTED) {
            BoardViewHolder(inflater.inflate(R.layout.item_board, parent, false))
        } else if (viewType == ITEM_NOW){
            BoardViewHolder(inflater.inflate(R.layout.item_now, parent, false))
        } else{
            BoardViewHolder(inflater.inflate(R.layout.item_dummy_board, parent, false))
        }
        //val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_board, parent, false)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        Log.d("umurgan", "entity: $umur")
        val currentItem = list[position]
        val highlightedItem = highlighted[position]
        if (position in 0 until umur){
            holder.textview.text = highlightedItem.textHighlighted
        } else {
            holder.textview.text = currentItem.textBoardDummy
            holder.itemView.setOnClickListener {
                val position: Int = holder.adapterPosition+1
                val gText = currentItem.textBoardDummy
                Toast.makeText(context,"you clicked item $position",Toast.LENGTH_LONG).show()
                val tinyDB = TinyDB(context)
                tinyDB.putInt("getUmur", position)
                val intent = Intent(context, TaskActivity::class.java)
                intent.putExtra("gText",gText)
                intent.putExtra("gPosition",position)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = list.size
    override fun getItemViewType(position: Int): Int {
        return if (position in 0 until umur) {
            ITEM_HIGHLIGHTED
        } else if (position == umur){
            ITEM_NOW
        } else {
            ITEM_DUMMY
        }
    }

    class BoardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textview: TextView = itemView.board_text
    }
}