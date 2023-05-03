package ru.vsu.cs.tp.richfamily.view.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.*
import ru.vsu.cs.tp.richfamily.databinding.FragmentTemplateBinding
import ru.vsu.cs.tp.richfamily.model.Template

class TemplateFragment :
    Fragment(),
    TemplateClickDeleteInterface,
    TemplateClickEditInterface {

    private lateinit var adapter: TemplateRVAdapter
    lateinit var binding: FragmentTemplateBinding

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        getTemplates()
        binding.fab.setOnClickListener {
            findNavController()
                .navigate(R.id.action_templateFragment_to_addTemplateFragment)
        }
    }

    private fun getTemplates() {
        CoroutineScope(Dispatchers.IO).launch {
//            val list = App.serviceAPI.getWallets(loginViewModel.token.value!!)
            val list = mutableListOf(
                Template(0, 0, "Шаблон 1", "Расход", "Мама", 1000f),
                Template(0, 1, "Шаблон 2", "Расход", "Мама", 1200f),
                Template(0, 2, "Шаблон 3", "Доход", "Мама", 1300f),
            )
            requireActivity().runOnUiThread {
                adapter.submitList(list)
            }
        }
    }

    private fun initRcView() = with(binding) {
        adapter = TemplateRVAdapter(
            this@TemplateFragment,
            this@TemplateFragment
        )
        walletsRv.layoutManager = LinearLayoutManager(context)
        walletsRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {
    }

    override fun onEditIconClick(id: Int) {
        findNavController()
            .navigate(R.id.action_templateFragment_to_updateTemplateFragment)
    }
}