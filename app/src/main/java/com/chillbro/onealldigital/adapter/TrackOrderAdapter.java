package com.chillbro.onealldigital.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.chillbro.onealldigital.fragment.CurrentOrderFragment;
import com.chillbro.onealldigital.fragment.OrderHistoryFragment;

public class TrackOrderAdapter extends FragmentStateAdapter {

    public TrackOrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0)
            fragment = new CurrentOrderFragment();
        else
            fragment = new OrderHistoryFragment();
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
