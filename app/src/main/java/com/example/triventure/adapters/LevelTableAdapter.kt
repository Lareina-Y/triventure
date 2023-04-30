//Author(s): Lareina Yang
package com.example.triventure.adapters

//Attribution: This file to create a table adapter was copied and modified from
//RecyclerView Example code posted in CSCI 4176 Lab4 - Creating a ListView and a RecyclerView in Android Application in Brightspace

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.triventure.QuestionActivity
import com.example.triventure.R
import com.example.triventure.models.LevelModel

class LevelTableAdapter (
    private val levels: List<LevelModel>,
    private val currentLevel: Int)
    : RecyclerView.Adapter<LevelTableAdapter.ViewHolder>() {

    //Stores values to identify whether a row is a header or regular row
    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ROW = 1
    }

    //Tracks the name and highest score of a level in a ViewHolder class
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val levelName: TextView = itemView.findViewById(R.id.level_name)
        val highestScore: TextView = itemView.findViewById(R.id.highest_score)
    }

    //Links a row type to the Row Layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = when (viewType) {
            VIEW_TYPE_HEADER -> layoutInflater.inflate(R.layout.row_layout, parent, false)
            VIEW_TYPE_ROW -> layoutInflater.inflate(R.layout.row_layout, parent, false)
            else -> throw IllegalArgumentException("Invalid view type")
        }
        return ViewHolder(itemView)
    }

    //Populates the table with content
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Populates the first row with header information (column names) and populates subsequent
        // rows with individual level data
        if (position == 0) {
            holder.levelName.text = "Level Name"
            holder.highestScore.text = "Highest Score"
            holder.levelName.setTypeface(null, Typeface.BOLD);
            holder.highestScore.setTypeface(null, Typeface.BOLD);
            holder.levelName.setTextColor(ContextCompat.getColor(holder.itemView.context,
                R.color.white
            ))
            holder.highestScore.setTextColor(ContextCompat.getColor(holder.itemView.context,
                R.color.white
            ))
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                R.color.Avocado
            ))
        } else {
            val level = levels[position - 1]
            holder.levelName.text = level.levelName
            holder.highestScore.text = level.highestScore.toString() + "%"

            // Different color for each row
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.OldLace
                ))
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.custom_light_blue
                ))
            }

            //Allow levels that are before or are the current level to be clickable; otherwise make
            // them un-clickable
            if (level.levelId <= currentLevel) {
                holder.itemView.setOnClickListener {
                    // handle click on each row
                    val intent = Intent(holder.itemView.context, QuestionActivity::class.java)
                    intent.putExtra("levelId", level.levelId)
                    startActivity(holder.itemView.context, intent, null)
                }
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, androidx.appcompat.R.color.material_grey_300))
                holder.itemView.setOnClickListener(null)
                holder.levelName.setTextColor(ContextCompat.getColor(holder.itemView.context, androidx.appcompat.R.color.material_grey_600))
                holder.highestScore.setTextColor(ContextCompat.getColor(holder.itemView.context, androidx.appcompat.R.color.material_grey_600))
            }
        }
    }

    //Retrieves the number of levels
    override fun getItemCount() = levels.size + 1

    // return viewType based on the position and include the header
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ROW
    }
}
