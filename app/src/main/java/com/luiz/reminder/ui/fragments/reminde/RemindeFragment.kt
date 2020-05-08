package com.luiz.reminder.ui.fragments.reminde

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.luiz.reminder.BR
import com.luiz.reminder.R
import com.luiz.reminder.api.models.Notes
import com.luiz.reminder.databinding.GuidelinesBinding
import com.luiz.reminder.ui.adapters.NotesAdapter
import com.luiz.reminder.ui.base.BaseFragment
import com.luiz.reminder.ui.view.AlertDefault
import com.luiz.reminder.util.Utils

class RemindeFragment : BaseFragment<GuidelinesBinding, RemindeViewModel>() {

    private val notesAdapter = NotesAdapter(arrayListOf())

    private val notesListDataObserver = Observer<List<Notes>> { list ->
        if (list != null && list.isNotEmpty()) {
            this.viewDataBinding().rvRemindes.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = notesAdapter
            }

            notesAdapter.updateNotesList(list)
        } else {
            this.viewDataBinding().rvRemindes.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = Utils.noResultAdapter(
                    context!!,
                    context!!.getString(R.string.title_reminde_empty),
                    R.drawable.ic_sad
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setHasOptionsMenu(true)

        this.viewModel().loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
        this.viewModel().loadError.observe(viewLifecycleOwner, errorLiveDataObserver)
        this.viewModel().user.observe(viewLifecycleOwner, userLiveDataObserver)
        this.viewModel().notes.observe(viewLifecycleOwner, notesListDataObserver)
        this.viewModel().context = context!!
        this.viewModel().getUserInfo()
        this.viewModel().getAllNotes()
    }

    private fun setupToolbar() {
        getParentActivity()!!.setSupportActionBar(this.viewDataBinding().toolbar)
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
            this.viewModel().logout()
            alertDefault.dismiss()
        })
        alertDefault.show()
    }

    override fun layoutId(): Int {
        return R.layout.fragment_guidelines
    }

    override fun binding(): Int {
        return BR.guidelineViewModel
    }

    override fun viewModel(): RemindeViewModel {
        return ViewModelProviders.of(this).get(RemindeViewModel::class.java)
    }
}
