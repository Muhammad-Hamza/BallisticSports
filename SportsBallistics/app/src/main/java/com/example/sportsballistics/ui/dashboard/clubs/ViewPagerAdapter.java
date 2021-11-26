package com.example.sportsballistics.ui.dashboard.clubs;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sportsballistics.ui.dashboard.athletes.AthletesFragment;
import com.example.sportsballistics.ui.dashboard.my_account.AccountFragment;
import com.example.sportsballistics.ui.dashboard.trainer.TrainerFragment;
import com.example.sportsballistics.ui.dashboard.users.UserFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter
{

    private Context myContext;
    int totalTabs;

    public ViewPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ClubFragment();
            case 1:
                return new AthletesFragment();
            case 2:
                return new TrainerFragment();
                case 3:
                return new UserFragment();
            case 4:
                return new AccountFragment();

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
