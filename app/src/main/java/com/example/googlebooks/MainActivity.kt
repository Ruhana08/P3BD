package com.example.googlebooks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.googlebooks.data.BookItem
import com.example.googlebooks.ui.theme.GoogleBooksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GoogleBooksTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BooksScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BooksScreen(
    modifier: Modifier = Modifier,
    vm: BooksViewModel = viewModel()
) {
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current

    val results by vm.results.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()

    var query by rememberSaveable { mutableStateOf("") }

    val doSearch = {
        keyboard?.hide()
        vm.search(query)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Cari Buku",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        // Input teks + tombol trigger pencarian
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Judul, penulis, atau topik") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { doSearch() }),
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { doSearch() },
                enabled = !isLoading
            ) {
                Text("Cari")
            }
        }

        Spacer(Modifier.height(12.dp))

        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        error?.let { msg ->
            Text(
                msg,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Daftar hasil pencarian
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(results, key = { it.id }) { item ->
                BookRow(
                    item = item,
                    onClick = {
                        // Buka halaman detail dan kirim data buku via Intent
                        context.startActivity(DetailActivity.newIntent(context, item))

                    }
                )
            }
        }
    }
}

@Composable
fun BookRow(item: BookItem, onClick: () -> Unit) {
    val info = item.volumeInfo
    val authors = info.authors?.joinToString(", ") ?: "Penulis tidak diketahui"

    val cover = secureImageUrl(
        info.imageLinks?.thumbnail ?: info.imageLinks?.smallThumbnail
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = cover,
                contentDescription = "Cover ${info.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(60.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.LightGray)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    info.title ?: "(Tanpa judul)",
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    authors,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )

                Text(
                    info.publishedDate?.take(4) ?: "-",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Mengubah URL gambar HTTP menjadi HTTPS
fun secureImageUrl(url: String?): String? {
    return url?.replace("http://", "https://")
}