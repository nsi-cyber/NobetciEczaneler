package com.nsicyber.nobetcieczaneler


import android.location.Location
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapter(fm:FragmentManager,var list: ArrayList<PharmacyClass>,var curLoc:Location) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2;
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                var fragment=ListFragment()
                fragment.list=list
                fragment.curLoc=curLoc
                return fragment
            }
            1 -> {
                var fragment=MapFragment()
                fragment.list=list
                fragment.curLoc=curLoc
                return fragment

            }

            else -> {
                var fragment=ListFragment()
                fragment.list=list
                fragment.curLoc=curLoc
                return fragment
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Eczaneler"
            }
            1 -> {
                return "Harita"
            }

        }
        return super.getPageTitle(position)
    }

}