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
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.Category
import ru.vsu.cs.tp.richfamily.api.model.wallet.Wallet
import ru.vsu.cs.tp.richfamily.api.service.CategoryApi
import ru.vsu.cs.tp.richfamily.api.service.OperationApi
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddOperationBinding
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.repository.OperationRepository
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.OperationViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory
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
        token = try {
            SessionManager.getToken(requireActivity())!!
        } catch (e: java.lang.NullPointerException) {
            ""
        }
        initViewModels(token = token)
        initAdapters()
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
        totalEt.setText(curTemp.temp_sum.toString())
        commentEt.setText(curTemp.temp_comment)
        senderEt.setText(curTemp.temp_recipient)
        if (curTemp.temp_variant == Constants.CONS_TEXT) {
            consumptionRb.isChecked = true
        } else {
            incomeRb.isChecked = true
        }
        filledScore.setText(findWalletById(curTemp.account))
        filledCategory.setText(findCategoryById(curTemp.category))
        initAdapters()
    }

    private fun findWalletById(id: Int): String {
        val selectedClass = walList.find {
            it.id == id
        }
        return "${selectedClass!!.acc_name} " +
                "${selectedClass.acc_sum} " +
                "${selectedClass.acc_currency}"
    }

    private fun findCategoryById(id: Int): String {
        val selectedClass = catList.find {
            it.id == id
        }
        return selectedClass!!.cat_name
    }

    private fun process() {
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
                    opViewModel.addOperation(
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

    private fun navigate(rbText: String) {
        if (rbText == Constants.CONS_TEXT) {
            findNavController()
                .navigate(R.id.action_addOperationFragment_to_consumptionFragment)
        } else if (rbText == Constants.INCOME_TEXT) {
            findNavController()
                .navigate(R.id.action_addOperationFragment_to_incomeFragment)
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

    private fun dateTimeToLocalDateTime(time: String, date: String): String {
        val inputDateFormat = SimpleDateFormat(
            "HH:mm d/M/yyyy", Locale.getDefault()
        )
        val outputDateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.getDefault()
        )

        val inputDate = inputDateFormat.parse("$time $date")
        return outputDateFormat.format(inputDate)
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

    private fun setDate(hasFocus: Boolean) {
        if (hasFocus) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireActivity(),
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    binding.dateEt
                        .setText("$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear")
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
                    binding.timeEt.setText("$selectedHour:$selectedMinute")
                }, hour, minute, true)

            timePickerDialog.show()
        }
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