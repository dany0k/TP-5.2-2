package ru.vsu.cs.tp.richfamily

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import ru.vsu.cs.tp.richfamily.adapter.OnboardingItemAdapter
import ru.vsu.cs.tp.richfamily.api.model.onboarding.OnboardingItem
import ru.vsu.cs.tp.richfamily.databinding.ActivityOnboardingBinding
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.DataBaseHelper

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onBoardingItemAdapter: OnboardingItemAdapter
    private lateinit var indicatorsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        indicatorsContainer = binding.indicatorContainer
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        setOnboardingItems()
        setupIndicators()
        setCurrentIndicator(0)
    }

    private fun setOnboardingItems() {
        onBoardingItemAdapter = OnboardingItemAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.wallet,
                    title = Constants.ONBOARDING_WALLET_TITLE,
                    description = Constants.ONBOARDING_WALLET_DISC),
                OnboardingItem(
                    onboardingImage = R.drawable.group,
                    title = Constants.ONBOARDING_GROUP_TITLE,
                    description = Constants.ONBOARDING_GROUP_DISC),
                OnboardingItem(
                    onboardingImage = R.drawable.analyze,
                    title = Constants.ONBOARDING_GROUP_TITLE,
                    description = Constants.ONBOARDING_GROUP_DISC),
            )
        )
        binding.onboardingVp.adapter = onBoardingItemAdapter
        binding.onboardingVp.registerOnPageChangeCallback(object :
        ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentIndicator(position)
            }
        })
        (binding.onboardingVp.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
        binding.nextImageIm.setOnClickListener {
            if (binding.onboardingVp.currentItem + 1 < onBoardingItemAdapter.itemCount) {
                binding.onboardingVp.currentItem += 1
            } else {
                navigateToMainActivity()
            }
        }
        binding.getStartedButton.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        val db = DataBaseHelper(applicationContext , null)
        db.updateFlag(1)
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(onBoardingItemAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams
        = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active_background
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
            }
        }
    }
}