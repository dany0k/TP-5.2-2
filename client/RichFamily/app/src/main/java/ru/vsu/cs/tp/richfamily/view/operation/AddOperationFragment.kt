package ru.vsu.cs.tp.richfamily.view.operation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yandex.metrica.YandexMetrica
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.category.Category
import ru.vsu.cs.tp.richfamily.api.model.wallet.Wallet
import ru.vsu.cs.tp.richfamily.api.service.CategoryApi
import ru.vsu.cs.tp.richfamily.api.service.OperationApi
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddOperationBinding
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.repository.OperationRepository
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.Filter
import ru.vsu.cs.tp.richfamily.utils.YandexEvents
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.OperationViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddOperationFragment : Fragment(){
    private lateinit var binding: FragmentAddOperationBinding
    private lateinit var walViewModel: WalletViewModel
    private lateinit var opViewModel: OperationViewModel
    private lateinit var catViewModel: CategoryViewModel
    private lateinit var token: String
    private lateinit var catList: List<Category>
    private lateinit var walList: List<Wallet>

    private val args by navArgs<AddOperationFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOperationBinding.inflate(
            inflater,
            container,
            false
        )
        token = MainActivity.getToken()
        initViewModels(token = token)
        initAdapters()
        binding.senderEt.filters = arrayOf(Filter.textFilter)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walViewModel.walletList.observe(viewLifecycleOwner) {
            catViewModel.catList.observe(viewLifecycleOwner) { it1 ->
                walList = it
                catList = it1
                initAdapters()
                if (!requireArguments().isEmpty) {
                    setOperation()
                }
                catViewModel.catList.removeObserver { }
                walViewModel.walletList.removeObserver { }
            }
        }
        process()
    }

    private fun setOperation() = with(binding) {
        val curTemp = args.template
        totalEt.setText(DecimalFormat("#.###").format(curTemp.temp_sum))
        commentEt.setText(curTemp.temp_comment)
        senderEt.setText(curTemp.temp_recipient)
        if (curTemp.temp_variant == Constants.CONS_TEXT) {
            consumptionRb.isChecked = true
        } else {
            incomeRb.isChecked = true
        }
        filledScore.setText(walViewModel.findWalletById(curTemp.account, walList))
        filledCategory.setText(opViewModel.findCategoryById(curTemp.category, catList))
        initAdapters()
    }

    private fun process() {
        binding.timeEt.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            setTime(hasFocus)
        }
        binding.dateEt.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            setDate(hasFocus)
        }
        binding.addOperationButton.setOnClickListener {
            processButton()
        }
    }

    private fun processButton() {
        val rbText: String = if (binding.consumptionRb.isChecked) {
            Constants.CONS_TEXT
        } else {
            Constants.INCOME_TEXT
        }
        val wallet = binding.filledScore.text.toString()
        val category = binding.filledCategory.text.toString()
        val time = binding.timeEt.text.toString()
        val date = binding.dateEt.text.toString()
        val opRecipient = binding.senderEt.text.toString()
        val opSum = binding.totalEt.text.toString()
        val opComment = binding.commentEt.text.toString()
        if (!inputCheck(
                wallet = wallet,
                category = category,
                opType = rbText,
                time = time,
                date = date,
                opRecipient = opRecipient,
                opSum = opSum,
                opComment = opComment
            )) {
        return
        }
        if (!walViewModel.isScoreValid(opSum)) {
            binding.totalEt.error = Constants.WALLET_INVALID
            return
        }
        binding.addOperationButton.startAnimation()
        opViewModel.addOperation(
            walletId = walViewModel.getWalletFromACTV(wallet, walList),
            categoryId = catViewModel.getCategoryFromACTV(category, catList),
            opType = rbText,
            opDate = dateTimeToLocalDateTime(
                time = time,
                date = date
            ),
            opRecipient = opRecipient,
            opSum = opSum.toFloat(),
            opComment = opComment
        )
        showToast(Constants.SUCCESS)
        YandexMetrica.reportEvent(YandexEvents.ADD_OPERATION)
        findNavController().popBackStack()
        stopAnim()
    }

    private fun stopAnim() {
        binding.addOperationButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner)
        binding.addOperationButton.revertAnimation()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun inputCheck(
        wallet: String,
        category: String,
        opType: String,
        time: String,
        date: String,
        opRecipient: String,
        opSum: String,
        opComment: String
    ): Boolean {
        if (wallet.isBlank()) {
            binding.scoreTil.error = Constants.COMP_FIELD
        }
        if (category.isBlank()) {
            binding.categoryTil.error = Constants.COMP_FIELD
        }
        if (opType.isBlank()) {
            binding.operationTypeTv.error = Constants.COMP_FIELD
        }
        if (opRecipient.isBlank()) {
            binding.senderEt.error = Constants.COMP_FIELD
        }
        if (opSum.isBlank()) {
            binding.totalEt.error = Constants.COMP_FIELD
        }
        if (opComment.isBlank()) {
            binding.commentEt.error = Constants.COMP_FIELD
        }
        if (date.isBlank()) {
            binding.dateEt.error = Constants.COMP_FIELD
        }
        if (time.isBlank()) {
            binding.timeEt.error = Constants.COMP_FIELD
        }
        if (opRecipient.length > 20) {
            binding.senderEt.error = Constants.MAX_LENGHT_ERR_20
        }
        if (opSum.length > 9) {
            binding.totalEt.error = Constants.MAX_LENGHT_ERR_9
        }
        if (opSum.toInt() <= 0) {
            binding.totalEt.error
        }
        return wallet.isNotBlank() &&
                category.isNotBlank() &&
                opType.isNotBlank() &&
                time.isNotBlank() &&
                date.isNotBlank() &&
                opRecipient.isNotBlank() &&
                opSum.isNotBlank() &&
                opComment.isNotBlank() &&
                opSum.length <= 9 &&
                opRecipient.length <= 20
    }

    private fun setDate(hasFocus: Boolean) {
        if (hasFocus) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireActivity(),
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val result = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    binding.dateEt.setText(result)
                }, year, month, dayOfMonth
            )
            datePickerDialog.show()
            binding.dateEt.clearFocus()
        }
    }

    private fun setTime(hasFocus: Boolean) {
        if (hasFocus) {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(requireActivity(),
                { _, selectedHour, selectedMinute ->
                    val result = "$selectedHour:$selectedMinute"
                    binding.timeEt.setText(result)
                }, hour, minute, true)
            timePickerDialog.show()
            binding.timeEt.clearFocus()
        }
    }

    private fun dateTimeToLocalDateTime(time: String, date: String): String {
        val inputDateFormat = SimpleDateFormat(
            "HH:mm d/M/yyyy", Locale.getDefault()
        )
        val outputDateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault()
        )

        val inputDate = inputDateFormat.parse("$time $date")
        return outputDateFormat.format(inputDate!!)
    }

    private fun initAdapters() = with(binding) {
        if (token.isNotBlank()) {
            catViewModel.catList.observe(viewLifecycleOwner) {
                val catAdapter = ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_list_item_1,
                    it.map { cat ->  cat.cat_name})
                filledCategory.setAdapter(catAdapter)
            }
            walViewModel.walletList.observe(viewLifecycleOwner) {
                val walAdapter = ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_list_item_1,
                    it.map { wal ->  "${wal.acc_name} ${wal.acc_sum} ${wal.acc_currency}"})
                filledScore.setAdapter(walAdapter)
            }
        }
    }
    private fun initViewModels(token: String) {
        if (token.isNotEmpty()) {
            // Operation vm
            val operationApi = OperationApi.getOperationApi()!!
            val opRepository = OperationRepository(operationApi = operationApi, token = token)
            opViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = opRepository,
                    token = token
                )
            )[OperationViewModel::class.java]
            // Category vm
            val categoryApi = CategoryApi.getCategoryApi()!!
            val categoryRepository = CategoryRepository(categoryApi = categoryApi, token = token)
            catViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = categoryRepository,
                    token = token
                )
            )[CategoryViewModel::class.java]
            catViewModel.getAllCategories()
            // Wallet vm
            val walletApi = WalletApi.getWalletApi()!!
            val walletRepository = WalletRepository(walletApi = walletApi, token = token)
            walViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = walletRepository,
                    token = token
                )
            )[WalletViewModel::class.java]
            walViewModel.getAllWallets()
        }
    }
}