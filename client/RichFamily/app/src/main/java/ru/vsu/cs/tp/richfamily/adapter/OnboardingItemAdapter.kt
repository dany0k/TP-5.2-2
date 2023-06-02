package ru.vsu.cs.tp.richfamily.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.onboarding.OnboardingItem
import ru.vsu.cs.tp.richfamily.databinding.OnboardingItemContainerBinding

class OnboardingItemAdapter(
    private val onBoardingItems: List<OnboardingItem>
) : RecyclerView.Adapter<OnboardingItemAdapter.OnBoardingItemViewHolder>() {

    inner class OnBoardingItemViewHolder(
        binding: OnboardingItemContainerBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private val imageOnboarding = binding.imageOnboardingIv
        private val textTitle = binding.titleOnboardingTv
        private val textDescription = binding.textDescrTv
        fun bind(onboardingItem: OnboardingItem) {
            imageOnboarding.setImageResource(onboardingItem.onboardingImage)
            textTitle.text = onboardingItem.title
            textDescription.text = onboardingItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.onboarding_item_container, parent, false)
        val binding = OnboardingItemContainerBinding.bind(view)
        return OnBoardingItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }

    override fun onBindViewHolder(holder: OnBoardingItemViewHolder, position: Int) {
        holder.bind(onBoardingItems[position])
    }
}