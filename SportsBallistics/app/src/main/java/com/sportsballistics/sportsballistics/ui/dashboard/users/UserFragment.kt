package com.sportsballistics.sportsballistics.ui.dashboard.users

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.sportsballistics.sportsballistics.AppSystem
import com.sportsballistics.sportsballistics.R
import com.sportsballistics.sportsballistics.data.SharedPrefUtil
import com.sportsballistics.sportsballistics.data.api.URLIdentifiers
import com.sportsballistics.sportsballistics.data.listeners.Listeners
import com.sportsballistics.sportsballistics.data.remote.club.ClubResponse
import com.sportsballistics.sportsballistics.data.remote.club.UsersItem
import com.sportsballistics.sportsballistics.databinding.FragmentUserBinding
import com.sportsballistics.sportsballistics.ui.dashboard.athletes.AthletesViewModel
import com.sportsballistics.sportsballistics.ui.dashboard.dashboard.DashboardViewModel
import com.sportsballistics.sportsballistics.utils.AppConstant
import com.sportsballistics.sportsballistics.utils.AppUtils.Companion.showToast
import kotlinx.android.synthetic.main.user_list_item_header.view.*

class UserFragment : Fragment()
{
    lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        loadAssets()
        initViewModel()
        binding.clubListHeader.txtClubName.text = "User Name"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        loadAssets()

