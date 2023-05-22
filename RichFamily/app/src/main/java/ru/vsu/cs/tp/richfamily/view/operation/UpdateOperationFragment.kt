package ru.vsu.cs.tp.richfamily.view.operation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.Category
import ru.vsu.cs.tp.richfamily.api.model.operation.Operation
import ru.vsu.cs.tp.richfamily.api.model.wallet.Wallet
import ru.vsu.cs.tp.richfamily.api.service.CategoryApi
import ru.vsu.cs.tp.richfamily.api.service.OperationApi
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentUpdateOperationBinding
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.repository.OperationRepository
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.OperationViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateOperationFragment : Fragment() {
    private lateinit var binding: FragmentUpdateOperationBinding
    private lateinit var walViewModel: WalletViewModel
    private lateinit var opViewModel: OperationViewModel
    private lateinit var catViewModel: CategoryViewModel
    private lateinit var curOp: Operation
    private lateinit var token: String
    private var catList: List<Category> = emptyList()
    private  var walList: List<Wallet> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateOperationBinding.inflate(
            inflater,
            container,
            false
        )
        token = MainActivity.getToken()
        if (token.isNotEmpty()) {
            initViewModels(token = token)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catViewModel.catList.observe(viewLifecycleOwner) {
            walViewModel.walletList.observe(viewLifecycleOwner) { it1 ->
                catList = it
                walList = it1
                initAdapters()
                setOperation()
                catViewModel.catList.removeObserver { }
                walViewModel.walletList.removeObserver { }
            }
        }
        binding.timeEt.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            setTime(hasFocus)
        }
        binding.dateEt.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            setDate(hasFocus)
        }
        binding.addOperationButton.setOnClickListener {
            val rbText: String = if (binding.consumptionRb.isChecked) {
                Constants.CONS_TEXT
            } else {
                Constants.INCOME_TEXT
            }

            if (inputCheck(
                    wallet = binding.filledScore.text.toString(),
                    category = binding.filledCategory.text.toString(),
                    opType = rbText,
                    time = binding.timeEt.text.toString(),
                    date = binding.dateEt.text.toString(),
                    opRecipient = binding.senderEt.toString(),
                    opSum = binding.totalEt.text.toString(),
                    opComment = binding.commentEt.text.toString()
                )) {
                with(binding) {
                    opViewModel.editOperation(
                        id = curOp.id,
                        walletId = getWalletFromACTV(filledScore.text.toString()),
                        categoryId = getCategoryFromACTV(filledCategory.text.toString()),
                        opType = rbText,
                        opDate = dateTimeToLocalDateTime(
                            time = timeEt.text.toString(),
                            date = dateEt.text.toString()),
                        opRecipient = senderEt.text.toString(),
                        opSum =  totalEt.text.toString().toFloat(),
                        opComment = commentEt.text.toString()
                    )
                }
                navigate(rbText = rbText)
            } else {
                Toast.makeText(
                    requireActivity(),
                    Constants.COMP_FIELDS_TOAST,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
                    binding.dateEt
                        .setText(result)
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
        }
    }

    private fun navigate(rbText: String) {
        if (rbText == Constants.CONS_TEXT) {
            findNavController()
                .navigate(R.id.action_updateOperationFragment_to_consumptionFragment)
        } else {
            findNavController()
                .navigate(R.id.action_updateOperationFragment_to_incomeFragment)
        }
    }

    private fun dateTimeToLocalDateTime(time: String, date: String): String {
        val inputDateFormat = SimpleDateFormat(
            "HH:mm d/M/yyyy", Locale.getDefault()
        )
        val outputDateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.getDefault()
        )

        val inputDate = inputDateFormat.parse("$time $date")
        return outputDateFormat.format(inputDate!!)
    }

    private fun setOperation() = with(binding) {
        opViewModel.currentOperation.observe(viewLifecycleOwner) {
            curOp = it
            dateEt.setText(getDate(it.op_date))
            timeEt.setText(getTime(it.op_date))
            senderEt.setText(it.op_recipient)
            totalEt.setText(it.op_sum.toString())
            commentEt.setText(it.op_comment)
            if (it.op_variant == Constants.CONS_TEXT) {
                consumptionRb.isChecked = true
            } else {
                incomeRb.isChecked = true
            }
            filledScore.setText(findWalletById(it.account))
            filledCategory.setText(findCategoryById(it.category))
            initAdapters()
        }
    }

    private fun getTime(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        return dateTime.format(timeFormatter)
    }

    private fun getDate(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        return dateTime.format(dateFormatter)
    }

    private fun findWalletById(id: Int): String {
        val selectedClass = walList.find {
            it.id == id
        }
        return "${selectedClass!!.acc_name} " +
                "${selectedClass.acc_sum} " +
                selectedClass.acc_currency
    }

    private fun findCategoryById(id: Int): String {
        val selectedClass = catList.find {
            it.id == id
        }
        return selectedClass!!.cat_name
    }

    private fun getWalletFromACTV(selectedItem: String): Int {
        val selectedClass = walList.find {
            "${it.acc_name} ${it.acc_sum} ${it.acc_currency}" == selectedItem
        }
        return selectedClass!!.id
    }

    private fun getCategoryFromACTV(selectedItem: String): Int {
        val selectedClass = catList.find {
            it.cat_name == selectedItem
        }
        return selectedClass!!.id
    }

    private fun initAdapters() = with(binding) {
        if (token.isNotBlank()) {
            val catAdapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_list_item_1,
                catList.map { cat ->  cat.cat_name})
            filledCategory.setAdapter(catAdapter)
            val walAdapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_list_item_1,
                walList.map { wal ->  "${wal.acc_name} ${wal.acc_sum} ${wal.acc_currency}"})
            filledScore.setAdapter(walAdapter)
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
        if (wallet.isNotBlank() &&
            category.isNotBlank() &&
            opType.isNotBlank() &&
            time.isNotBlank() &&
            date.isNotBlank() &&
            opRecipient.isNotBlank() &&
            opSum.isNotBlank() &&
            opComment.isNotBlank()
        ) {
            return true
        }
        return false
    }
}