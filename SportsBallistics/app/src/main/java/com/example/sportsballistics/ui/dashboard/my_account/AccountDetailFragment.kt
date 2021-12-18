package com.example.sportsballistics.ui.dashboard.my_account

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.databinding.FragmentAccountDetailBinding
import com.example.sportsballistics.utils.AppConstant

class AccountDetailFragment : Fragment() {
    lateinit var binding: FragmentAccountDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account_detail, container, false)
        loadAssets()

        loadAssets()

        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_1)) {
            var title = requireArguments().getString(AppConstant.INTENT_EXTRA_1)!!
            binding.txtTitle.text = title
        }
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_2)) {
            val body = requireArguments().getString(AppConstant.INTENT_EXTRA_2)!!
            val text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                Html.fromHtml(body).toString()
            }
            binding.txtBody.text = text
        }
        return binding.root
    }

    fun loadAssets() {
        val sportsType = SharedPrefUtil.getInstance().sportsType

        AppConstant.changeColor(binding.txtTitle)
        AppConstant.changeColor(binding.txtBody)
        when (sportsType) {
            AppConstant.BASEBALL -> {
                Glide.with(binding.root).load(R.drawable.bb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.bb_inner_logo)
                    .into(binding.imgLogo)
            }
            AppConstant.VOLLEYBALL -> {
                Glide.with(binding.root).load(R.drawable.vb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.vb_inner_logo)
                    .into(binding.imgLogo)
            }
            AppConstant.TODDLER -> {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
                    .into(binding.imgLogo)
            }
            AppConstant.QB -> {
                Glide.with(binding.root).load(R.drawable.qb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo)
                    .into(binding.imgLogo)
            }
        }
    }

}