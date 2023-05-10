package ru.vsu.cs.tp.richfamily.view.group

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.GroupListRVAdapter
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ItemClickInterface
import ru.vsu.cs.tp.richfamily.api.model.Group
import ru.vsu.cs.tp.richfamily.app.App
import ru.vsu.cs.tp.richfamily.databinding.FragmentGroupListBinding

class GroupListFragment :
    Fragment(),
    ClickDeleteInterface,
    ItemClickInterface {

    private lateinit var binding: FragmentGroupListBinding
    private lateinit var adapter: GroupListRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupListBinding.inflate(
            inflater,
            container,
            false
        )
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
                Group(0, "Семья"),
                Group(0, "Работа")
            )
            requireActivity().runOnUiThread {
                adapter.submitList(list)
            }
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