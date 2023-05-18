package ru.vsu.cs.tp.richfamily.view.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.GroupListRVAdapter
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ItemClickInterface
import ru.vsu.cs.tp.richfamily.api.service.GroupApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentGroupListBinding
import ru.vsu.cs.tp.richfamily.repository.GroupRepository
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.viewmodel.GroupViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class GroupListFragment :
    Fragment(),
    ClickDeleteInterface,
    ItemClickInterface {
    private lateinit var binding: FragmentGroupListBinding
    private lateinit var adapter: GroupListRVAdapter
    private lateinit var grViewModel: GroupViewModel
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupListBinding.inflate(
            inflater,
            container,
            false
        )
        token = try {
            SessionManager.getToken(requireActivity())!!
        } catch (e: java.lang.NullPointerException) {
            ""
        }
        if (token.isNotEmpty()) {
            val groupApi = GroupApi.getGroupApi()!!
            val grRepository = GroupRepository(groupApi = groupApi, token = token)
            grViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = grRepository,
                    token = token
                )
            )[GroupViewModel::class.java]
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        if (token.isNotBlank()) {
            grViewModel.groupList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            grViewModel.errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
            grViewModel.loading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
            grViewModel.getUsersGroup()
        }
    }

    private fun initRcView() = with(binding) {
        adapter = GroupListRVAdapter(
            this@GroupListFragment,
            this@GroupListFragment
        )
        groupsRv.layoutManager = LinearLayoutManager(context)
        groupsRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {

    }

    override fun onItemClick(id: Int) {
        findNavController()
            .navigate(R.id.action_groupListFragment_to_groupFragment)
    }
}