package com.luiz.reminder.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.luiz.reminder.R
import com.luiz.reminder.api.models.Notes
import com.luiz.reminder.databinding.ItemsNotesBinding
import com.luiz.reminder.ui.interfaces.NoteClickListener

class NotesAdapter(private val notes: ArrayList<Notes>) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>(), NoteClickListener {

    override fun onClick(v: View) {

    }

    fun updateNotesList(newList: List<Notes>) {
        notes.clear()
        notes.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemsNotesBinding>(
            inflater,
            R.layout.items_notes,
            parent,
            false
        )
        return NotesViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val itemAtual = notes[position]
        holder.view.note = itemAtual
        holder.view.listener = this
    }

    class NotesViewHolder(var view: ItemsNotesBinding) : RecyclerView.ViewHolder(view.root)
}