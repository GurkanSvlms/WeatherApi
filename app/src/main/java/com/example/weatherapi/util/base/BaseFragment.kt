package com.example.weatherapi.util.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import java.lang.IllegalArgumentException

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding as VB
    protected abstract val viewModel: VM

    protected abstract fun onCreateFinished() // onViewCreated içine tanımlanacak diğer fonksiyonlar eklenir.
    protected abstract fun initListeners() // view'lar için event kodlarını yazarız. Örnek edittext okuma, buton tıklanma vs.
    protected abstract fun observeEvents() // Viewmodel'dan gelen dataları gözlemler.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null) {
            throw IllegalArgumentException("Binding is null")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateFinished()
        initListeners()
        observeEvents()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}