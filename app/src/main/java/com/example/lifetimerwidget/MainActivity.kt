package com.example.lifetimerwidget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.updateAll
import com.example.lifetimerwidget.domain.LifeTimer
import com.example.lifetimerwidget.domain.LifeTimerRepository
import com.example.lifetimerwidget.widget.LifeTimerWidget
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val repository = LifeTimerRepository(this)
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LifeTimerScreen(repository) {
                        val scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO)
                        scope.launch {
                            LifeTimerWidget().updateAll(this@MainActivity)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LifeTimerScreen(repository: LifeTimerRepository, onDataChanged: () -> Unit) {
    var birthDateString by remember { mutableStateOf(repository.getBirthDate().value.toString()) }
    var lifeExpectancyString by remember { mutableStateOf(repository.getLifeExpectancy().years.toString()) }
    var currentDateTime by remember { mutableStateOf(LocalDateTime.now()) }
    var inputError by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            currentDateTime = LocalDateTime.now()
            delay(1000L)
        }
    }

    val remainingLife = remember(currentDateTime, birthDateString, lifeExpectancyString) {
        try {
            val date = LocalDate.parse(birthDateString)
            val years = lifeExpectancyString.toInt()
            val timer = LifeTimer(
                com.example.lifetimerwidget.domain.BirthDate(date),
                com.example.lifetimerwidget.domain.LifeExpectancy(years)
            )
            inputError = ""
            timer.calculateRemainingLife(currentDateTime)
        } catch (e: Exception) {
            inputError = "入力形式が正しくありません (日付: YYYY-MM-DD)"
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "残り寿命",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (remainingLife != null) {
            if (remainingLife.isExpired) {
                Text(text = "寿命を迎えました", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            } else {
                Text(text = "${remainingLife.years} 年", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(text = "${remainingLife.days} 日", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(text = "${remainingLife.hours} 時間", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(text = "${remainingLife.minutes} 分", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(text = "${remainingLife.seconds} 秒", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Text(text = inputError, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = birthDateString,
            onValueChange = { 
                birthDateString = it 
                try {
                    repository.saveBirthDate(LocalDate.parse(it))
                    onDataChanged()
                } catch (e: Exception) {}
            },
            label = { Text("生年月日 (YYYY-MM-DD)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = lifeExpectancyString,
            onValueChange = { 
                lifeExpectancyString = it 
                try {
                    val years = it.toInt()
                    if (years > 0) {
                        repository.saveLifeExpectancy(years)
                        onDataChanged()
                    }
                } catch (e: Exception) {}
            },
            label = { Text("想定寿命 (歳)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "※ホーム画面のウィジェットはバッテリー保護のため毎秒更新されません。最新の秒数を確認するにはウィジェットの更新ボタンを押してください。",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
