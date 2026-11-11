
package com.example.countercompose_pertemuan11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.countercompose_pertemuan11.ui.theme.CounterCompose_pertemuan11Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CounterCompose_pertemuan11Theme {
                var halamanDataDiri by remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (halamanDataDiri) {
                        DataDiriScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        CounterApp(
                            modifier = Modifier.padding(innerPadding),
                            onNext = {
                                halamanDataDiri = true
                            }

                        )
                    }
                }
            }
        }
    }
}
