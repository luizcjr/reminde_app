package com.luiz.reminder.ui.fragments.reminde

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.luiz.reminder.R
import com.luiz.reminder.api.models.Notes
import com.luiz.reminder.databinding.GuidelinesBinding
import com.luiz.reminder.ui.adapters.NotesAdapter
import com.luiz.reminder.ui.base.BaseFragment
import com.luiz.reminder.ui.view.AlertDefault
import com.luiz.reminder.util.Utils

class RemindeFragment : BaseFragment() {

    private lateinit var remindeViewModel: RemindeViewModel
    private var binding: GuidelinesBinding? = null
    private val notesAdapter = NotesAdapter(arrayListOf())

    private val notesListDataObserver = Observer<List<Notes>> { list ->
        if (list != null && list.isNotEmpty()) {
            binding!!.rvRemindes.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = notesAdapter
            }

            notesAdapter.updateNotesList(list)
        } else {
            binding!!.rvRemindes.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = Utils.noResultAdapter(
                    context!!,
                    context!!.getString(R.string.title_reminde_empty),
                    R.drawable.ic_sad
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_guidelines, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        remindeViewModel =
            ViewModelProviders.of(this).get(RemindeViewModel::class.java)

        setupToolbar()
        setHasOptionsMenu(true)

        binding!!.guidelineViewModel = remindeViewModel
        binding!!.lifecycleOwner = this

        remindeViewModel.loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
        remindeViewModel.loadError.observe(viewLifecycleOwner, errorLiveDataObserver)
        remindeViewModel.user.observe(viewLifecycleOwner, userLiveDataObserver)
        remindeViewModel.notes.observe(viewLifecycleOwner, notesListDataObserver)
        remindeViewModel.context = context!!
        remindeViewModel.getUserInfo()
        remindeViewModel.getAllNotes()
    }

    private fun setupToolbar() {
        getParentActivity()!!.setSupportActionBar(binding!!.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit) {
            showDialogExit()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showDialogExit() {
        val alertDefault = AlertDefault(
            context!!,
            "Atenção",
            "Deseja realmente sair?",
            true
        )

        alertDefault.addButton("Não", R.style.ButtonOutline, View.OnClickListener {
            alertDefault.dismiss()
        })

        alertDefault.addButton("Sim", R.style.ButtonDefault, View.OnClickListener {
            remindeViewModel.logout()
            alertDefault.dismiss()
        })
        alertDefault.show()
    }
}
