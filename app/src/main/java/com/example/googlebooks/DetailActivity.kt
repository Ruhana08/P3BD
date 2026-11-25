package com.example.googlebooks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.googlebooks.data.BookItem
import com.example.googlebooks.ui.theme.GoogleBooksTheme
class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
// Membaca data yang dikirim dari MainActivity
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "-"
        val authors = intent.getStringExtra(EXTRA_AUTHORS) ?: "-"
        val cover = intent.getStringExtra(EXTRA_COVER) ?: ""
        val publisher = intent.getStringExtra(EXTRA_PUBLISHER) ?: "-"
        val date = intent.getStringExtra(EXTRA_DATE) ?: "-"
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: "-"
        setContent {
            GoogleBooksTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DetailScreen(
                        title, authors, cover, publisher, date, description,

                        onBack = { finish() },
                        modifier = Modifier.padding(innerPadding)

                    )
                }
            }
        }
    }
    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_AUTHORS = "extra_authors"
        const val EXTRA_COVER = "extra_cover"
        const val EXTRA_PUBLISHER = "extra_publisher"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_DESCRIPTION = "extra_description"
        // Membuat Intent berisi data buku yang dipilih
        fun newIntent(context: Context, item: BookItem): Intent {
            val info = item.volumeInfo
            val cover = secureImageUrl(
                info.imageLinks?.thumbnail ?: info.imageLinks?.smallThumbnail
            )
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_TITLE, info.title ?: "-")
                putExtra(EXTRA_AUTHORS, info.authors?.joinToString(", ") ?: "-")
                putExtra(EXTRA_COVER, cover ?: "")
                putExtra(EXTRA_PUBLISHER, info.publisher ?: "-")
                putExtra(EXTRA_DATE, info.publishedDate ?: "-")
                putExtra(EXTRA_DESCRIPTION, info.description ?: "Tidak ada deskripsi.")
            }
        }
    }
}
@Composable
fun DetailScreen(
    title: String,
    authors: String,
    cover: String,
    publisher: String,
    date: String,
    description: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
            Text("← Kembali")
        }
        AsyncImage(
            model = cover.ifEmpty { null },
            contentDescription = "Cover $title",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(140.dp)
                .height(210.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            authors,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Text("$publisher • $date", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))
        Text(
            "Deskripsi",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text(
            description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )
    }
}
