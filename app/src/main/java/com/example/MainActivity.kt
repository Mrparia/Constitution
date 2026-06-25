package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.ConstitutionApp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ConstitutionViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: ConstitutionViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        ConstitutionApp(viewModel = viewModel)
      }
    }
  }
}
