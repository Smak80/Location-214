package ru.smak.location_214

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.smak.location_214.ui.theme.Location214Theme

class MainActivity : ComponentActivity() {
    private val launcher2 = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){}
    private val launcher1 = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){
        launcher2.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Location214Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        launcher1.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
    }

    fun requestMissingPermissions(
        permissions: Array<String>,
        results: (Map<String, Boolean>)->Unit
    ){
        if (
            permissions.fold(false){ all,p ->
                all || checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED
            }
        ){
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Location214Theme {
        Greeting("Android")
    }
}