package ru.vsu.cs.tp.richfamily.view.credit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.CreditClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.CreditClickItemInterface
import ru.vsu.cs.tp.richfamily.adapter.CreditRVAdapter
import ru.vsu.cs.tp.richfamily.api.service.CreditApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentCreditListBinding
import ru.vsu.cs.tp.richfamily.repository.CreditRepository
import ru.vsu.cs.tp.richfamily.viewmodel.CreditViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class CreditListFragment:
    Fragment(),
    CreditClickItemInterface,
    CreditClickDeleteInterface {
    private lateinit var adapter: CreditRVAdapter
    private lateinit var creditViewModel: CreditViewModel
    private lateinit var binding: FragmentCreditListBinding
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreditListBinding.inflate(
            inflater,
            container,
            false
        )
        token = MainActivity.getToken()
        if (token.isNotEmpty()) {
            val creditApi = CreditApi.getCreditApi()!!

            val creditRepository = CreditRepository(creditApi = creditApi, token = token)
            creditViewModel = ViewModelProvider(
                this,
                AnyViewModelFactory(
                    repository = creditRepository,
                    token = token
                )
            )[CreditViewModel::class.java]
        }
        initRcView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (token.isNotBlank()) {
            creditViewModel.creditList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            creditViewModel.errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
            creditViewModel.loading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
            creditViewModel.getAllCredits()
        }
        binding.calculateCreditButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_creditListFragment_to_addCreditFragment)
        }
    }

    private fun initRcView() = with(binding) {
        adapter = CreditRVAdapter(
            this@CreditListFragment,
            this@CreditListFragment
        )
        creditsRv.layoutManager = LinearLayoutManager(context)
        creditsRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {
        creditViewModel.deleteCredit(id = id)
    }

    override fun onItemClick(id: Int) {
        val action = CreditListFragmentDirections
            .actionCreditListFragmentToCreditFragment(adapter.currentList[id], true)
        findNavController()
            .navigate(action)
    }
}

