package ru.smak.location_214

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.smak.location_214.locating.LocationHelper
import ru.smak.location_214.ui.theme.Location214Theme

class MainActivity : ComponentActivity() {

    //private lateinit var dbh: DbHelper
    private val dbh by lazy { DbHelper(applicationContext) }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){
        parseResults(it)
    }

    private val lh by lazy{ LocationHelper(::showLocation) }
    private val mvm by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun parseResults(results: Map<String, Boolean>) {
        try {
            var mainGranted = true
            results.forEach { (k, v) ->
                mainGranted = if (k == Manifest.permission.ACCESS_COARSE_LOCATION
                    || k == Manifest.permission.ACCESS_FINE_LOCATION
                ) mainGranted and v
                else false
            }
            if (mainGranted) {
                launcher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                lh.start(this)
            }
        } catch (e: Throwable){
            Log.e("LOC", "ERROR $e")
        }
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
                    LocationInfo()
                }
            }
        }
        if (requestMissingPermissions(arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ))){
            if (requestMissingPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))){
                lh.start(this)
            }
        }
        //dbh = DbHelper(applicationContext)
        dbh.getLocations().forEach {
            Log.i("REC_LOC", it)
        }
    }

    private fun requestMissingPermissions(permissions: Array<String>): Boolean {
        if (permissions.fold(false){ all,p -> all || checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED
        }){
            launcher.launch(permissions)
            return false
        }
        return true
    }

    private fun showLocation(l: Location){
        Log.i("LOC", "LAT=${l.latitude} LON=${l.longitude}")
        dbh.addLocation(l)
        mvm.location = l
    }
}

@Composable
fun LocationList(modifier: Modifier = Modifier){
    LazyColumn(content = {
        this.item {
            LocationInfo(Modifier.fillMaxWidth())
        }
    }, modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationInfo(modifier: Modifier = Modifier){
    val mvm = viewModel(MainViewModel::class.java)
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(5)
    ) {
        Text(stringResource(id = R.string.location, mvm.location.latitude, mvm.location.longitude))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Location214Theme {
        //Greeting("Android")
        LocationInfo(Modifier.fillMaxWidth())
    }
}