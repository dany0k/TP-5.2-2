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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yandex.metrica.YandexMetrica
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.GroupListRVAdapter
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ItemClickInterface
import ru.vsu.cs.tp.richfamily.api.service.GroupApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentGroupListBinding
import ru.vsu.cs.tp.richfamily.databinding.GroupAddDialogBinding
import ru.vsu.cs.tp.richfamily.repository.GroupRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.YandexEvents
import ru.vsu.cs.tp.richfamily.viewmodel.GroupViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class GroupListFragment :
    Fragment(),
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
        initRcView()
        if (token.isNotBlank()) {
            grViewModel.groupList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
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
            grViewModel.getUsersGroup()
        }
        binding.fab.setOnClickListener {
            if (token.isBlank()) {
                findNavController()
                    .navigate(R.id.action_groupListFragment_to_registrationFragment)
            } else {
                createDialog()
            }
        }

        binding.groupsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.fab.hide()
                } else if (dy < 0) {
                    binding.fab.show()
                }
            }
        })
    }

    private fun createDialog() {
        val builder = AlertDialog.Builder(context)
        val dialogBinding = GroupAddDialogBinding.inflate(
            layoutInflater,
            null,
            false
        )
        builder.setView(dialogBinding.root)
        builder.setPositiveButton(R.string.add) { _, _ ->
            val grName = dialogBinding.grNameEt.text.toString()
            if (!grViewModel.isNameValid(grName)) {
                showToast(Constants.INVALID_GROUP_NAME)
            } else {
                grViewModel.addGroup(grName)
                YandexMetrica.reportEvent(YandexEvents.ADD_USER_IN_GROUP)
            }
        }
        builder.setNegativeButton(R.string.cancel) { _, _ ->
            onDestroyView()
        }
        builder.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun initRcView() = with(binding) {
        adapter = GroupListRVAdapter(
            this@GroupListFragment
        )
        groupsRv.layoutManager = LinearLayoutManager(context)
        groupsRv.adapter = adapter
    }

    override fun onItemClick(id: Int) {
        val action = GroupListFragmentDirections.actionGroupListFragmentToGroupFragment(id)
        findNavController()
            .navigate(action)
    }
}