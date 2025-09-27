package com.example.harjoitustyo.ui_weatherapp

import com.example.harjoitustyo.R
import android.R.attr.fontWeight
import android.R.attr.text
import android.R.id.bold
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.harjoitustyo.ui.theme.displayFontFamily

@Composable
fun WelcomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.weather_mix_24px),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(56.dp))
        Text("Harjoitustyö", fontSize = 48.sp, style = MaterialTheme.typography.displayLarge)
        Text("Sääasema", fontSize = 32.sp, style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(32.dp))
        IconButton(onClick = { navController.navigate("home") }, modifier = Modifier.size(96.dp) ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Get Started", modifier = Modifier.size(96.dp))

        }
    }
}




