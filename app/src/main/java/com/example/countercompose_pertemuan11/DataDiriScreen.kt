
package com.example.countercompose_pertemuan11

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DataDiriScreen(
    modifier: Modifier = Modifier
) {
    var nama by rememberSaveable { mutableStateOf("") }
    var umur by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Data Diri",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Masukkan Nama") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = umur,
            onValueChange = { umur = it },
            label = { Text("Masukkan Umur") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Nama saya \"$nama\" dan umur saya \"$umur\", Salam kenal!",
            fontSize = 16.sp
        )
    }
}
