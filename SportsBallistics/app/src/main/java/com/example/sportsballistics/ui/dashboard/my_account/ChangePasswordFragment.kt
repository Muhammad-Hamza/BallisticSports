package com.example.sportsballistics.ui.dashboard.my_account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.sportsballistics.R
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.AccountResponse
import com.example.sportsballistics.databinding.FragmentAccountDetailBinding
import com.example.sportsballistics.databinding.FragmentChangePasswordBinding
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : Fragment()
{
    lateinit var binding: FragmentChangePasswordBinding
    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
        initViewModel()

        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        binding.btnSubmit.setOnClickListener {
            if (etNewPass.text.toString().isEmpty())
            {
                showMessage("Please insert new password")
            }
            else if (etOldPass.text.toString().isEmpty())
            {
                showMessage("Please insert old password")
            }
            else if (etConfirmPass.text.toString().isEmpty())
            {
                showMessage("Please insert confirm password")
            }
            else if (!etNewPass.text.toString().equals(etConfirmPass.text.toString()))
            {
                showMessage("New password and confirm password is not same")
            }
            else
            {
                viewModel.changePassword(requireContext(), etOldPass.text.toString(), etNewPass.text.toString(), etConfirmPass.text.toString(), object :
                        AccountViewModel.ContentFetchListener
                {
                    override fun onFetched(content: AccountResponse)
                    {
                        if (content.message != null) showMessage(content.message)
                    }
                })
            }
        }
        return binding.root
    }

    private fun initViewModel()
    {
        viewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        viewModel.attachErrorListener(object : Listeners.DialogInteractionListener
        {
            override fun dismissDialog()
            {
                binding.progressBar.visibility = View.GONE
            }

            override fun addDialog()
            {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun addErrorDialog()
            {
                binding.progressBar.visibility = View.GONE
            }

            override fun addErrorDialog(msg: String?)
            {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(binding.root.context, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showMessage(content: String)
    {
        Toast.makeText(binding.root.context, content, Toast.LENGTH_SHORT).show()
    }
}