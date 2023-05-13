package ru.vsu.cs.tp.richfamily.view.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.*
import ru.vsu.cs.tp.richfamily.api.service.TemplateApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentTemplateBinding
import ru.vsu.cs.tp.richfamily.repository.TemplateRepository
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.viewmodel.TemplateViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class TemplateFragment :
    Fragment(),
    TemplateClickDeleteInterface,
    TemplateClickEditInterface,
    TemplateItemClickInterface {

    private lateinit var adapter: TemplateRVAdapter
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var temViewModel: TemplateViewModel
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_template,
            container,
            false
        )
        token = try {
            SessionManager.getToken(requireActivity())!!
        } catch (e: java.lang.NullPointerException) {
            ""
        }
        if (token.isNotEmpty()) {
            val templateApi = TemplateApi.getTemplatesApi()!!
            val temRepository = TemplateRepository(templateApi = templateApi, token = token)
            temViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = temRepository,
                    token = token
                )
            )[TemplateViewModel::class.java]
        }
        initRcView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        temViewModel.templatesList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        temViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }
        temViewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
        temViewModel.getAllTemplates()
        binding.fab.setOnClickListener {
            findNavController()
                .navigate(R.id.action_templateFragment_to_addTemplateFragment)
        }
    }

    private fun initRcView() = with(binding) {
        adapter = TemplateRVAdapter(
            this@TemplateFragment,
            this@TemplateFragment,
            this@TemplateFragment
        )
        walletsRv.layoutManager = LinearLayoutManager(context)
        walletsRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {
        temViewModel.deleteTemplate(id = id)
    }

    override fun onEditIconClick(id: Int) {
        val action = TemplateFragmentDirections
            .actionTemplateFragmentToUpdateTemplateFragment(adapter.currentList[id])
        findNavController()
            .navigate(action)
    }

    override fun onItemClick(id: Int) {
        val action = TemplateFragmentDirections
            .actionTemplateFragmentToAddOperationFragment(adapter.currentList[id])
        findNavController()
            .navigate(action)
    }
}