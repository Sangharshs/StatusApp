package com.sangharsh.statusapp.Adapters;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sangharsh.statusapp.ui.EnglishCategory.EnglishCategoryFragment;
import com.sangharsh.statusapp.ui.HindiCategory.HindiCategoryFragment;
import com.sangharsh.statusapp.ui.Imagestatus.ImageFragment;

//public class PageAdapter extends FragmentPagerAdapter {
//    int tabcount;
//
//    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
//        super(fm, behavior);
//        tabcount = behavior;
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position)
//    {
//        switch (position)
//        {
//            case 0:
//                return new ImageFragment();
//            case 1:
//                return new HindiCategoryFragment();
//            case 2:
//                return new EnglishCategoryFragment();
//            default: return null;
//        }
//
//    }
//
//    @Override
//    public int getCount() {
//        return tabcount;
//    }
//}
public class PageAdapter extends FragmentPagerAdapter
{
    int tabcount;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0 : return new ImageFragment();
            case 1 : return new HindiCategoryFragment();
            case 2 : return new EnglishCategoryFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
