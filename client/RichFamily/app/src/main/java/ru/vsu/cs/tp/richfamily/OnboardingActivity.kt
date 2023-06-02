package ru.vsu.cs.tp.richfamily

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import ru.vsu.cs.tp.richfamily.adapter.OnboardingItemAdapter
import ru.vsu.cs.tp.richfamily.api.model.onboarding.OnboardingItem
import ru.vsu.cs.tp.richfamily.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onBoardingItemAdapter: OnboardingItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        setOnboardingItems()
    }

    private fun setOnboardingItems() {
        onBoardingItemAdapter = OnboardingItemAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.wallet,
                    title = "Следите за своими финансами создавая расходы и доходы.",
                    description = "Создайте разные кошельки и категории для точного "),
                OnboardingItem(
                    onboardingImage = R.drawable.group,
                    title = "Объединяйтесь в группы",
                    description = "Создавайте или присоединяйтесь в группы, чтобы отслеживать операции других участников"),
                OnboardingItem(
                    onboardingImage = R.drawable.analyze,
                    title = "Анализируйте свой бюджет",
                    description = "Вы всегда можете поссмотреть на все свои операции, а также экспортировать их на свое мобильное устройство "),
            )
        )
        binding.onboardingVp.adapter = onBoardingItemAdapter
    }
}