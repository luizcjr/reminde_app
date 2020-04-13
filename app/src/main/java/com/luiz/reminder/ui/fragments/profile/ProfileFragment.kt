package com.luiz.reminder.ui.fragments.profile

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.luiz.reminder.R
import com.luiz.reminder.api.models.User
import com.luiz.reminder.databinding.ProfileBinding
import com.luiz.reminder.ui.base.BaseFragment
import com.luiz.reminder.ui.view.AlertDefault

class ProfileFragment : BaseFragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var binding: ProfileBinding? = null

    private val userDataObserver = Observer<User> { user ->
        user.let {
            profileViewModel.email.value = user.email
            profileViewModel.name.value = user.name
            getParentActivity()!!.title = user.name
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        setupToolbar()
        setHasOptionsMenu(true)

        binding!!.profilleViewModel = profileViewModel
        binding!!.lifecycleOwner = this

        profileViewModel.loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
        profileViewModel.loadError.observe(viewLifecycleOwner, errorLiveDataObserver)
        profileViewModel.user.observe(viewLifecycleOwner, userDataObserver)
        profileViewModel.context = context!!
        profileViewModel.getUserInfo()
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
            profileViewModel.logout()
            alertDefault.dismiss()
        })
        alertDefault.show()
    }
}
