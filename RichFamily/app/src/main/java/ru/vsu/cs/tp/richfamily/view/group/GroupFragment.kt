package ru.vsu.cs.tp.richfamily.view.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ru.vsu.cs.tp.richfamily.adapter.GroupUserRVAdapter
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ItemClickInterface
import ru.vsu.cs.tp.richfamily.api.service.GroupApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentGroupBinding
import ru.vsu.cs.tp.richfamily.repository.GroupRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.viewmodel.GroupViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class GroupFragment :
    Fragment(),
    ClickDeleteInterface,
    ItemClickInterface {

    private lateinit var binding: FragmentGroupBinding
    private lateinit var adapter: GroupUserRVAdapter
    private lateinit var grViewModel: GroupViewModel
    private val args by navArgs<GroupFragmentArgs>()
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupBinding.inflate(
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
        grViewModel.isLeader(args.groupId)
        grViewModel.isLeader.observe(viewLifecycleOwner) { isLeader ->
            initRcView(isLeader)
            grViewModel.usersList.observe(viewLifecycleOwner) {
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
        }
        grViewModel.getAllUsersFromGroup(args.groupId)
    }

    private fun initRcView(isLeader: Boolean) = with(binding) {
        adapter = GroupUserRVAdapter(
            this@GroupFragment,
            this@GroupFragment,
            isLeader)
        usersRv.layoutManager = LinearLayoutManager(context)
        usersRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {
        grViewModel.deleteUserFromGroup(groupId = args.groupId, userId = id)
    }

    override fun onItemClick(id: Int) {
        val action = GroupFragmentDirections.actionGroupFragmentToUserOperationFragment(id)
        findNavController().navigate(action)
    }
}