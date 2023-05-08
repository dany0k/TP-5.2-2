package ru.vsu.cs.tp.richfamily.view.operation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.vsu.cs.tp.richfamily.api.model.Category
import ru.vsu.cs.tp.richfamily.api.service.CategoryApi
import ru.vsu.cs.tp.richfamily.api.service.OperationApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddOperationBinding
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.repository.OperationRepository
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.OperationViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory
import java.util.Calendar

class AddOperationFragment : Fragment() {

    private lateinit var binding: FragmentAddOperationBinding
    private lateinit var walViewModel: WalletViewModel
    private lateinit var opViewModel: OperationViewModel
    private lateinit var catViewModel: CategoryViewModel
    private lateinit var token: String
    private lateinit var catList: List<Category>


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
        catViewModel.catList.observe(requireActivity()) {
            catList = it
        }
        binding.timeEt.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            setTime(hasFocus)
        }
        binding.dateEt.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            setDate(hasFocus)
        }
        binding.addOperationButton.setOnClickListener {
            with(binding) {
                opViewModel.addOperation(
                    walletId = getWalletFromACTV()!!.id,
                    categoryId = getCategoryFromACTV()!!.id,
                    opType = ,
                    opDate = ,
                    opRecipient = ,
                    opSum = ,
                    opComment =
                )
            }
        }
    }

    private fun getWalletFromACTV(): Any {

    }
    private fun getCategoryFromACTV(): Category? = with(binding) {
        var selected: Category? = null
        filledCategory.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            val selectedClass = catList.find { it.cat_name == selectedItem }
            selected = selectedClass
        }
        return selected
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
        }
    }
}