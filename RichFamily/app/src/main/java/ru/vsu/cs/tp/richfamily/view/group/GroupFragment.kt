package ru.vsu.cs.tp.richfamily.view.group

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.GroupUserRVAdapter
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ItemClickInterface
import ru.vsu.cs.tp.richfamily.api.model.group.GroupUser
import ru.vsu.cs.tp.richfamily.api.service.GroupApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentGroupBinding
import ru.vsu.cs.tp.richfamily.repository.GroupRepository
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        getCons()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCons() {
        CoroutineScope(Dispatchers.IO).launch {
//            val list = App.serviceAPI.getWallets(loginViewModel.token.value!!)
            val list = mutableListOf(
                GroupUser(0, "Иван", "Иванов"),
                GroupUser(1, "София", "Иванова"),
                GroupUser(2, "Петр", "Иванова")
            )
            requireActivity().runOnUiThread {
                adapter.submitList(list)
            }
        }
    }

    private fun initRcView() = with(binding) {
        adapter = GroupUserRVAdapter(
            this@GroupFragment,
            this@GroupFragment)
        usersRv.layoutManager = LinearLayoutManager(context)
        usersRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {

    }

    override fun onItemClick(id: Int) {
        findNavController()
            .navigate(R.id.action_groupFragment_to_userOperationFragment)
    }
}