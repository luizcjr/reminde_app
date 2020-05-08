package com.luiz.reminder.ui.fragments.profile

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.luiz.reminder.BR
import com.luiz.reminder.R
import com.luiz.reminder.api.models.User
import com.luiz.reminder.databinding.ProfileBinding
import com.luiz.reminder.ui.base.BaseFragment
import com.luiz.reminder.ui.view.AlertDefault

class ProfileFragment : BaseFragment<ProfileBinding, ProfileViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setHasOptionsMenu(true)

        this.viewModel().loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
        this.viewModel().loadError.observe(viewLifecycleOwner, errorLiveDataObserver)
        this.viewModel().user.observe(viewLifecycleOwner, userLiveDataObserver)
        this.viewModel().context = context!!
        this.viewModel().getUserInfo()
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
        return R.layout.fragment_profile
    }

    override fun binding(): Int {
        return BR.profilleViewModel
    }

    override fun viewModel(): ProfileViewModel {
        return ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }
}
