package ru.vsu.cs.tp.richfamily.view.report

import android.content.ContentValues
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yandex.metrica.YandexMetrica
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.OperationClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.OperationClickEditInterface
import ru.vsu.cs.tp.richfamily.adapter.OperationRVAdapter
import ru.vsu.cs.tp.richfamily.api.model.operation.Operation
import ru.vsu.cs.tp.richfamily.api.service.OperationApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentReportBinding
import ru.vsu.cs.tp.richfamily.repository.OperationRepository
import ru.vsu.cs.tp.richfamily.utils.YandexEvents
import ru.vsu.cs.tp.richfamily.viewmodel.OperationViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class ReportFragment :
    Fragment(),
    OperationClickDeleteInterface,
    OperationClickEditInterface {
    private lateinit var adapter: OperationRVAdapter
    private lateinit var binding: FragmentReportBinding
    private lateinit var opViewModel: OperationViewModel
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(
            inflater,
            container,
            false
        )
        token = MainActivity.getToken()
        if (token.isNotEmpty()) {
            val operationApi = OperationApi.getOperationApi()!!
            val opRepository = OperationRepository(operationApi = operationApi, token = token)
            opViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = opRepository,
                    token = token
                )
            )[OperationViewModel::class.java]
        } else {
            binding.saveReportButton.visibility = View.GONE
        }
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
                    binding.content.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.content.visibility = View.VISIBLE
                }
            }
            opViewModel.getAllOperations()
        }
        binding.saveReportButton.setOnClickListener {
            YandexMetrica.reportEvent(YandexEvents.SAVE_REPORT)
            binding.saveReportButton.startAnimation()
            opViewModel.consList.observe(viewLifecycleOwner) { cons ->
                opViewModel.inList.observe(viewLifecycleOwner) { incs ->
                    createCsvFile(cons = cons, incs = incs)
                }
            }
            binding.saveReportButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner)
            binding.saveReportButton.revertAnimation()
        }
    }

    private fun createCsvFile(cons: List<Operation>, incs: List<Operation>) {
        val fileName = "report.csv"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }
        val resolver = requireContext().contentResolver
        val uri = resolver.insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), contentValues)

        uri?.let { fileUri ->
            resolver.openOutputStream(fileUri)?.use { outputStream ->
                val writer = outputStream.bufferedWriter()
                writer.write("Расходы")
                writer.newLine()
                writer.write("Номер,Операции,Дата,Получатель/Получатель,Сумма,Комментарий")
                writer.newLine()
                for (el in cons) {
                    writer.write("${el.id},${el.op_variant},${el.op_date},${el.op_recipient},${el.op_sum},${el.op_comment}")
                    writer.newLine()
                }
                writer.write("Доходы")
                writer.newLine()
                writer.write("Номер,Операции,Дата,Получатель/Получатель,Сумма,Комментарий")
                writer.newLine()
                for (el in incs) {
                    writer.write("${el.id},${el.op_variant},${el.op_date},${el.op_recipient},${el.op_sum},${el.op_comment}")
                    writer.newLine()
                }
                writer.close()
                Toast.makeText(requireContext(), "CSV файл сохранен", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "Ошибка при создании файла", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRcView() = with(binding) {
        adapter = OperationRVAdapter(
            this@ReportFragment,
            this@ReportFragment,
            true
        )
        operationsRv.layoutManager = LinearLayoutManager(context)
        operationsRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {}

    override fun onEditIconClick(id: Int) {}

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}