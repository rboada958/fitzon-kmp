package com.tepuytech.fitzon.presentation.ui.screen.athlete

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tepuytech.fitzon.presentation.ui.composable.*
import org.jetbrains.compose.ui.tooling.preview.Preview

class PersonalRecords : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        PersonalRecordScreen(
            onBackClick = { navigator.pop() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalRecordScreen(
    onBackClick: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val mockRecords = listOf(
        PersonalRecordMock(
            id = "1",
            exerciseName = "Run",
            value = "2000 reps",
            achievedAt = "16 Nov 2024",
            isNew = true
        ),
        PersonalRecordMock(
            id = "2",
            exerciseName = "Air_Squats",
            value = "300 reps",
            achievedAt = "07 Nov 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "3",
            exerciseName = "Air_Squats",
            value = "250 reps",
            achievedAt = "01 Nov 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "4",
            exerciseName = "Push_ups",
            value = "200 reps",
            achievedAt = "07 Nov 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "5",
            exerciseName = "Push_ups",
            value = "180 reps",
            achievedAt = "30 Oct 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "6",
            exerciseName = "Pull_ups",
            value = "100 reps",
            achievedAt = "07 Nov 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "7",
            exerciseName = "Pull_ups",
            value = "85 reps",
            achievedAt = "25 Oct 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "8",
            exerciseName = "Power_Snatch",
            value = "120 kg",
            achievedAt = "29 Oct 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "9",
            exerciseName = "Power_Snatch",
            value = "115 kg",
            achievedAt = "20 Oct 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "10",
            exerciseName = "Back_Squat",
            value = "150 kg",
            achievedAt = "29 Oct 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "11",
            exerciseName = "Back_Squat",
            value = "145 kg",
            achievedAt = "22 Oct 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "12",
            exerciseName = "Back_Squat",
            value = "140 kg",
            achievedAt = "15 Oct 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "13",
            exerciseName = "Deadlift",
            value = "200 kg",
            achievedAt = "12 Nov 2024",
            isNew = true
        ),
        PersonalRecordMock(
            id = "14",
            exerciseName = "Deadlift",
            value = "195 kg",
            achievedAt = "05 Nov 2024",
            isNew = false
        ),
        PersonalRecordMock(
            id = "15",
            exerciseName = "Box_Jump",
            value = "75 cm",
            achievedAt = "10 Nov 2024",
            isNew = true
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Personal Records",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B4332),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundGradient)
                .padding(paddingValues)
        ) {
            if (mockRecords.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text("🏆", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Aún no tienes PRs",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "¡Completa tus primeros workouts para establecer records!",
                            fontSize = 14.sp,
                            color = textGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = tween(500)
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        val groupedRecords = mockRecords.groupBy { it.exerciseName }

                        groupedRecords.forEach { (exerciseName, records) ->
                            item {
                                Text(
                                    text = formatExerciseName(exerciseName),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = greenLight,
                                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                )
                            }

                            items(records) { record ->
                                PersonalRecordDetailCard(record = record)
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}

data class PersonalRecordMock(
    val id: String,
    val exerciseName: String,
    val value: String,
    val achievedAt: String,
    val isNew: Boolean = false
)

@Composable
fun PersonalRecordDetailCard(
    record: PersonalRecordMock
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = cardBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = record.achievedAt,
                        fontSize = 12.sp,
                        color = textGray
                    )

                    if (record.isNew) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFF6B6B).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "NUEVO",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF6B6B),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = record.value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = greenPrimary
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = greenPrimary.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "PR",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = greenPrimary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

fun formatExerciseName(exerciseName: String): String {
    return exerciseName
        .replace("_", " ")
        .split(" ")
        .joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
}

@Preview
@Composable
fun PersonalRecordsScreenPreview() {
    PersonalRecordScreen()
}