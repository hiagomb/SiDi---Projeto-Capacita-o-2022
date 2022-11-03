package com.example.projetocapacitacao.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.adapter.ViewPagerAdapter
import com.example.projetocapacitacao.databinding.ActivityNewReceiptBinding
import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.view.fragments.ConfirmationFragment
import com.example.projetocapacitacao.view.fragments.DetailsFragment
import com.google.android.material.tabs.TabLayout

class NewReceiptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewReceiptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Send receipt"

        setUpTabs()
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DetailsFragment(), Constants.FRAGMENTTABS.DETAILS)
        adapter.addFragment(ConfirmationFragment(), Constants.FRAGMENTTABS.CONFIRMATION)

        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        disableTabAt(binding.tabs, 1)

    }

    private fun disableTabAt(tablayout: TabLayout?, index: Int) {
        (tablayout?.getChildAt(0) as? ViewGroup)?.getChildAt(index)?.isEnabled = false
    }



}