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
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.CategoryClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.CategoryClickEditInterface
import ru.vsu.cs.tp.richfamily.adapter.CategoryRVAdapter
import ru.vsu.cs.tp.richfamily.api.service.CategoryApi
import ru.vsu.cs.tp.richfamily.databinding.CategoryDialogBinding
import ru.vsu.cs.tp.richfamily.databinding.FragmentCategoryBinding
import ru.vsu.cs.tp.richfamily.databinding.SubmitDialogBinding
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class CategoryFragment:
    Fragment(),
    CategoryClickDeleteInterface,
    CategoryClickEditInterface {
    private lateinit var adapter: CategoryRVAdapter
    private lateinit var catViewModel: CategoryViewModel
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(
            inflater,
            container,
            false
        )
        token = MainActivity.getToken()
        if (token.isNotEmpty()) {
            val categoryApi = CategoryApi.getCategoryApi()!!

            val categoryRepository = CategoryRepository(categoryApi = categoryApi, token = token)
            catViewModel = ViewModelProvider(
                this,
                AnyViewModelFactory(
                    repository = categoryRepository,
                    token = token
                )
            )[CategoryViewModel::class.java]
        }
        initRcView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (token.isNotBlank()) {
            catViewModel.catList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            catViewModel.errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
            catViewModel.loading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.content.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.content.visibility = View.VISIBLE
                }
            }
            catViewModel.getAllCategories()
        }
        binding.addCategoryFab.setOnClickListener {
            if (token.isEmpty()) {
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
                    catViewModel.addCategory(catName)
                }
                builder.setNegativeButton(R.string.cancel) { _, _ ->
                    onDestroyView()
                }
                builder.show()
            }
        }

        binding.categoryRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.addCategoryFab.hide()
                } else if (dy < 0) {
                    binding.addCategoryFab.show()
                }
            }
        })
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
        createDialog(id = id)
        binding.addCategoryFab.show()
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
            catViewModel.editCategory(catName = catName, id = id)
        }
        builder.setNegativeButton(R.string.cancel) { _, _ ->
            onDestroyView()
        }
        builder.show()
    }

    private fun createDialog(id: Int) {
        val builder = AlertDialog.Builder(context)
        val dialogBinding = SubmitDialogBinding.inflate(
            layoutInflater,
            null,
            false
        )
        builder.setView(dialogBinding.root)
        val submitText: String = Constants.SUBMIT_CAT_DELETE_TEXT
        dialogBinding.textToSubmit.text = submitText
        builder.setPositiveButton(R.string.accept) { _, _ ->
            catViewModel.deleteCategory(id = id)
        }
        builder.setNegativeButton(R.string.cancel) { _, _ ->
            onDestroyView()
        }
        builder.show()
    }
}