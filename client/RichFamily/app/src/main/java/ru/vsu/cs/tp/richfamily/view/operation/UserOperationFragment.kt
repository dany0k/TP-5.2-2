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
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.adapter.OperationClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.OperationClickEditInterface
import ru.vsu.cs.tp.richfamily.adapter.OperationRVAdapter
import ru.vsu.cs.tp.richfamily.api.service.GroupApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentUserOperationBinding
import ru.vsu.cs.tp.richfamily.repository.GroupRepository
import ru.vsu.cs.tp.richfamily.viewmodel.GroupViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class UserOperationFragment :
    Fragment(),
    OperationClickDeleteInterface,
    OperationClickEditInterface {

    private lateinit var binding: FragmentUserOperationBinding
    private lateinit var adapter: OperationRVAdapter
    private lateinit var grViewModel: GroupViewModel
    private val args by navArgs<UserOperationFragmentArgs>()
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        token = MainActivity.getToken()
        binding = FragmentUserOperationBinding.inflate(
            inflater,
            container,
            false
        )
        val groupApi = GroupApi.getGroupApi()!!
        val grRepository = GroupRepository(groupApi = groupApi, token = token)
        grViewModel = ViewModelProvider(
            requireActivity(),
            AnyViewModelFactory(
                repository = grRepository,
                token = token
            )
        )[GroupViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        if (token.isNotBlank()) {
            grViewModel.opList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            grViewModel.errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
            grViewModel.loading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.content.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.content.visibility = View.VISIBLE
                }
            }
            grViewModel.getUsersOperations(args.userId, args.groupId)
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