        val user = SharedPrefUtil.getInstance().user
        when (user.loggedIn?.roleId)
        {
            "3" ->
            {
                binding.llAddClubAdmin.visibility = View.VISIBLE
                binding.lladdAthleteTrainer.visibility = View.GONE
            }
            "4" ->
            {
                binding.llAddClubAdmin.visibility = View.GONE
                binding.lladdAthleteTrainer.visibility = View.VISIBLE
            }
            else ->
            {
                "Unknown"
            }
        }
        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        binding.llAddClubAdmin.setOnClickListener {
            val args = Bundle()
            args.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_ADD)
            Navigation.findNavController(binding.root).navigate(R.id.action_userFragment_to_createClubAdminFragment, args)
        }

        binding.etReason.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                if (!TextUtils.isEmpty(s))
                {
                    getContent(s.toString())
                }
                else
                {
                    if (TextUtils.isEmpty(s)) getContent("")
                }
            }

            override fun afterTextChanged(s: Editable?)
            {
            }

        })
        binding.llAddTrainer.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_ADD)
            Navigation.findNavController(binding.root).navigate(R.id.action_userFragment_to_createTrainerFragment, bundle)
        }

        binding.llAddAthlete.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_ADD)
            Navigation.findNavController(binding.root).navigate(R.id.action_userFragment_to_createAthleteFragment, bundle)
        }

    }

    private fun initViewModel()
    {
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
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
            }
        })

        getContent("")
    }

    private fun getContent(searchKey: String)
    {
        viewModel.getContent(requireContext(), URLIdentifiers.USER_CONTENT, searchKey, object :
                DashboardViewModel.ContentFetchListener
        {
            override fun onFetched(content: ClubResponse)
            {
                if (content != null && content.content != null && content.content.users != null && content.content.users.size > 0)
                {
                    initRecyclerView(content.content?.users as MutableList<UsersItem>)
                }
                else
                {
                    initRecyclerView(ArrayList<UsersItem>())
                }
            }
        })
    }

    private fun initRecyclerView(list: MutableList<UsersItem>)
    {
        val mLayoutManager = LinearLayoutManager(context)
        val mAdapter = UserAdapter(context, list, object : UserAdapter.OnItemClickListener
        {
            override fun onEditClick(adapterType: Int, user: UsersItem)
            {
                if (user != null && user.role_name != null)
                {
                    val args = Bundle()
                    args.putString(AppConstant.INTENT_EXTRA_1, user.id)
                    args.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_EDIT)
                    when (user.role_name)
                    {
                        "Athlete" ->
                        {
                            Navigation.findNavController(binding.root).navigate(R.id.action_userFragment_to_createAthleteFragment, args)
                        }
                        "Club Admin" ->
                        {
                            if (AppSystem.getInstance().getCurrentUser() != null && AppSystem.getInstance().getCurrentUser()!!.loggedIn != null && AppSystem.getInstance().getCurrentUser()!!.loggedIn!!.roleId.equals(AppConstant.ROLE_SUPER_PORTAL))
                            {
                                Navigation.findNavController(binding.root).navigate(R.id.action_userFragment_to_createClubAdminFragment, args)
                            }
                        }
                        "Trainer" ->
                        {
                            Navigation.findNavController(binding.root).navigate(R.id.action_userFragment_to_createTrainerFragment, args)
                        }
                    }
                }
            }

            override fun onViewClick(adapterType: Int, user: UsersItem)
            {
                if (user != null && user.role_name != null)
                {
                    val args = Bundle()
                    args.putString(AppConstant.INTENT_EXTRA_1, user.id)
                    args.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_VIEW)
                    when (user.role_name)
                    {
                        "Athlete" ->
                        {
                            Navigation.findNavController(binding.root).navigate(R.id.action_userFragment_to_createAthleteFragment, args)
                        }
                        "Club Admin" ->
                        {
                            if (AppSystem.getInstance().getCurrentUser() != null && AppSystem.getInstance().getCurrentUser()!!.loggedIn != null && AppSystem.getInstance().getCurrentUser()!!.loggedIn!!.roleId.equals(AppConstant.ROLE_SUPER_PORTAL))
                            {
                                Navigation.findNavController(binding.root).navigate(R.id.action_userFragment_to_createClubAdminFragment, args)
                            }
                        }
                        "Trainer" ->
                        {
                            Navigation.findNavController(binding.root).navigate(R.id.action_userFragment_to_createTrainerFragment, args)
                        }
                    }
                }
            }

            override fun onDeleteClick(adapterType: Int, user: UsersItem)
            {
//                when(user.role_name){
//                    "Club Admin" -> {
//                        if (AppSystem.getInstance()
//                                .getCurrentUser() != null && AppSystem.getInstance()
//                                .getCurrentUser()!!.loggedIn != null && AppSystem.getInstance()
//                                .getCurrentUser()!!.loggedIn!!.roleId.equals(AppConstant.ROLE_SUPER_PORTAL)
//                        ) {
//                            Navigation.findNavController(binding.root)
//                                .navigate(R.id.action_userFragment_to_createClubFragment, args)
//                        }
//                    }
//                }
                MaterialDialog(binding.root.context).title(null, "Want to delete!").message(null, "Do you want to delete this user?").positiveButton(null, "YES") {
                    viewModel.deleteTrainer(requireContext(), user.id!!, object :
                            AthletesViewModel.ContentFetchListener
                    {
                        override fun onFetched(anyObject: Any)
                        {
                            showToast(R.string.txt_user_deleted)
                            getContent("")
                        }
                    })
                }.negativeButton(null, "NO") {

                }.show()
            }
        })
        binding.recyclerView.layoutManager = mLayoutManager
        binding.recyclerView.adapter = mAdapter
    }

    fun loadAssets()
    {
        val sportsType = SharedPrefUtil.getInstance().sportsType

        AppConstant.changeColor(binding.txtTotalTrainersText)
        AppConstant.changeColor(binding.clubListHeader.txtSerialNo)
        AppConstant.changeColor(binding.clubListHeader.txtClubName)
        AppConstant.changeColor(binding.clubListHeader.txtAction)
        AppConstant.changeColor(binding.clubListHeader.txtRole)
        when (sportsType)
        {
            AppConstant.BASEBALL ->
            {
                Glide.with(binding.root).load(R.drawable.bb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.bb_inner_logo).into(binding.imgLogo)
            }
            AppConstant.VOLLEYBALL ->
            {
                Glide.with(binding.root).load(R.drawable.vb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.vb_inner_logo).into(binding.imgLogo)
            }
            AppConstant.TODDLER ->
            {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo).into(binding.imgLogo)
            }
            AppConstant.QB ->
            {
                Glide.with(binding.root).load(R.drawable.qb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo).into(binding.imgLogo)
            }
        }
    }

}