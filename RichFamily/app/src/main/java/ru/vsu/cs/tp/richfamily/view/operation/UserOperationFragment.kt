package ru.vsu.cs.tp.richfamily.view.operation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ru.vsu.cs.tp.richfamily.adapter.OperationClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.OperationClickEditInterface
import ru.vsu.cs.tp.richfamily.adapter.OperationRVAdapter
import ru.vsu.cs.tp.richfamily.api.service.OperationApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentUserOperationBinding
import ru.vsu.cs.tp.richfamily.repository.OperationRepository
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.viewmodel.OperationViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class UserOperationFragment :
    Fragment(),
    OperationClickDeleteInterface,
    OperationClickEditInterface {

    private lateinit var binding: FragmentUserOperationBinding
    private lateinit var adapter: OperationRVAdapter
    private lateinit var opViewModel: OperationViewModel
    private val args by navArgs<UserOperationFragmentArgs>()
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        token = try {
            SessionManager.getToken(requireActivity())!!
        } catch (e: java.lang.NullPointerException) {
            ""
        }
        binding = FragmentUserOperationBinding.inflate(
            inflater,
            container,
            false
        )
        val operationApi = OperationApi.getOperationApi()!!
        val opRepository = OperationRepository(operationApi = operationApi, token = token)
        opViewModel = ViewModelProvider(
            requireActivity(),
            AnyViewModelFactory(
                repository = opRepository,
                token = token
            )
        )[OperationViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        if (token.isNotBlank()) {
            opViewModel.opList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            opViewModel.errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
            opViewModel.loading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
            opViewModel.getAllOperations()
        }
    }

    private fun initRcView() = with(binding) {
        adapter = OperationRVAdapter(
            this@UserOperationFragment,
            this@UserOperationFragment,
            true)
        operationsRv.layoutManager = LinearLayoutManager(context)
        operationsRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) { }

    override fun onEditIconClick(id: Int) { }
}