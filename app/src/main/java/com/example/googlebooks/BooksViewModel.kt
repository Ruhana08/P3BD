package com.example.googlebooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlebooks.data.BookItem
import com.example.googlebooks.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class BooksViewModel : ViewModel(){
    private val _results = MutableStateFlow<List<BookItem>>(emptyList())
    val results: StateFlow<List<BookItem>> = _results.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    companion object{
        private const val API_KEY = "AIzaSyDqviHLxTmkhViiGaMHcZ_3VdKTDu1nRwk"
        private const val MAX_RESULTS = 20
    }
    fun search(query: String) {
        val q = query.trim()
        if (q.isEmpty()) {
            _error.value = "Kata kunci tidak boleh kosong"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = ApiClient.api.searchVolumes(q, MAX_RESULTS, API_KEY)
                val books = response.items.orEmpty().distinctBy { it.id }
                _results.value = books
                if (books.isEmpty()) _error.value = "Buku tidak ditemukan"
            } catch (e: IOException) {
                _error.value = "Koneksi error, periksa internet Anda"
                _results.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}