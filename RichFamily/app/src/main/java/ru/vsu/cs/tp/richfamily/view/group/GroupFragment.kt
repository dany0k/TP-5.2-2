package ru.vsu.cs.tp.richfamily.view.group

import android.app.AlertDialog
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
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.GroupUserRVAdapter
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ItemClickInterface
import ru.vsu.cs.tp.richfamily.api.service.GroupApi
import ru.vsu.cs.tp.richfamily.databinding.AddUserDialogBinding
import ru.vsu.cs.tp.richfamily.databinding.FragmentGroupBinding
import ru.vsu.cs.tp.richfamily.databinding.SubmitDialogBinding
import ru.vsu.cs.tp.richfamily.repository.GroupRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
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
        token = MainActivity.getToken()
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
            if (!isLeader) {
                binding.fab.visibility = View.GONE
                binding.leaveGroupButton.visibility = View.VISIBLE
            } else {
                binding.deleteGroupButton.visibility = View.VISIBLE
            }
            grViewModel.usersList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            grViewModel.leaderUser.observe(viewLifecycleOwner) {
                val leaderName = "${it.first_name} ${it.last_name}"
                binding.leaderNameTv.text = leaderName
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
        binding.leaderCv.setOnClickListener {
            showOperations(grViewModel.leaderUser.value!!.id)
        }
        binding.fab.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val dialogBinding = AddUserDialogBinding.inflate(
                layoutInflater,
                null,
                false
            )
            builder.setView(dialogBinding.root)
            builder.setPositiveButton(R.string.add) { _, _ ->
                val userEmail = dialogBinding.userEmailEt.text.toString()
                grViewModel.addUserInGroup(args.groupId, userEmail)
            }
            builder.setNegativeButton(R.string.cancel) { _, _ ->
                onDestroyView()
            }
            builder.show()
        }
        binding.deleteGroupButton.setOnClickListener {
            createDeleteDialog(id = args.groupId)
        }
        binding.leaveGroupButton.setOnClickListener {
            createLeaveDialog(id = args.groupId)
        }
    }

    private fun createDeleteDialog(id: Int) {
        val builder = AlertDialog.Builder(context)
        val dialogBinding = SubmitDialogBinding.inflate(
            layoutInflater,
            null,
            false
        )
        builder.setView(dialogBinding.root)
        val submitText: String = Constants.DELETE_GROUP_MESSAGE
        dialogBinding.textToSubmit.text = submitText
        builder.setPositiveButton(R.string.accept) { _, _ ->
            grViewModel.deleteGroup(groupId = id)
            findNavController().popBackStack()
            showToast(Constants.SUCCESS)
        }
        builder.setNegativeButton(R.string.cancel) { _, _ ->
            onDestroyView()
        }
        builder.show()
    }

    private fun createLeaveDialog(id: Int) {
        val builder = AlertDialog.Builder(context)
        val dialogBinding = SubmitDialogBinding.inflate(
            layoutInflater,
            null,
            false
        )
        builder.setView(dialogBinding.root)
        val submitText: String = Constants.LEAVE_GROUP_MESSAGE
        dialogBinding.textToSubmit.text = submitText
        builder.setPositiveButton(R.string.accept) { _, _ ->
            grViewModel.leaveFromGroup(groupId = id)
            findNavController().popBackStack()
            showToast(Constants.SUCCESS)
        }
        builder.setNegativeButton(R.string.cancel) { _, _ ->
            onDestroyView()
        }
        builder.show()
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun showOperations(id: Int) {
        val action = GroupFragmentDirections.actionGroupFragmentToUserOperationFragment(id, args.groupId)
        findNavController().navigate(action)
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
        createDialog(id = id)
    }

    override fun onItemClick(id: Int) {
        showOperations(id = id)
    }

    private fun createDialog(id: Int) {
        val builder = AlertDialog.Builder(context)
        val dialogBinding = SubmitDialogBinding.inflate(
            layoutInflater,
            null,
            false
        )
        builder.setView(dialogBinding.root)
        val submitText: String = Constants.SUBMIT_GROUP_USER_DELETE_TEXT
        dialogBinding.textToSubmit.text = submitText
        builder.setPositiveButton(R.string.accept) { _, _ ->
            grViewModel.deleteUserFromGroup(groupId = args.groupId, userId =  id)
            showToast(Constants.SUCCESS)
        }
        builder.setNegativeButton(R.string.cancel) { _, _ ->
            onDestroyView()
        }
        builder.show()
    }
}