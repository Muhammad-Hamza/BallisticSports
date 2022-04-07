package com.sportsballistics.sportsballistics.ui.dashboard.create_athlete_form

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sportsballistics.sportsballistics.R
import com.sportsballistics.sportsballistics.data.SharedPrefUtil
import com.sportsballistics.sportsballistics.data.listeners.Listeners
import com.sportsballistics.sportsballistics.data.local.AthleteFormLocalModel
import com.sportsballistics.sportsballistics.data.remote.athletes.Service
import com.sportsballistics.sportsballistics.data.remote.form_service.FormServiceModel
import com.sportsballistics.sportsballistics.databinding.FragmentAthleteFormBinding
import com.sportsballistics.sportsballistics.ui.dashboard.create_athlete_form.component.AthleteFormAdapter
import com.sportsballistics.sportsballistics.ui.dashboard.create_athlete_form.component.AthleteFormViewModel
import com.sportsballistics.sportsballistics.utils.AppConstant
import com.sportsballistics.sportsballistics.utils.AppUtils.Companion.showToast
import com.google.gson.Gson

class AthleteFormFragment : Fragment()
{


    lateinit var binding: FragmentAthleteFormBinding
    lateinit var viewModel: AthleteFormViewModel
    var athleteId: String? = null
    var serviceModel: Service? = null
    lateinit var adapter: AthleteFormAdapter
    lateinit var listOfQuestion: ArrayList<AthleteFormLocalModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View?
    { // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_athlete_form, container, false);
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        loadAssets()
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_1))
        {
            athleteId = requireArguments().getString(AppConstant.INTENT_EXTRA_1)!!
        }
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_2))
        {
            val stringContent = requireArguments().getString(AppConstant.INTENT_EXTRA_2)!!
            serviceModel = Gson().fromJson(stringContent, Service::class.java)
        }
        if (!TextUtils.isEmpty(athleteId) && serviceModel != null)
        {
            viewModel.getServiceContent(
                binding.root.context, object : AthleteFormViewModel.ContentFetchListener
                {
                    override fun onFetched(anyObject: Any)
                    {
                        if (anyObject is FormServiceModel) loadUIData(anyObject)
                    }
                }, athleteId!!, serviceModel!!.slug
            )
        } else
        {
            getBackNavigate()
        }

        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        binding.btnSubmit.setOnClickListener {
            if (adapter != null && context != null)
            {
                viewModel.submitForm(
                    requireContext(), object : AthleteFormViewModel.ContentFetchListener
                    {
                        override fun onFetched(anyObject: Any)
                        {
                            showToast("Add data inserted")
                            getBackNavigate()
                        }
                    }, athleteId!!, adapter.paramMap, serviceModel!!.slug_edit
                )
            }
        }
        binding.tvCancel.setOnClickListener {
            getBackNavigate()
        }
    }

    private fun loadUIData(anyObject: FormServiceModel)
    {
        if (anyObject.data!!.athletic_name != null && !TextUtils.isEmpty(anyObject.data.athletic_name.fullname))
        {
            binding.tvInfo.setText(anyObject.data.athletic_name.fullname)
            binding.tvFormHeading.setText(serviceModel!!.name)
            listOfQuestion = ArrayList()

            if (anyObject != null && anyObject.data != null && anyObject.data.nameArr != null)
            {
                for (i in 0..(anyObject.data.nameArr.size - 1))
                {
                    var athleteFormObj = AthleteFormLocalModel(
                        i, anyObject.data.nameArr[i], null, null,null
                    )
                    if( anyObject.data.valueArr != null)
                    {
                        athleteFormObj.value = anyObject.data.valueArr[i]
                    }
                    listOfQuestion.add(athleteFormObj)
                }
            }
            adapter = AthleteFormAdapter(binding.root.context, listOfQuestion)
            binding.recyclerView.layoutManager =
                LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.setItemViewCacheSize(1000)
            binding.recyclerView.adapter = adapter
            binding.progressBar.visibility = View.GONE
        } else
        {
            showToast("No form found")
            getBackNavigate()
        }
    }


    private fun initViewModel()
    {
        viewModel = ViewModelProviders.of(this).get(AthleteFormViewModel::class.java)
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
                                              showToast("No data found")
                                              getBackNavigate()
                                          }

                                          override fun addErrorDialog(msg: String?)
                                          {
                                              binding.progressBar.visibility = View.GONE
                                              showToast(msg!!)
                                              getBackNavigate()
                                          }
                                      })
    }

    private fun getBackNavigate()
    {
        Navigation.findNavController(binding.root).navigateUp();
    }

    fun loadAssets()
    {
        val sportsType = SharedPrefUtil.getInstance().sportsType

        AppConstant.changeColor(binding.tvInfo)
        AppConstant.changeColor(binding.tvFormHeading)
        AppConstant.changeColor(binding.tvCancel)

        binding.btnSubmit.background = null
        var drawable: Drawable? = null

        when (sportsType)
        {
            AppConstant.BASEBALL   ->
            {
                Glide.with(binding.root).load(R.drawable.bb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.bb_inner_logo).into(binding.imgLogo)
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.btn_baseball)
            }
            AppConstant.VOLLEYBALL ->
            {
                Glide.with(binding.root).load(R.drawable.vb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.vb_inner_logo).into(binding.imgLogo)
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.btn_baseball)
            }
            AppConstant.TODDLER    ->
            {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
                    .into(binding.imgLogo)
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.btn_bg)
            }
            AppConstant.QB         ->
            {
                Glide.with(binding.root).load(R.drawable.qb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo).into(binding.imgLogo)
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.btn_qb)
            }
        }
        if (drawable != null)
        {
            binding.btnSubmit.background = drawable
            binding.btnSubmit.setTextColor(
                ContextCompat.getColor(
                    binding.root.context, R.color.white
                )
            )
        }
    }
}