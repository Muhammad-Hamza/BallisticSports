package com.example.sportsballistics.ui.dashboard.my_account

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.databinding.FragmentAccountDetailBinding
import com.example.sportsballistics.utils.AppConstant

class AccountDetailFragment : Fragment()
{
    lateinit var binding: FragmentAccountDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_detail, container, false)

        loadAssets()

        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_1))
        {
            var title = requireArguments().getString(AppConstant.INTENT_EXTRA_1)!!
            binding.txtTitle.text = title
        }
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_2))
        {
            val body = requireArguments().getString(AppConstant.INTENT_EXTRA_2)!!
            val text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY).toString()
            }
            else
            {
                Html.fromHtml(body).toString()
            }
            binding.txtBody.text = text
        }
        return binding.root
    }



    fun loadAssets(){
        val sportsType = SharedPrefUtil.getInstance().sportsType
        when(sportsType){
            AppConstant.BASEBALL->{
                binding.clubListLayoutParent.background = ContextCompat.getDrawable(requireContext(),R.drawable.bb_bg)
                binding.imgLogo.background = ContextCompat.getDrawable(requireContext(),R.drawable.bb_inner_logo)
                binding.txtBody.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorBB))
                binding.txtTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorBB))
            }
            AppConstant.VOLLEYBALL->{
                binding.clubListLayoutParent.background = ContextCompat.getDrawable(requireContext(),R.drawable.vb_all_bg)
                binding.imgLogo.background = ContextCompat.getDrawable(requireContext(),R.drawable.vb_inner_logo)
                binding.imgLogo.background = ContextCompat.getDrawable(requireContext(),R.drawable.vb_inner_logo)
                binding.txtBody.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorVB))
                binding.txtTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorVB))
            }

            AppConstant.QB->{

                binding.clubListLayoutParent.background = ContextCompat.getDrawable(requireContext(),R.drawable.qb_bg)
                binding.imgLogo.background = ContextCompat.getDrawable(requireContext(),R.drawable.qb_inner_logo)
                binding.imgLogo.background = ContextCompat.getDrawable(requireContext(),R.drawable.qb_inner_logo)
                binding.txtBody.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorQB))
                binding.txtTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorQB))
            }
        }

        AppSystem.getInstance().setStatusColor(requireActivity())
    }


}