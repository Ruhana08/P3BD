package com.example.notesapp

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesapp.ui.theme.NotesAppTheme

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String
)

@Composable
fun NoteItem(note: Note) {
    ListItem(
        headlineContent = { Text(note.title) },
        supportingContent = { Text("ID: ${note.id}") }
    )
}

@Preview(showBackground = true)
@Composable
fun NoteItemPreview() {
    NotesAppTheme {
        NoteItem(note = Note(id = 1, title = "Belajar Room Database"))
    }
}