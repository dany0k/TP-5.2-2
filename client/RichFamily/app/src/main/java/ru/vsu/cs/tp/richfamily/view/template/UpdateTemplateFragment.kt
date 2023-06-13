package ru.vsu.cs.tp.richfamily.view.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.api.model.category.Category
import ru.vsu.cs.tp.richfamily.api.model.wallet.Wallet
import ru.vsu.cs.tp.richfamily.api.service.CategoryApi
import ru.vsu.cs.tp.richfamily.api.service.TemplateApi
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentUpdateTemplateBinding
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.repository.TemplateRepository
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.Filter
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.TemplateViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory
import java.text.DecimalFormat

class UpdateTemplateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateTemplateBinding
    private val args by navArgs<UpdateTemplateFragmentArgs>()
    private lateinit var token: String
    private lateinit var walViewModel: WalletViewModel
    private lateinit var catViewModel: CategoryViewModel
    private lateinit var temViewModel: TemplateViewModel
    private var catList: List<Category> = emptyList()
    private  var walList: List<Wallet> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateTemplateBinding.inflate(
            inflater,
            container,
            false
        )
        token = MainActivity.getToken()
        if (token.isNotEmpty()) {
            initViewModels(token = token)
        }
        binding.templateNameEt.filters = arrayOf(Filter.textFilter)
        binding.senderEt.filters = arrayOf(Filter.textFilter)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catViewModel.catList.observe(viewLifecycleOwner) {
            walViewModel.walletList.observe(viewLifecycleOwner) { it1 ->
                catList = it
                walList = it1
                initAdapters()
                setTemplate()
                catViewModel.catList.removeObserver { }
                walViewModel.walletList.removeObserver { }
            }
        }
        binding.updateTemplateButton.setOnClickListener {
            processButton()
        }
    }

    private fun processButton() {
        val rbText: String = if (binding.consumptionRb.isChecked) {
            Constants.CONS_TEXT
        } else {
            Constants.INCOME_TEXT
        }
        val name = binding.templateNameEt.text.toString()
        val wallet = binding.filledScore.text.toString()
        val cat = binding.filledCategory.text.toString()
        val opRec = binding.senderEt.text.toString()
        val opSum = binding.totalEt.text.toString()
        val opComment = binding.commentEt.text.toString()
        if (!inputCheck(
                name = name,
                wallet = wallet,
                category = cat,
                opType = rbText,
                opRecipient = opRec,
                opSum = opSum,
                opComment = opComment
            )) {
            return
        }
        if (!walViewModel.isScoreValid(opSum)) {
            binding.totalEt.error = Constants.WALLET_INVALID
            return
        }
        temViewModel.editOperation(
            id = args.template.id,
            name = name,
            walletId = getWalletFromACTV(wallet),
            categoryId = getCategoryFromACTV(cat),
            opType = rbText,
            opRecipient = opRec,
            opSum =  opSum.toFloat(),
            opComment = opComment
        )
        findNavController().popBackStack()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
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

    private fun setTemplate() = with(binding) {
        val curTem = args.template
        templateNameEt.setText(curTem.temp_name)
        senderEt.setText(curTem.temp_recipient)
        totalEt.setText(DecimalFormat("#.###").format(curTem.temp_sum))
        commentEt.setText(curTem.temp_comment)
        if (curTem.temp_variant == Constants.CONS_TEXT) {
            consumptionRb.isChecked = true
            consumptionRb.isChecked = true
        } else {
            incomeRb.isChecked = true
        }
        filledScore.setText(findWalletById(curTem.account))
        filledCategory.setText(findCategoryById(curTem.category))
        initAdapters()
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
            // Template vm
            val templateApi = TemplateApi.getTemplatesApi()!!
            val temRepository = TemplateRepository(templateApi = templateApi, token = token)
            temViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = temRepository,
                    token = token
                )
            )[TemplateViewModel::class.java]
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
        name: String,
        wallet: String,
        category: String,
        opType: String,
        opRecipient: String,
        opSum: String,
        opComment: String
    ): Boolean {
        if (name.isBlank()) {
            binding.templateNameEt.error = Constants.COMP_FIELD
        }
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
        return name.isNotBlank() &&
                wallet.isNotBlank() &&
                category.isNotBlank() &&
                opType.isNotBlank() &&
                opRecipient.isNotBlank() &&
                opSum.isNotBlank() &&
                opComment.isNotBlank()
    }
}