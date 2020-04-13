package com.luiz.reminder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luiz.reminder.R

class NoResultAdapter(
    private val context: Context,
    private val message: String,
    private val color: Int,
    private val icon: Int,
    private val marginTop: Int
) : RecyclerView.Adapter<NoResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_no_result, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.message.text = message

        val layoutParams = holder.icon.layoutParams as LinearLayout.LayoutParams

        layoutParams.setMargins(0, marginTop, 0, 0)

        holder.icon.layoutParams = layoutParams
        holder.message.setTextColor(context.resources.getColor(color))

        if (icon != 0) {
            Glide.with(context)
                .asBitmap()
                .load(icon)
                .into(holder.icon)
        } else {
            val layoutParamsMessage = holder.message.layoutParams as LinearLayout.LayoutParams

            layoutParamsMessage.setMargins(0, marginTop, 0, 0)

            holder.message.layoutParams = layoutParamsMessage

            holder.icon.visibility = View.GONE
        }
    }

    inner class ViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val message: TextView = itemView.findViewById(R.id.txtMessage)
        val icon: ImageView = itemView.findViewById(R.id.imgIcon)
    }
}