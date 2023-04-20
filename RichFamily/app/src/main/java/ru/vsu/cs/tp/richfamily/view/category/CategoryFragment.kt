package ru.vsu.cs.tp.richfamily.view.category

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.CategoryClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.CategoryClickEditInterface
import ru.vsu.cs.tp.richfamily.adapter.CategoryRVAdapter
import ru.vsu.cs.tp.richfamily.api.model.CategoryRequestBody
import ru.vsu.cs.tp.richfamily.app.App.Companion.categoryService
import ru.vsu.cs.tp.richfamily.app.App
import ru.vsu.cs.tp.richfamily.databinding.CategoryDialogBinding
import ru.vsu.cs.tp.richfamily.databinding.FragmentCategoryBinding
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel

class CategoryFragment :
    Fragment(),
    CategoryClickDeleteInterface,
    CategoryClickEditInterface {
    private lateinit var adapter: CategoryRVAdapter
    private lateinit var loginViewModel : LoginViewModel
    private lateinit var binding: FragmentCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(requireActivity(),
            ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application))[LoginViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.initRetrofit()
        initRcView()
        if (loginViewModel.isToken()) {
            getCategories()
        }

        binding.addCategoryFab.setOnClickListener {
            if (!loginViewModel.isToken()) {
                findNavController()
                    .navigate(R.id.action_categoryFragment_to_registrationFragment)
            } else {
                val builder = AlertDialog.Builder(context)
                val dialogBinding = CategoryDialogBinding.inflate(
                    layoutInflater,
                    null,
                    false
                )
                builder.setView(dialogBinding.root)
                builder.setPositiveButton(R.string.add) { _, _ ->
                    val catName = dialogBinding.categoryNameEt.text.toString()
                    addCategory(catName)
                    getCategories()
                }
                builder.setNegativeButton(R.string.cancel) { _, _ ->
                    onDestroyView()
                }
                builder.show()
            }
        }
    }

    private fun getCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = categoryService.getCategories(loginViewModel.token.value!!)
            requireActivity().runOnUiThread {
                adapter.submitList(list)
            }
        }
    }

    private fun addCategory(catName: String) {
        if (catName.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            categoryService.addCategory(
                loginViewModel.token.value!!,
                CategoryRequestBody(catName)
            )
            requireActivity().runOnUiThread {

                Toast.makeText(
                    requireActivity(),
                    R.string.succesful_add,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateCategory(catName: String, id: Int) {
        if (catName.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            categoryService.updateCategory(
                loginViewModel.token.value!!,
                CategoryRequestBody(catName),
                id
            )
            requireActivity().runOnUiThread {

                Toast.makeText(
                    requireActivity(),
                    R.string.succesful_update,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initRcView() = with(binding) {
        adapter = CategoryRVAdapter(
            this@CategoryFragment,
            this@CategoryFragment
        )
        categoryRv.layoutManager = LinearLayoutManager(context)
        categoryRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoryService.deleteCategory(loginViewModel.token.value!!, id)
            if (response.isSuccessful) {
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireActivity(),
                        R.string.succesful_delete,
                        Toast.LENGTH_SHORT
                    ).show()
                    getCategories()
                }
            } else {
                Toast.makeText(
                    requireActivity(),
                    R.string.error_delete,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onEditIconClick(id: Int) {
        val builder = AlertDialog.Builder(context)
        val dialogBinding = CategoryDialogBinding.inflate(
            layoutInflater,
            null,
            false
        )
        builder.setView(dialogBinding.root)
        builder.setPositiveButton(R.string.edit_button_label) { _, _ ->
            val catName = dialogBinding.categoryNameEt.text.toString()
            updateCategory(catName, id)
            getCategories()
        }
        builder.setNegativeButton(R.string.cancel) { _, _ ->
            onDestroyView()
        }
        builder.show()
    }
}