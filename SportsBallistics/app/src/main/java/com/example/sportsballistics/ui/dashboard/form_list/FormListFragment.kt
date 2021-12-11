package com.example.sportsballistics.ui.dashboard.form_list

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.R
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.athletes.Service
import com.example.sportsballistics.data.remote.service.ServiceResponseModel
import com.example.sportsballistics.databinding.FragmentFormListBinding
import com.example.sportsballistics.ui.dashboard.form_list.component.FormListAdapter
import com.example.sportsballistics.ui.dashboard.form_list.component.FormListViewModel
import com.example.sportsballistics.utils.AppConstant
import com.google.gson.Gson

class FormListFragment : Fragment() {
    lateinit var binding: FragmentFormListBinding
    lateinit var viewModel: FormListViewModel
    var athleteId: String? = null
    lateinit var adapter: FormListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_form_list, container, false);
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_1)) {
            athleteId = requireArguments().getString(AppConstant.INTENT_EXTRA_1)!!
        }
        if (!TextUtils.isEmpty(athleteId)) {
            viewModel.getServiceContent(
                requireContext(),
                object : FormListViewModel.ContentFetchListener {
                    override fun onFetched(anyObject: Any) {
                        if (anyObject is ServiceResponseModel) {
                            loadUIContent(anyObject)
                        }
                    }

                }, athleteId!!
            )
        } else {
            Navigation.findNavController(binding.root).navigateUp()
        }
    }

    private fun loadUIContent(anyObject: ServiceResponseModel) {
        if (anyObject.data!!.athletic_name != null && !TextUtils.isEmpty(anyObject.data.athletic_name.fullname)) {
            binding.tvInfo.setText(anyObject.data!!.athletic_name.fullname)

            adapter = FormListAdapter(
                requireContext(),
                anyObject.data.services,
                object : FormListAdapter.OnItemClickListener {
                    override fun onEditClick(adapterType: Int, anyData: Any) {
                        if (anyData is Service) {
//                            showMessage(anyData.name)
                            val bundle = Bundle()
                            bundle.putString(AppConstant.INTENT_EXTRA_1, athleteId)
                            bundle.putString(AppConstant.INTENT_EXTRA_2, Gson().toJson(anyData))
                            Navigation.findNavController(binding.root).navigate(
                                R.id.action_formListFragment_to_athleteFormFragment,
                                bundle
                            )
                        }
                    }

                })
            binding.recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = adapter
        }
        binding.progressBar.visibility = View.GONE
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(FormListViewModel::class.java)
        viewModel.attachErrorListener(object : Listeners.DialogInteractionListener {
            override fun dismissDialog() {
                binding.progressBar.visibility = View.GONE
            }

            override fun addDialog() {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun addErrorDialog() {
                binding.progressBar.visibility = View.GONE
                showMessage("No data found")
                getBackNavigate()
            }

            override fun addErrorDialog(msg: String?) {
                binding.progressBar.visibility = View.GONE
                showMessage(msg!!)
                getBackNavigate()
            }
        })
    }

    private fun getBackNavigate() {
        Navigation.findNavController(binding.root).navigateUp();
    }

    private fun showMessage(content: String) {
        Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()
    }

}