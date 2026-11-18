package com.example.notesapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    vm: NotesViewModel = viewModel()
) {
    val notes by vm.notes.collectAsState()

    var inputTitle by rememberSaveable { mutableStateOf("") }
    var inputNote by rememberSaveable { mutableStateOf("") }

    var editingId by rememberSaveable { mutableStateOf<Long?>(null) }
    var editingTitle by rememberSaveable { mutableStateOf("") }
    var editingNote by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Catatan Saya",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        // FORM TAMBAH
        OutlinedTextField(
            value = inputTitle,
            onValueChange = { inputTitle = it },
            label = { Text("Judul Catatan") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = inputNote,
            onValueChange = { inputNote = it },
            label = { Text("Isi Catatan") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                vm.addNote(inputTitle, inputNote)
                inputTitle = ""
                inputNote = ""
            },
            enabled = inputTitle.isNotBlank() && inputNote.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text("Tambah Catatan")
        }

        Text(
            text = "Jumlah catatan: ${notes.size}",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // DAFTAR CATATAN
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada catatan", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(notes, key = { it.id }) { note ->
                    if (editingId == note.id) {
                        NoteItemEditing(
                            title = editingTitle,
                            note = editingNote,
                            onTitleChange = { editingTitle = it },
                            onNoteChange = { editingNote = it },
                            onSave = {
                                vm.updateNote(note, editingTitle, editingNote)
                                editingId = null
                            },
                            onCancel = {
                                editingId = null
                            }
                        )
                    } else {
                        NoteItem(
                            note = note,
                            onEdit = {
                                editingId = note.id
                                editingTitle = note.title
                                editingNote = note.note
                            },
                            onDelete = {
                                vm.deleteNote(note)
                            }
                        )
                    }

                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = note.title,
                fontWeight = FontWeight.Bold
            )
        },
        supportingContent = {
            Column {
                Text("ID: ${note.id}")
                Text(note.note)
            }
        },
        trailingContent = {
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "Edit",
                        tint = Color(0xFF2196F3)
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Hapus",
                        tint = Color(0xFFF44336)
                    )
                }
            }
        }
    )
}

@Composable
fun NoteItemEditing(
    title: String,
    note: String,
    onTitleChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Ubah Judul") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = note,
            onValueChange = onNoteChange,
            label = { Text("Ubah Isi Catatan") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onSave) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = "Simpan",
                    tint = Color(0xFF4CAF50)
                )
            }

            IconButton(onClick = onCancel) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Batal"
                )
            }
        }
    }
}
