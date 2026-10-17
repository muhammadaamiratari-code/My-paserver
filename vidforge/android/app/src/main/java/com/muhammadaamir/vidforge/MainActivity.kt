package com.muhammadaamir.vidforge

import android.net.Uri
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.media.MediaMetadataRetriever
import android.content.Context
import android.content.Intent
import java.io.File
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONObject
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.VideoSettings
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Pets
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.ClearCredentialStateRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

object ProjectStore {
    private const val PREFS = "vidforge_projects"

    fun loadProjects(context: Context): List<String> {
        val preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = preferences.getString("project_names", "[]") ?: "[]"
        val array = org.json.JSONArray(raw)

        return buildList {
            for (i in 0 until array.length()) {
                val name = array.optString(i).trim()
                if (name.isNotEmpty()) add(name)
            }
        }
    }

    fun saveProject(context: Context, name: String) {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) return

        val projects = loadProjects(context).toMutableList()

        if (!projects.contains(cleanName)) {
            projects.add(cleanName)
        }

        val array = org.json.JSONArray()
        projects.forEach { array.put(it) }

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString("project_names", array.toString())
            .apply()
    }

    fun deleteProject(context: Context, name: String) {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) return

        val projects = loadProjects(context)
            .filterNot { it == cleanName }

        val array = org.json.JSONArray()
        projects.forEach { array.put(it) }

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove("project_data_$cleanName")
            .putString("project_names", array.toString())
            .apply()
    }

    fun loadProjectData(
        context: Context,
        projectName: String
    ): Map<String, String> {
        val preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = preferences.getString("project_data_$projectName", "{}") ?: "{}"
        val objectData = org.json.JSONObject(raw)

        return buildMap {
            val keys = objectData.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                put(key, objectData.optString(key, ""))
            }
        }
    }

    fun saveProjectData(
        context: Context,
        projectName: String,
        data: Map<String, String>
    ) {
        val cleanName = projectName.trim()
        if (cleanName.isEmpty()) return

        val objectData = org.json.JSONObject()
        data.forEach { (key, value) ->
            objectData.put(key, value)
        }

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString("project_data_$cleanName", objectData.toString())
            .apply()
    }
}

    
data class JobItem(
    val id: String,
    val type: String,
    val status: String,
    val title: String,
    val projectName: String = "",
    val progress: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val startedAt: Long = 0L,
    val processingTime: Long = 0L,
    val error: String = "",
    val isServerJob: Boolean = false
)

object JobStore {
    private const val PREFS = "vidforge_jobs"
    private const val JOBS_KEY = "jobs"

    fun loadJobs(context: Context): List<JobItem> {
        val preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = preferences.getString(JOBS_KEY, "[]") ?: "[]"
        val array = org.json.JSONArray(raw)

        return buildList {
            for (i in 0 until array.length()) {
                val item = array.optJSONObject(i) ?: continue
                add(
                    JobItem(
                        id = item.optString("id"),
                        type = item.optString("type"),
                        status = item.optString("status"),
                        title = item.optString("title"),
                        projectName = item.optString("projectName"),
                        progress = item.optInt("progress", 0),
                        createdAt = item.optLong("createdAt", 0L),
                        startedAt = item.optLong("startedAt", 0L),
                        processingTime = item.optLong("processingTime", 0L),
                        error = item.optString("error")
                    )
                )
            }
        }
    }

    fun saveJobs(context: Context, jobs: List<JobItem>) {
        val array = org.json.JSONArray()

        jobs.forEach { job ->
            array.put(
                org.json.JSONObject().apply {
                    put("id", job.id)
                    put("type", job.type)
                    put("status", job.status)
                    put("title", job.title)
                    put("projectName", job.projectName)
                    put("progress", job.progress)
                    put("createdAt", job.createdAt)
                    put("startedAt", job.startedAt)
                    put("processingTime", job.processingTime)
                    put("error", job.error)
                }
            )
        }

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(JOBS_KEY, array.toString())
            .apply()
    }

    fun deleteJob(context: Context, jobId: String) {
        val jobs = loadJobs(context).filterNot { it.id == jobId }
        saveJobs(context, jobs)
    }

    fun updateJob(context: Context, updatedJob: JobItem) {
        val jobs = loadJobs(context).map { job ->
            if (job.id == updatedJob.id) updatedJob else job
        }
        saveJobs(context, jobs)
    }

    fun clearAllJobs(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove(JOBS_KEY)
            .apply()
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VidForgeApp()
        }
    }
}

@Composable
fun VidForgeApp() {
    val context = LocalContext.current
    val preferences = context.getSharedPreferences("vidforge_profile", Context.MODE_PRIVATE)
    val googleAccountSaved = preferences.getBoolean("google_account_saved", false)

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF7F8FC)
        ) {
            if (googleAccountSaved) {
                VidForgeHome()
            } else {
                WelcomeScreen()
            }
        }
    }
}

@Composable
fun WelcomeScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isSigningIn by remember { mutableStateOf(false) }
    var signInError by remember { mutableStateOf<String?>(null) }
    var debugStep by remember { mutableStateOf<String?>(null) }

    if (isSigningIn) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF7F8FC)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Signing in with Google...")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "VidForge",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Welcome",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sign in to continue",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        SignInOptionCard(
            iconText = "G",
            title = "Continue with Google",
            enabled = !isSigningIn,
            onClick = {
                isSigningIn = true
                signInError = null
                debugStep = "STEP 1 — Google Sign-In شروع"
                scope.launch {
                    try {
                        debugStep = "STEP 2 — Google Credential Manager"
                        val account = signInWithGoogle(context)

                        if (account != null) {
                            debugStep = "STEP 4 — Google account ملا"
                            android.util.Log.d("VidForgeAuth", "STEP 5: Starting backend connection")
                            debugStep = "STEP 5 — Backend connection"
                            val backendConnected = connectGoogleToBackend(account.idToken) { step -> debugStep = step }
                            android.util.Log.d("VidForgeAuth", "STEP 6: Backend connection returned: $backendConnected")
                            debugStep = "STEP 6 — Backend response: $backendConnected"

                            if (backendConnected != null) {
                                context.getSharedPreferences(
                                    "vidforge_profile",
                                    Context.MODE_PRIVATE
                                ).edit()
                                    .putBoolean("google_account_saved", true)
                                    .putString("google_account_id", account.uniqueId)
                                    .putString("google_account_name", account.displayName)
                                    .putString("google_account_picture", account.profilePictureUri?.toString())
                                    .putInt("backend_user_id", backendConnected)
                                    .apply()

                                isSigningIn = false
                                (context as? ComponentActivity)?.recreate()
                            } else {
                                isSigningIn = false
                                signInError = "Google account could not connect to VidForge server."
                            }
                        } else {
                            isSigningIn = false
                            signInError = "Google Sign-In was not completed."
                        }
                    } catch (e: Exception) {
                        android.util.Log.e(
                            "VidForgeAuth",
                            "Google Sign-In exception: ${e::class.java.name}",
                            e
                        )
                        isSigningIn = false
                        signInError = "${e::class.java.simpleName}: ${e.message ?: "Google Sign-In failed"}"
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        SignInOptionCard(
            iconText = "f",
            title = "Continue with Facebook",
            onClick = { }
        )

        Spacer(modifier = Modifier.height(14.dp))

        SignInOptionCard(
            iconText = "📱",
            title = "Continue with Phone",
            onClick = { }
        )

        debugStep?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = it)
        }

        signInError?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun SignInOptionCard(
    iconText: String,
    title: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconText,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun VidForgeHome() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showProfile by remember { mutableStateOf(false) }
    var showVideoFactory by remember { mutableStateOf(false) }
    var showActiveJobs by remember { mutableStateOf(false) }
    var showYourVideos by remember { mutableStateOf(false) }
    var showCustomizeVideo by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var showSettingsMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val preferences = context.getSharedPreferences("vidforge_profile", Context.MODE_PRIVATE)
    var googleAccount by remember {
        mutableStateOf(
            if (preferences.getBoolean("google_account_saved", false)) {
                GoogleAccountInfo(
                    uniqueId = preferences.getString("google_account_id", "") ?: "",
                    idToken = "",
                    displayName = preferences.getString("google_account_name", null),
                    email = null,
                    profilePictureUri = preferences.getString("google_account_picture", null)?.let(Uri::parse)
                )
            } else {
                null
            }
        )
    }
    val profileImageFile = File(context.filesDir, "profile_picture.jpg")
    var profileImageUri by remember {
        mutableStateOf(
            if (profileImageFile.exists()) Uri.fromFile(profileImageFile) else null
        )
    }

    Scaffold(
        bottomBar = {
            if (!showCustomizeVideo) {
                NavigationBar(
                modifier = Modifier.navigationBarsPadding(),
                tonalElevation = 4.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Folder, contentDescription = "Projects") },
                    label = { Text("Projects") }
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Create") },
                    label = { Text("Create") }
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Characters") },
                    label = { Text("Characters") }
                )

                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.MoreHoriz, contentDescription = "More") },
                    label = { Text("More") }
                )
            }
            }
        }
    ) { paddingValues ->

        if (selectedTab == 1 || selectedTab == 2 || selectedTab == 3 || selectedTab == 4) {
            BackHandler(
                enabled = selectedTab == 1 || selectedTab == 2 || selectedTab == 3 || selectedTab == 4
            ) {
                selectedTab = 0
            }
        }

        if (showSettings) {
            BackHandler(enabled = showSettings) {
                showSettings = false
            }

            SettingsScreen(
                onBack = { showSettings = false },
                modifier = Modifier.padding(paddingValues)
            )
        } else if (showVideoFactory) {
            BackHandler(enabled = showVideoFactory) {
                showVideoFactory = false
            }

            VideoFactoryScreen(
                onBack = { showVideoFactory = false },
                modifier = Modifier.padding(paddingValues)
            )
        } else if (showYourVideos) {
            BackHandler(enabled = showYourVideos) {
                showYourVideos = false
            }

            YourVideosScreen(
                onBack = { showYourVideos = false },
                modifier = Modifier.padding(paddingValues)
            )
        } else if (showCustomizeVideo) {
            BackHandler(enabled = showCustomizeVideo) {
                showCustomizeVideo = false
            }
            CustomizeVideoScreen(
                onBack = { showCustomizeVideo = false },
                modifier = Modifier.padding(paddingValues)
            )
        } else if (showActiveJobs) {
            BackHandler(enabled = showActiveJobs) {
                showActiveJobs = false
            }

            ActiveJobsScreen(
                onBack = { showActiveJobs = false },
                modifier = Modifier.padding(paddingValues)
            )
        } else if (showProfile) {
            BackHandler(enabled = showProfile) {
                showProfile = false
            }

            ProfileScreen(
                onBack = { showProfile = false },
                profileImageUri = profileImageUri,
                onProfileImageSelected = { profileImageUri = it },
                googleAccount = googleAccount,
                onGoogleSignIn = { account ->
                    googleAccount = account
                    if (account != null) {
                        preferences.edit()
                            .putBoolean("google_account_saved", true)
                            .putString("google_account_id", account.uniqueId)
                            .putString("google_account_name", account.displayName)
                            .putString("google_account_picture", account.profilePictureUri?.toString())
                            .apply()
                    } else {
                        preferences.edit()
                            .remove("google_account_saved")
                            .remove("google_account_id")
                            .remove("google_account_name")
                            .remove("google_account_picture")
                            .remove("backend_user_id")
                            .apply()
                    }
                },
                onLogout = {
                    preferences.edit()
                        .remove("google_account_saved")
                        .remove("google_account_id")
                        .remove("google_account_name")
                        .remove("google_account_picture")
                            .remove("backend_user_id")
                        .apply()
                    googleAccount = null
                    (context as? ComponentActivity)?.recreate()
                },
                modifier = Modifier.padding(paddingValues)
            )
        } else if (selectedTab == 0) {
            HomeDashboard(
                modifier = Modifier.padding(paddingValues),
                profileImageUri = profileImageUri,
                onProfileClick = { showProfile = true },
                onSettingsClick = { showSettings = true },
                onVideoFactoryClick = { showVideoFactory = true },
                onProjectsClick = { selectedTab = 1 },
                onActiveJobsClick = { showActiveJobs = true },
                onYourVideosClick = { showYourVideos = true },
                onCustomizeVideoClick = { showCustomizeVideo = true }
            )
        } else if (selectedTab == 4) {
            MoreScreen(
                modifier = Modifier.padding(paddingValues)
            )
        } else if (selectedTab == 1) {
            ProjectsScreen(
                modifier = Modifier.padding(paddingValues)
            )
        } else if (selectedTab == 2) {
            CreateScreen(
                modifier = Modifier.padding(paddingValues)
            )
        } else if (selectedTab == 3) {
            CharactersScreen(
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            PlaceholderScreen(
                title = when (selectedTab) {
                    1 -> "Projects"
                    else -> "More"
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun VideoFactoryScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var videoPrompt by remember { mutableStateOf("") }
    var videoStyle by remember { mutableStateOf("2D") }
    var videoResolution by remember { mutableStateOf("720p") }
    var videoPlatform by remember { mutableStateOf("TikTok") }
    var videoAspectRatio by remember { mutableStateOf("9:16") }
    var videoDuration by remember { mutableStateOf("10s") }
    var customDuration by remember { mutableStateOf("") }
    var activeVideoSetting by remember { mutableStateOf<String?>(null) }
    var customDurationAmount by remember { mutableStateOf("") }
    var customDurationUnit by remember { mutableStateOf("s") }
    var showProjectWorkspace by remember { mutableStateOf(false) }
    var showSelectProject by remember { mutableStateOf(false) }
    var selectedProjectName by remember { mutableStateOf("") }
    val videoFactoryContext = androidx.compose.ui.platform.LocalContext.current
    val videoFactoryScope = rememberCoroutineScope()
    var isSubmittingVideoJob by remember { mutableStateOf(false) }
    var videoJobMessage by remember { mutableStateOf("") }

    var referenceImageUri by remember { mutableStateOf<Uri?>(null) }
    val referenceImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        referenceImageUri = uri
        if (uri != null) videoJobMessage = "Reference image select ho gayi."
    }



    if (showProjectWorkspace) {
        ProjectWorkspaceScreen(
            projectName = selectedProjectName,
            onBack = { showProjectWorkspace = false },
            modifier = modifier
        )
        return
    }

    if (showSelectProject) {
        SelectProjectScreen(
            onBack = { showSelectProject = false },
            onProjectSelected = { project ->
                selectedProjectName = project
                showSelectProject = false
                showProjectWorkspace = true
            },
            modifier = modifier
        )
        return
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Start Creating",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Create a quick video or build a full project.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF111827)
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF312E81)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Text to Video",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Turn a prompt or script into a video.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFD1D5DB)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = videoPrompt,
                    onValueChange = { videoPrompt = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    placeholder = {
                        Text(
                            "Write your prompt or script...",
                            color = Color(0xFF6B7280)
                        )
                    },
                    label = {
                        Text("Video Prompt")
                    },
                    minLines = 4,
                    maxLines = 5,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = Color(0xFF312E81),
                        unfocusedLabelColor = Color(0xFF4B5563),
                        focusedBorderColor = Color(0xFF312E81),
                        unfocusedBorderColor = Color(0xFFD1D5DB)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { referenceImagePicker.launch("image/*") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Add Optional Reference Image",
                        color = Color.White
                    )
                }

                referenceImageUri?.let { selectedUri ->
                    Image(
                        painter = coil.compose.rememberAsyncImagePainter(model = selectedUri),
                        contentDescription = "Reference image preview",
                        modifier = Modifier.fillMaxWidth().height(160.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = "Video Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))

                val videoSettings = listOf(
                    "Visual Style" to videoStyle,
                    "Platform" to videoPlatform,
                    
                    "Resolution" to videoResolution,
                    "Video Duration" to if (videoDuration == "Custom") {
                        "Custom: $customDuration"
                    } else {
                        videoDuration
                    }
                )

                videoSettings.chunked(2).forEach { rowSettings ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowSettings.forEach { setting ->
                            OutlinedButton(
                                onClick = {
                                    activeVideoSetting = when (setting.first) {
                                        "Visual Style" -> "style"
                                        "Platform" -> "platform"
                                        
                                        "Resolution" -> "resolution"
                                        else -> "duration"
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(68.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(
                                    horizontal = 5.dp,
                                    vertical = 4.dp
                                )
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = setting.first,
                                        style = MaterialTheme.typography.labelMedium,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = if (setting.first == "Platform" && videoPlatform == "Custom") {
                                            "Custom ($videoAspectRatio)"
                                        } else {
                                            setting.second.ifBlank { "Not set" }
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                        if (rowSettings.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (activeVideoSetting != null &&
                    activeVideoSetting != "custom_duration"
                ) {
                    val settingKey = activeVideoSetting ?: ""
                    val settingTitle = when (settingKey) {
                        "style" -> "Visual Style"
                        "platform" -> "Platform"
                        "format" -> "Custom Screen Format"
                        "resolution" -> "Resolution"
                        else -> "Video Duration"
                    }
                    val settingOptions = when (settingKey) {
                        "style" -> listOf("2D", "2.5D", "3D")
                        "platform" -> listOf(
                            "TikTok",
                            "YouTube",
                            "YouTube Shorts",
                            "Instagram Reels",
                            "Facebook",
                            "Custom"
                        )
                        "format" -> listOf("9:16", "16:9", "1:1", "4:5")
                        "resolution" -> listOf(
                            "360p", "480p", "720p", "1080p", "4K"
                        )
                        else -> listOf(
                            "5s", "10s", "30s", "50s",
                            "1m", "5m", "10m", "Custom"
                        )
                    }

                    AlertDialog(
                        onDismissRequest = { activeVideoSetting = null },
                        title = { Text(settingTitle) },
                        text = {
                            Column {
                                settingOptions.forEach { option ->
                                    TextButton(
                                        onClick = {
                                            when (settingKey) {
                                                "style" -> videoStyle = option
                                                "platform" -> {
                                                    videoPlatform = option
                                                    when (option) {
                                                        "YouTube" -> videoAspectRatio = "16:9"
                                                        "TikTok",
                                                        "YouTube Shorts",
                                                        "Instagram Reels",
                                                        "Facebook" -> videoAspectRatio = "9:16"
                                                        "Custom" -> activeVideoSetting = "format"
                                                    }
                                                }
                                                "format" -> videoAspectRatio = option
                                                "resolution" -> videoResolution = option
                                                "duration" -> {
                                                    if (option == "Custom") {
                                                        customDurationAmount =
                                                            customDuration.filter { it.isDigit() }
                                                        customDurationUnit =
                                                            if (customDuration.trim().endsWith("m", true)) "m" else "s"
                                                        activeVideoSetting = "custom_duration"
                                                    } else {
                                                        videoDuration = option
                                                        activeVideoSetting = null
                                                    }
                                                }
                                            }
                                            if (settingKey != "duration" && !(settingKey == "platform" && videoPlatform == "Custom")) {
                                                activeVideoSetting = null
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(option)
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = { activeVideoSetting = null }
                            ) {
                                Text("Close")
                            }
                        }
                    )
                }

                if (activeVideoSetting == "custom_duration") {
                    AlertDialog(
                        onDismissRequest = { activeVideoSetting = null },
                        title = { Text("Custom Video Duration") },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = customDurationAmount,
                                    onValueChange = { input ->
                                        customDurationAmount = input.filter { it.isDigit() }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    label = { Text("Duration amount") },
                                    placeholder = { Text("Enter a number") }
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Unit")
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (customDurationUnit == "s") {
                                        Button(
                                            onClick = { customDurationUnit = "s" },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Seconds")
                                        }
                                    } else {
                                        OutlinedButton(
                                            onClick = { customDurationUnit = "s" },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Seconds")
                                        }
                                    }
                                    if (customDurationUnit == "m") {
                                        Button(
                                            onClick = { customDurationUnit = "m" },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Minutes")
                                        }
                                    } else {
                                        OutlinedButton(
                                            onClick = { customDurationUnit = "m" },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Minutes")
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val amount = customDurationAmount.toIntOrNull()
                                    if (amount != null && amount > 0) {
                                        customDuration = "$amount$customDurationUnit"
                                        videoDuration = "Custom"
                                        activeVideoSetting = null
                                    }
                                }
                            ) {
                                Text("Apply")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { activeVideoSetting = null }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (isSubmittingVideoJob) {
                            Unit
                        } else if (videoPrompt.isBlank()) {
                            videoJobMessage = "Pehle video ka prompt likhein."
                        } else {
                            val prefs = videoFactoryContext.getSharedPreferences(
                                "vidforge_profile",
                                Context.MODE_PRIVATE
                            )
                            val backendUserId = prefs.getInt("backend_user_id", -1)
                            if (backendUserId <= 0) {
                                videoJobMessage = "Pehle Google account ko VidForge server se connect karein."
                            } else {
                                val durationInput = if (videoDuration == "Custom") customDuration else videoDuration
                                val durationMatch = Regex(
                                    """^\s*(\d+)\s*(s|m)?\s*$""",
                                    RegexOption.IGNORE_CASE
                                ).matchEntire(durationInput)
                                val durationAmount = durationMatch?.groupValues?.get(1)?.toIntOrNull()
                                val durationUnit = durationMatch?.groupValues?.get(2)?.lowercase()
                                val durationSeconds = durationAmount?.let {
                                    if (durationUnit == "m") it * 60 else it
                                }

                                if (durationSeconds == null || durationSeconds <= 0) {
                                    videoJobMessage = "Duration sahi likhein, misal: 120s ya 2m."
                                } else {
                                    val selectedReferenceUri = referenceImageUri
                                    isSubmittingVideoJob = true
                                    videoJobMessage = "Server par video job ban rahi hai..."
                                    videoFactoryScope.launch {
                                        try {
                                            val uploadedReferencePath = selectedReferenceUri?.let { uri ->
                                                uploadVidForgeReferenceImage(videoFactoryContext, uri)
                                            }

                                            val project = postVidForgeJson(
                                                "/api/projects",
                                                JSONObject()
                                                    .put("user_id", backendUserId)
                                                    .put("title", "$videoPlatform Video")
                                            )
                                            val projectId = project.optInt("id", -1)
                                            if (projectId <= 0) {
                                                throw IllegalStateException("Server ne project ID nahi di.")
                                            }

                                            val jobPayload = JSONObject()
                                                .put("project_id", projectId)
                                                .put("user_id", backendUserId)
                                                .put("prompt", videoPrompt.trim())
                                                .put("duration_seconds", durationSeconds)
                                                .put("style", videoStyle)
                                                .put("platform", videoPlatform)
                                                .put("aspect_ratio", videoAspectRatio)
                                                .put("resolution", videoResolution)

                                            if (uploadedReferencePath != null) {
                                                jobPayload.put("reference_image_path", uploadedReferencePath)
                                            }

                                            val job = postVidForgeJson("/api/jobs", jobPayload)
                                            val jobId = job.optInt("id", -1)
                                            if (jobId <= 0) {
                                                throw IllegalStateException("Server ne job ID nahi di.")
                                            }
                                            videoJobMessage =
                                                "Job #$jobId server par ban gayi (${job.optString("status", "QUEUED")}). Video engine abhi connect nahi hai."
                                        } catch (e: Exception) {
                                            videoJobMessage =
                                                "Job nahi ban saki: ${e.message ?: e.javaClass.simpleName}"
                                        } finally {
                                            isSubmittingVideoJob = false
                                        }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(17.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Generate Video",
                        fontWeight = FontWeight.SemiBold
                    )
                }
                if (videoJobMessage.isNotBlank()) {
                    Text(
                        text = videoJobMessage,
                        color = if (videoJobMessage.startsWith("Job #")) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Movies & Drama Series Studio",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Create movies and drama series with your own characters, scenes, and custom durations.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Project",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Create or continue a video project.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { showSelectProject = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Edit Project")
                    }

                    Button(
                        onClick = {
                            selectedProjectName = ""
                            showProjectWorkspace = true
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text("New Project")
                    }
                }
            }
        }
    }
}

@Composable
fun SelectProjectScreen(
    onBack: () -> Unit,
    onProjectSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var projects by remember { mutableStateOf(ProjectStore.loadProjects(context)) }
    var projectMenu by remember { mutableStateOf<String?>(null) }
    var projectToDelete by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Select Project",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Choose a saved project to continue.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (projects.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "No saved projects yet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Projects saved from New Project will appear here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(projects) { project ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 18.dp, end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onProjectSelected(project) }
                                    .padding(vertical = 18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Text(
                                    text = project,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Box {
                                IconButton(
                                    onClick = {
                                        projectMenu = if (projectMenu == project) {
                                            null
                                        } else {
                                            project
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Project options"
                                    )
                                }

                                DropdownMenu(
                                    expanded = projectMenu == project,
                                    onDismissRequest = { projectMenu = null }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Delete") },
                                        onClick = {
                                            projectMenu = null
                                            projectToDelete = project
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    projectToDelete?.let { project ->
        AlertDialog(
            onDismissRequest = { projectToDelete = null },
            title = {
                Text("Delete Project?")
            },
            text = {
                Text("Are you sure you want to delete \"$project\"?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        ProjectStore.deleteProject(context, project)
                        projects = ProjectStore.loadProjects(context)
                        projectToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { projectToDelete = null }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProjectWorkspaceScreen(
    projectName: String = "",
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val sections = listOf(
        "Story / Script",
        "Scenes",
        "Characters",
        "Visual Style",
        "Camera & Motion",
        "Voice & Dialogue",
        "Music & Sound",
        "Reference Images",
        "AI Generation",
        "Generation Progress",
        "Preview",
        "Export"
    )

    var editableProjectName by remember { mutableStateOf(projectName) }
    var projectSaved by remember { mutableStateOf(false) }
    var projectData by remember {
        mutableStateOf(
            if (projectName.isNotBlank()) {
                ProjectStore.loadProjectData(context, projectName)
            } else {
                emptyMap()
            }
        )
    }

    var storyParts by remember {
        mutableStateOf(
            run {
                val saved = projectData["story_parts"] ?: ""
                if (saved.isNotBlank()) {
                    val array = org.json.JSONArray(saved)
                    buildList {
                        for (i in 0 until array.length()) {
                            add(array.optString(i, ""))
                        }
                    }.ifEmpty { listOf("") }
                } else {
                    listOf("")
                }
            }
        )
    }
    var showStoryScript by remember { mutableStateOf(false) }
    var showScenesEditor by remember { mutableStateOf(false) }
    var showCharactersWorkspace by remember { mutableStateOf(false) }
    var showVisualStyle by remember { mutableStateOf(false) }
    var showCameraMotion by remember { mutableStateOf(false) }
    var showVoiceDialogue by remember { mutableStateOf(false) }
    var showMusicSound by remember { mutableStateOf(false) }
    var showReferenceImages by remember { mutableStateOf(false) }
    var showAIGeneration by remember { mutableStateOf(false) }
    var showGenerationProgress by remember { mutableStateOf(false) }

    if (showStoryScript) {
        BackHandler {
            showStoryScript = false
        }

        StoryScriptScreen(
            projectName = editableProjectName,
            parts = storyParts,
            onPartsChange = { storyParts = it },
            onSave = { parts ->
                val cleanName = editableProjectName.trim()
                if (cleanName.isNotEmpty()) {
                    val array = org.json.JSONArray()
                    parts.forEach { array.put(it) }
                    projectData = projectData + ("story_parts" to array.toString())
                    ProjectStore.saveProject(context, cleanName)
                    ProjectStore.saveProjectData(context, cleanName, projectData)
                }
            },
            onBack = { showStoryScript = false }
        )
        return
    }

    if (showScenesEditor) {
        BackHandler { showScenesEditor = false }
        ScenesEditorScreen(
            modifier = modifier,
            projectName = editableProjectName,
            savedScenes = projectData["scenes_data"] ?: "[]",
            allowSavedSceneEditing = projectName.isNotBlank(),
            onSave = { scenesJson ->
                val cleanName = editableProjectName.trim()
                if (cleanName.isNotEmpty()) {
                    projectData = projectData + ("scenes_data" to scenesJson)
                    ProjectStore.saveProject(context, cleanName)
                    ProjectStore.saveProjectData(context, cleanName, projectData)
                }
            },
            onBack = { showScenesEditor = false }
        )
        return
    }

    if (showCharactersWorkspace) {
        BackHandler { showCharactersWorkspace = false }
        ProjectCharactersScreen(
                    projectName = editableProjectName,
                    savedCharactersJson = projectData["characters_data"] ?: "[]",
                    savedRelationshipsJson = projectData["relationships_data"] ?: "[]",
                    onSaveRelationships = { relationshipsJson ->
                        projectData = projectData + ("relationships_data" to relationshipsJson)
                        val cleanName = editableProjectName.trim()
                        if (cleanName.isNotEmpty()) {
                            ProjectStore.saveProject(context, cleanName)
                            ProjectStore.saveProjectData(context, cleanName, projectData)
                        }
                    },
                    onSave = { charactersJson ->
                        projectData = projectData + ("characters_data" to charactersJson)
                        val cleanName = editableProjectName.trim()
                        if (cleanName.isNotEmpty()) {
                            ProjectStore.saveProject(context, cleanName)
                            ProjectStore.saveProjectData(context, cleanName, projectData)
                        }
                    },
                    modifier = modifier,
                    onBack = { showCharactersWorkspace = false }
                )
        return
    }

    
    if (showReferenceImages) {
        BackHandler { showReferenceImages = false }
        ReferenceImagesScreen(
            projectName = editableProjectName,
            savedCharactersJson = projectData["characters_data"] ?: "[]",
            savedScenesJson = projectData["scenes_data"] ?: "[]",
            savedReferencesJson = projectData["reference_images_data"] ?: "[]",
            onSave = { referencesJson ->
                val cleanName = editableProjectName.trim()
                if (cleanName.isNotEmpty()) {
                    projectData = projectData + ("reference_images_data" to referencesJson)
                    ProjectStore.saveProject(context, cleanName)
                    ProjectStore.saveProjectData(context, cleanName, projectData)
                }
            },
            onBack = { showReferenceImages = false },
            modifier = modifier
        )
        return
    }

    if (showCameraMotion) {
        BackHandler { showCameraMotion = false }
        CameraMotionScreen(
            savedCameraJson = projectData["camera_motion_data"] ?: "{}",
            onSave = { cameraJson ->
                projectData = projectData + ("camera_motion_data" to cameraJson)
                val cleanName = editableProjectName.trim()
                if (cleanName.isNotEmpty()) {
                    ProjectStore.saveProject(context, cleanName)
                    ProjectStore.saveProjectData(context, cleanName, projectData)
                }
            },
            onBack = { showCameraMotion = false },
            modifier = modifier
        )
        return
    }



    if (showGenerationProgress) {
        BackHandler { showGenerationProgress = false }
        GenerationProgressScreen(
            projectName = editableProjectName,
            savedScenesJson = projectData["scenes_data"] ?: "[]",
            onBack = { showGenerationProgress = false },
            modifier = modifier
        )
        return
    }

    if (showAIGeneration) {
        BackHandler { showAIGeneration = false }
        AIGenerationScreen(
            projectName = editableProjectName,
            savedGenerationJson = projectData["ai_generation_data"] ?: "{}",
            onSave = { generationJson ->
                val cleanName = editableProjectName.trim()
                if (cleanName.isNotEmpty()) {
                    projectData = projectData + ("ai_generation_data" to generationJson)
                    ProjectStore.saveProject(context, cleanName)
                    ProjectStore.saveProjectData(context, cleanName, projectData)
                }
            },
            onBack = { showAIGeneration = false }
        )
        return
    }

    if (showMusicSound) {
        BackHandler { showMusicSound = false }
        MusicSoundScreen(
            savedMusicJson = projectData["music_sound_data"] ?: "{}",
            onSave = { musicJson ->
                projectData = projectData + ("music_sound_data" to musicJson)
                val cleanName = editableProjectName.trim()
                if (cleanName.isNotEmpty()) {
                    ProjectStore.saveProject(context, cleanName)
                    ProjectStore.saveProjectData(context, cleanName, projectData)
                }
            },
            onBack = { showMusicSound = false },
            modifier = modifier
        )
        return
    }

    if (showVoiceDialogue) {
        BackHandler { showVoiceDialogue = false }
        VoiceDialogueScreen(
            savedVoiceJson = projectData["voice_dialogue_data"] ?: "{}",
            onSave = { voiceJson ->
                projectData = projectData + ("voice_dialogue_data" to voiceJson)
                val cleanName = editableProjectName.trim()
                if (cleanName.isNotEmpty()) {
                    ProjectStore.saveProject(context, cleanName)
                    ProjectStore.saveProjectData(context, cleanName, projectData)
                }
            },
            onBack = { showVoiceDialogue = false },
            modifier = modifier
        )
        return
    }

    if (showVisualStyle) {
        BackHandler { showVisualStyle = false }
        VisualStyleScreen(
            projectName = editableProjectName,
            savedStyleJson = projectData["visual_style_data"] ?: "{}",
            onSave = { styleJson ->
                projectData = projectData + ("visual_style_data" to styleJson)
                val cleanName = editableProjectName.trim()
                if (cleanName.isNotEmpty()) {
                    ProjectStore.saveProject(context, cleanName)
                    ProjectStore.saveProjectData(context, cleanName, projectData)
                }
            },
            onBack = { showVisualStyle = false },
            modifier = modifier
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "New Project",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Build your video project in one workspace.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Project Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = editableProjectName,
                            onValueChange = {
                                editableProjectName = it
                                projectSaved = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Project Name") },
                            placeholder = { Text("Enter project name") },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val cleanName = editableProjectName.trim()
                                    if (cleanName.isNotEmpty()) {
                                        ProjectStore.saveProject(context, cleanName)
                                        ProjectStore.saveProjectData(
                                            context,
                                            cleanName,
                                            projectData + ("project_name" to cleanName)
                                        )
                                        projectSaved = true
                                        onBack()
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Save")
                            }

                            Button(
                                onClick = { },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Generate")
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Status: Ready to build",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            items(sections.size) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = index in 0..9) {
                    if (index == 0) {
                        showStoryScript = true
                    } else if (index == 1) {
                        showScenesEditor = true
                    } else if (index == 2) {
                        showCharactersWorkspace = true
                    } else if (index == 3) {
                        showVisualStyle = true
                    } else if (index == 4) {
                        showCameraMotion = true
                    } else if (index == 5) {
                        showVoiceDialogue = true
                    } else if (index == 6) {
                        showMusicSound = true
                    } else if (index == 7) {
                        showReferenceImages = true
                    } else if (index == 8) {
                        showAIGeneration = true
                    } else if (index == 9) {
                        showGenerationProgress = true
                    }
                },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "${index + 1}. ${sections[index]}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Project workspace section",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}






@Composable
fun GenerationProgressScreen(
    projectName: String,
    savedScenesJson: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sceneCount = remember(savedScenesJson) {
        runCatching { JSONArray(savedScenesJson).length() }.getOrDefault(0)
    }

    val progressCards = listOf(
        "Overall Progress" to
            "Waiting for AI Engine Connection. Real progress will appear after a generation job starts.",
        "Current Stage" to
            "Not started. No live generation job is connected.",
        "Scene Progress" to
            if (sceneCount > 0)
                "$sceneCount saved scene(s). Generation has not started; all scenes are waiting."
            else
                "No saved scenes found. Scene status will appear after scenes are saved and generation starts.",
        "Shot Progress" to
            "Real shot statuses will appear when a generation job starts. No shot progress is being reported yet.",
        "Queue & Server Status" to
            "Live queue and server status are not connected to this screen. AI engine connection is still required.",
        "Errors & Warnings" to
            "Generation has not started, so there are no generation errors to report. Connect an AI engine to continue.",
        "Generated Results" to
            "No generated video results are recorded for this project yet.",
        "Progress History" to
            "No generation activity has been recorded yet."
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to Project"
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Generation Progress",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = projectName.ifBlank { "New Project" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            progressCards.forEach { (title, detail) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = detail,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Text("Back to Project")
        }
    }
}


@Composable
fun AIGenerationScreen(
    projectName: String,
    savedGenerationJson: String,
    onSave: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initial = remember(savedGenerationJson) {
        runCatching { JSONObject(savedGenerationJson) }.getOrElse { JSONObject() }
    }

    var sceneNumber by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("scene_number", "1"))
    }
    var shotNumber by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("shot_number", "1"))
    }
    var visualStyle by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("visual_style", "3D"))
    }
    var customStyle by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("custom_style", ""))
    }
    var platform by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("platform", "YouTube"))
    }
    var width by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("width", "1920"))
    }
    var height by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("height", "1080"))
    }
    var ratio by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("aspect_ratio", "16:9"))
    }
    var resolution by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("resolution", "1080p"))
    }
    var fps by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("fps", "30"))
    }
    var engine by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("engine", "Auto (not connected)"))
    }
    var quality by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("quality", "Standard"))
    }
    var instructions by remember(savedGenerationJson) {
        mutableStateOf(initial.optString("instructions", ""))
    }
    var autoDirector by remember(savedGenerationJson) {
        mutableStateOf(initial.optBoolean("auto_director", false))
    }
    var savedShotsJson by remember(savedGenerationJson) {
        mutableStateOf(initial.optJSONArray("saved_shot_entries")?.toString() ?: "[]")
    }
    var status by remember { mutableStateOf("Settings not saved yet") }

    fun makeData(): JSONObject {
        return JSONObject().apply {
            put("scene_number", sceneNumber)
            put("shot_number", shotNumber)
            put("visual_style", visualStyle)
            put("custom_style", customStyle)
            put("platform", platform)
            put("width", width)
            put("height", height)
            put("aspect_ratio", ratio)
            put("resolution", resolution)
            put("fps", fps)
            put("engine", engine)
            put("quality", quality)
            put("instructions", instructions)
            put("auto_director", autoDirector)
            put("saved_shot_entries", JSONArray(savedShotsJson))
        }
    }

    fun choosePlatform(value: String) {
        platform = value
        when (value) {
            "TikTok" -> {
                width = "1080"
                height = "1920"
                ratio = "9:16"
            }
            "YouTube" -> {
                width = "1920"
                height = "1080"
                ratio = "16:9"
            }
            "Facebook" -> {
                width = "1080"
                height = "1080"
                ratio = "1:1"
            }
            "Instagram" -> {
                width = "1080"
                height = "1350"
                ratio = "4:5"
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
        Text(
            text = "AI Generation",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Project: $projectName",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("Scene & Shot", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = sceneNumber,
                    onValueChange = { sceneNumber = it.filter(Char::isDigit) },
                    label = { Text("Scene Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = shotNumber,
                    onValueChange = { shotNumber = it.filter(Char::isDigit) },
                    label = { Text("Shot Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("Visual Style", fontWeight = FontWeight.Bold)
                listOf("2D", "2.5D", "3D", "Realistic", "Anime", "Custom").forEach { item ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        androidx.compose.material3.RadioButton(
                            selected = visualStyle == item,
                            onClick = { visualStyle = item }
                        )
                        Text(item)
                    }
                }
                if (visualStyle == "Custom") {
                    OutlinedTextField(
                        value = customStyle,
                        onValueChange = { customStyle = it },
                        label = { Text("Describe your custom style") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("Platform & Screen Size", fontWeight = FontWeight.Bold)
                listOf("TikTok", "YouTube", "Facebook", "Instagram", "Other Platforms", "Custom").forEach { item ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        androidx.compose.material3.RadioButton(
                            selected = platform == item,
                            onClick = { choosePlatform(item) }
                        )
                        Text(item)
                    }
                }
                Text(
                    "These are suggested output settings; you can edit them.",
                    style = MaterialTheme.typography.bodySmall
                )
                OutlinedTextField(
                    value = width,
                    onValueChange = { width = it.filter(Char::isDigit) },
                    label = { Text("Width (pixels)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it.filter(Char::isDigit) },
                    label = { Text("Height (pixels)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = ratio,
                    onValueChange = { ratio = it },
                    label = { Text("Aspect Ratio (e.g. 16:9)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("Resolution", fontWeight = FontWeight.Bold)
                listOf("720p", "1080p", "4K").forEach { item ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        androidx.compose.material3.RadioButton(
                            selected = resolution == item,
                            onClick = { resolution = item }
                        )
                        Text(item)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text("Frame Rate", fontWeight = FontWeight.Bold)
                listOf("24", "30", "60").forEach { item ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        androidx.compose.material3.RadioButton(
                            selected = fps == item,
                            onClick = { fps = item }
                        )
                        Text("$item FPS")
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("Generation Settings", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = engine,
                    onValueChange = { engine = it },
                    label = { Text("AI Engine / Model") },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Quality", fontWeight = FontWeight.Bold)
                listOf("Draft", "Standard", "High").forEach { item ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        androidx.compose.material3.RadioButton(
                            selected = quality == item,
                            onClick = { quality = item }
                        )
                        Text(item)
                    }
                }
                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Additional instructions") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("Director Controls", fontWeight = FontWeight.Bold)
                Button(
                    onClick = {
                        autoDirector = !autoDirector
                        status = "Auto Director preference updated; engine is not connected."
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (autoDirector) "Auto Director: ON (not connected)" else "Auto Director: OFF")
                }
                Spacer(Modifier.height(6.dp))
                Button(
                    onClick = { status = "Play is unavailable until a video engine is connected." },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Play — Engine not connected")
                }
                Text(
                    "Video generation is not connected yet. These controls currently save settings only.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        Button(
            onClick = {
                onSave(makeData().toString())
                status = "Settings saved for this project."
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Settings")
        }

        Button(
            onClick = {
                val current = makeData()
                val entries = runCatching { JSONArray(savedShotsJson) }
                    .getOrElse { JSONArray() }
                entries.put(JSONObject(current.toString()))
                savedShotsJson = entries.toString()
                val nextShot = (shotNumber.toIntOrNull() ?: 1) + 1
                shotNumber = nextShot.toString()
                onSave(makeData().toString())
                status = "Shot saved. Ready for Shot $shotNumber."
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Shot")
        }

        Spacer(Modifier.height(8.dp))
        Text("Saved shots: ${runCatching { JSONArray(savedShotsJson).length() }.getOrDefault(0)}")
        Text(status, style = MaterialTheme.typography.bodyMedium)
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Back to Project")
        }
    }
}

@Composable
fun ReferenceImagesScreen(
    projectName: String,
    savedCharactersJson: String,
    savedScenesJson: String,
    savedReferencesJson: String,
    onSave: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var mode by remember { mutableStateOf("character") }
    var selectedCharacter by remember { mutableStateOf(0) }
    var characterId by remember { mutableStateOf("") }
    var selectedScene by remember { mutableStateOf(0) }
    var selectedShot by remember { mutableStateOf(0) }
    var sceneNumberInput by remember { mutableStateOf("") }
    var shotNumberInput by remember { mutableStateOf("") }
    var characterMenu by remember { mutableStateOf(false) }
    var sceneMenu by remember { mutableStateOf(false) }
    var shotMenu by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("Location Reference") }
    var imagePath by remember { mutableStateOf("") }
    var imageName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var referencesJson by remember(savedReferencesJson) {
        mutableStateOf(savedReferencesJson.ifBlank { "[]" })
    }
    var status by remember { mutableStateOf("") }

    val categories = listOf(
        "Location Reference",
        "Background Reference",
        "Vehicle Reference",
        "Interior Reference",
        "Object Reference",
        "Animal & Creature Reference",
        "Style Reference",
        "Weather & Environment Reference"
    )

    val characters = remember(savedCharactersJson) {
        val array = runCatching { org.json.JSONArray(savedCharactersJson) }
            .getOrElse { org.json.JSONArray() }
        (0 until array.length()).map { i ->
            val raw = array.optString(i, "")
            val obj = array.optJSONObject(i)
                ?: runCatching { org.json.JSONObject(raw) }.getOrNull()
            val name = obj?.optString("name")?.takeIf { it.isNotBlank() }
                ?: obj?.optString("character_name")?.takeIf { it.isNotBlank() }
                ?: raw.takeIf { it.isNotBlank() && obj == null }
                ?: "Character ${i + 1}"
            val id = obj?.optString("character_id")?.takeIf { it.isNotBlank() }
                ?: obj?.optString("id")?.takeIf { it.isNotBlank() }
                ?: ""
            Triple(id, name, i)
        }
    }

    val scenes = remember(savedScenesJson) {
        val array = runCatching { org.json.JSONArray(savedScenesJson) }
            .getOrElse { org.json.JSONArray() }
        (0 until array.length()).mapNotNull { i ->
            val obj = array.optJSONObject(i) ?: return@mapNotNull null
            val name = obj.optString("description").ifBlank { "Scene ${i + 1}" }
            val id = obj.optString("id").ifBlank {
                obj.optString("scene_id").ifBlank { "scene_${i + 1}" }
            }
            Triple(id, name, obj)
        }
    }

    val shots = remember(selectedScene, savedScenesJson) {
        val scene = scenes.getOrNull(selectedScene)?.third
        val array = scene?.optJSONArray("shots") ?: org.json.JSONArray()
        (0 until array.length()).map { i ->
            val obj = array.optJSONObject(i)
            val name = obj?.optString("description")?.takeIf { it.isNotBlank() }
                ?: "Shot ${i + 1}"
            val id = obj?.optString("id")?.takeIf { it.isNotBlank() }
                ?: "shot_${i + 1}"
            id to name
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            runCatching {
                val folder = File(context.filesDir, "reference_images")
                folder.mkdirs()
                val file = File(folder, "ref_${System.currentTimeMillis()}.img")
                context.contentResolver.openInputStream(uri).use { input ->
                    requireNotNull(input) { "Could not open the selected image" }.use { source ->
                        java.io.FileOutputStream(file).use { output ->
                            source.copyTo(output)
                        }
                    }
                }
                imagePath = file.absolutePath
                status = "Image selected. Press Save Reference to save it."
            }.onFailure {
                status = "Could not save image: ${it.message ?: "Unknown error"}"
            }
        }
    }

    fun persist(json: String) {
        referencesJson = json
        onSave(json)
    }

    fun saveReference() {
        if (imagePath.isBlank()) {
            status = "Please select an image first."
            return
        }
        var sceneNumberToSave = ""
        var shotNumberToSave = ""
        val targetId: String
        val targetName: String
        val sceneId: String
        val shotId: String

        if (mode == "character") {
            val character = characters.getOrNull(selectedCharacter)
            val enteredId = characterId.trim()
            val savedCharacterId = character?.first?.trim().orEmpty()
            if (enteredId.isBlank() && savedCharacterId.isBlank()) {
                status = "Enter a valid Character ID before saving this reference."
                return
            }
            targetId = enteredId.ifBlank { savedCharacterId }
            targetName = character?.second ?: "Character"
            sceneId = ""
            shotId = ""
        } else {
            val scene = scenes.getOrNull(selectedScene)
            if (scene == null) {
                status = "Please save a scene in the Scenes section first."
                return
            }
            val enteredSceneNumber = sceneNumberInput.trim().toIntOrNull()
            val enteredShotNumber = shotNumberInput.trim().toIntOrNull()
            if (enteredSceneNumber == null || enteredSceneNumber <= 0) {
                status = "Enter a valid positive Scene Number."
                return
            }
            if (enteredShotNumber == null || enteredShotNumber <= 0) {
                status = "Enter a valid positive Shot Number."
                return
            }
            sceneNumberToSave = enteredSceneNumber.toString()
            shotNumberToSave = enteredShotNumber.toString()
            targetId = scene.first
            targetName = scene.second
            sceneId = scene.first
            shotId = shots.getOrNull(selectedShot)?.first ?: ""
        }

        val array = runCatching { org.json.JSONArray(referencesJson) }
            .getOrElse { org.json.JSONArray() }
        array.put(
            org.json.JSONObject()
                .put("id", "ref_${System.currentTimeMillis()}")
                .put("target_type", mode)
                .put("target_id", targetId)
                .put("target_name", targetName)
                .put("scene_id", sceneId)
                .put("shot_id", shotId)
                .put("scene_number", sceneNumberToSave)
                .put("shot_number", shotNumberToSave)
                .put("category", category)
                .put("name", imageName.trim().ifBlank { category })
                .put("description", description.trim())
                .put("image_path", imagePath)
                .put("auto_detect_status", "pending")
        )
        persist(array.toString())
        imagePath = ""
        imageName = ""
        description = ""
        status = "Reference image saved."
    }

    Column(
        modifier = modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Column {
                Text("Reference Images", style = MaterialTheme.typography.headlineSmall)
                Text("Project: $projectName")
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("Select Reference Target", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { mode = "character" }) {
                Text(if (mode == "character") "✓ Character" else "Character")
            }
            Button(onClick = { mode = "scene_shot" }) {
                Text(if (mode == "scene_shot") "✓ Scene / Shot" else "Scene / Shot")
            }
        }

        if (mode == "character") {
            Spacer(Modifier.height(8.dp))
            Button(onClick = { characterMenu = true }) {
                Text(characters.getOrNull(selectedCharacter)?.second ?: "Select Character")
            }
            OutlinedTextField(
                value = characterId,
                onValueChange = { characterId = it },
                label = { Text("Character ID (optional)") },
                placeholder = { Text("Enter or paste Character ID") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Text(
                "Character ID: ${characterId.trim().ifBlank { characters.getOrNull(selectedCharacter)?.first?.takeIf { it.isNotBlank() } ?: "Not set" }}",
                style = MaterialTheme.typography.bodyMedium
            )
            androidx.compose.material3.DropdownMenu(
                expanded = characterMenu,
                onDismissRequest = { characterMenu = false }
            ) {
                characters.forEachIndexed { index, item ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(item.second) },
                        onClick = {
                            selectedCharacter = index
                            characterMenu = false
                        }
                    )
                }
            }
            if (characters.isEmpty()) {
                Text("No characters available yet. You can enter a Character ID manually.")
            }
        } else {
            Spacer(Modifier.height(8.dp))
            Button(onClick = { sceneMenu = true }) {
                Text(scenes.getOrNull(selectedScene)?.second ?: "Select Scene")
            }
            androidx.compose.material3.DropdownMenu(
                expanded = sceneMenu,
                onDismissRequest = { sceneMenu = false }
            ) {
                scenes.forEachIndexed { index, item ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(item.second) },
                        onClick = {
                            selectedScene = index
                            selectedShot = 0
                            sceneMenu = false
                        }
                    )
                }
            }
            OutlinedTextField(
                value = sceneNumberInput,
                onValueChange = { sceneNumberInput = it.filter(Char::isDigit) },
                label = { Text("Scene Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Button(onClick = { shotMenu = true }) {
                Text(shots.getOrNull(selectedShot)?.second ?: "Select Shot (optional)")
            }
            OutlinedTextField(
                value = shotNumberInput,
                onValueChange = { shotNumberInput = it.filter(Char::isDigit) },
                label = { Text("Shot Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            androidx.compose.material3.DropdownMenu(
                expanded = shotMenu,
                onDismissRequest = { shotMenu = false }
            ) {
                shots.forEachIndexed { index, item ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(item.second) },
                        onClick = {
                            selectedShot = index
                            shotMenu = false
                        }
                    )
                }
            }
            if (scenes.isEmpty()) {
                Text("Save a scene and its shots in the Scenes section first.")
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("Reference Category", style = MaterialTheme.typography.titleMedium)
        categories.forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                    .clickable { category = item },
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(if (category == item) "✓  $item" else item)
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = imageName,
            onValueChange = { imageName = it },
            label = { Text("Image Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description / Instructions") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
        Button(
            onClick = { imagePicker.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Select Reference Image") }

        if (imagePath.isNotBlank()) {
            Image(
                painter = coil.compose.rememberAsyncImagePainter(model = File(imagePath)),
                contentDescription = "Selected reference image",
                modifier = Modifier.fillMaxWidth().height(190.dp),
                contentScale = ContentScale.Fit
            )
        }

        Button(onClick = { saveReference() }, modifier = Modifier.fillMaxWidth()) {
            Text("Save Reference")
        }
        Button(
            onClick = {
                imagePath = ""
                imageName = ""
                description = ""
                status = "Ready for another image. Existing references are unchanged."
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Add Other Reference Image") }

        Button(
            onClick = {
                status = "AI Auto Detector is not connected to an image analysis engine yet. Integration is pending."
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("AI Auto Detector — Pending Integration") }

        if (status.isNotBlank()) {
            Text(status, modifier = Modifier.padding(vertical = 8.dp))
        }

        Spacer(Modifier.height(8.dp))
        Text("Saved Reference Images", style = MaterialTheme.typography.titleLarge)
        val savedArray = runCatching { org.json.JSONArray(referencesJson) }
            .getOrElse { org.json.JSONArray() }
        for (i in 0 until savedArray.length()) {
            val item = savedArray.optJSONObject(i) ?: continue
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(item.optString("name", item.optString("category")))
                    Text("${item.optString("target_name")} · ${item.optString("category")}")
                    if (item.optString("description").isNotBlank()) {
                        Text(item.optString("description"))
                    }
                    val path = item.optString("image_path")
                    if (path.isNotBlank() && File(path).exists()) {
                        Image(
                            painter = coil.compose.rememberAsyncImagePainter(model = File(path)),
                            contentDescription = "Saved reference image",
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Button(onClick = {
                        val updated = org.json.JSONArray()
                        for (j in 0 until savedArray.length()) {
                            if (j != i) updated.put(savedArray.get(j))
                        }
                        persist(updated.toString())
                        status = "Reference deleted."
                    }) { Text("Delete") }
                }
            }
        }

        Button(
            onClick = { onSave(referencesJson); status = "All reference data saved." },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Save All References") }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun ProjectCharactersScreen(
    projectName: String,
    savedCharactersJson: String,
    savedRelationshipsJson: String,
    onSaveRelationships: (String) -> Unit,
    onSave: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedCard by remember { mutableStateOf<String?>(null) }

    val savedCharacters = remember(savedCharactersJson) {
        androidx.compose.runtime.mutableStateListOf<String>().apply {
            val array = org.json.JSONArray(savedCharactersJson.ifBlank { "[]" })
            for (i in 0 until array.length()) {
                val item = array.optString(i, "")
                if (item.isNotBlank()) add(item)
            }
        }
    }

    var relationshipId1 by remember { mutableStateOf("") }
    var relationshipId2 by remember { mutableStateOf("") }
    var relationshipOfId1 by remember { mutableStateOf("") }
    var relationshipOfId2 by remember { mutableStateOf("") }

    var relationshipsJson by remember(savedRelationshipsJson) {
        mutableStateOf(savedRelationshipsJson.ifBlank { "[]" })
    }
    var showRelationshipForm by remember(savedRelationshipsJson) {
        mutableStateOf(savedRelationshipsJson.isBlank() || savedRelationshipsJson == "[]")
    }
    val savedRelationships = remember(relationshipsJson) {
        val array = org.json.JSONArray(relationshipsJson)
        (0 until array.length()).mapNotNull { index ->
            array.optJSONObject(index)?.toString()
        }
    }

    val characterCards = listOf(
        Triple("Create Character", "Create a character in the Character Library",
          "Character creation will be connected to the shared library later."),
        Triple("Import Character", "Add a character from the Character Library",
          "Import an existing library character into this project."),
        Triple("Project Characters", "Characters in this project",
            "View and manage characters assigned to this story."),
        Triple("Relationships", "Family, friends and connections",
            "Define parents, children, siblings, friends, enemies and custom relationships."),
        Triple("Character Groups", "Organize characters into groups",
            "Group the hero's family, heroine's friends, enemy team and other characters.")
    )

    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (selectedCard != null) selectedCard = null else onBack()
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = selectedCard ?: "Characters",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (selectedCard == null)
                        "Manage characters for this project."
                    else "Character workspace",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedCard == null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(characterCards.size) { index ->
                    val card = characterCards[index]
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                    if (card.first != "Create Character") {
                                        selectedCard = card.first
                                    }
                                },
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = card.first,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = card.second,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = card.third,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
         } else if (selectedCard == "Import Character") {
    Column(
        modifier = Modifier.weight(1f).fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                "1. Add Character",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Add Other Character",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { selectedCard = null },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Characters")
        }
    }
} else if (selectedCard == "Relationships") {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(savedRelationships.size) { index ->
                    val relation = org.json.JSONObject(savedRelationships[index])
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text("ID Number 1: ${relation.optString("id1")}",
                                fontWeight = FontWeight.Bold)
                            Text("Relationship of ID 1: ${relation.optString("relationship1")}")
                            Spacer(Modifier.height(6.dp))
                            Text("ID Number 2: ${relation.optString("id2")}",
                                fontWeight = FontWeight.Bold)
                            Text("Relationship of ID 2: ${relation.optString("relationship2")}")
                        }
                    }
                }

                if (showRelationshipForm) {
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedTextField(
                                    value = relationshipId1,
                                    onValueChange = { relationshipId1 = it },
                                    label = { Text("ID Number 1") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = relationshipId2,
                                    onValueChange = { relationshipId2 = it },
                                    label = { Text("ID Number 2") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = relationshipOfId1,
                                    onValueChange = { relationshipOfId1 = it },
                                    label = { Text("Relationship of ID 1") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = relationshipOfId2,
                                    onValueChange = { relationshipOfId2 = it },
                                    label = { Text("Relationship of ID 2") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Button(
                                    onClick = {
                                        if (relationshipId1.isNotBlank() &&
                                            relationshipId2.isNotBlank() &&
                                            relationshipOfId1.isNotBlank() &&
                                            relationshipOfId2.isNotBlank()
                                        ) {
                                            val array = org.json.JSONArray(relationshipsJson)
                                            val relation = org.json.JSONObject()
                                            relation.put("id1", relationshipId1.trim())
                                            relation.put("id2", relationshipId2.trim())
                                            relation.put("relationship1", relationshipOfId1.trim())
                                            relation.put("relationship2", relationshipOfId2.trim())
                                            array.put(relation)
                                            relationshipsJson = array.toString()
                                            onSaveRelationships(relationshipsJson)
                                            relationshipId1 = ""
                                            relationshipId2 = ""
                                            relationshipOfId1 = ""
                                            relationshipOfId2 = ""
                                            showRelationshipForm = false
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Save Relationship")
                                }
                            }
                        }
                    }
                } else if (savedRelationships.isNotEmpty()) {
                    item {
                        Button(
                            onClick = { showRelationshipForm = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("+ Add Another Relationship")
                        }
                    }
                }
            }
        } else if (selectedCard == "Project Characters") {
            if (savedCharacters.isEmpty()) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "No Project Characters Yet",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(savedCharacters.size) { index ->
                        val character = org.json.JSONObject(savedCharacters[index])
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    character.optString("name"),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Type: ${character.optString("type")}")
                                if (character.optString("appearance").isNotBlank())
                                    Text("Appearance: ${character.optString("appearance")}")
                                if (character.optString("clothing").isNotBlank())
                                    Text("Clothing: ${character.optString("clothing")}")
                                if (character.optString("voice").isNotBlank())
                                    Text("Voice: ${character.optString("voice")}")
                            }
                        }
                    }
                }
            }

        } else {
            val details = characterCards.firstOrNull { it.first == selectedCard }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = details?.second ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = details?.third ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "This section will be implemented in a later stage.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { selectedCard = null }) {
                        Text("Back to Characters")
                    }
                }
            }
        }
    }
}

@Composable
fun ScenesEditorScreen(
    projectName: String,
    savedScenes: String,
    onSave: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    allowSavedSceneEditing: Boolean = false
) {
    var scenesJson by remember(savedScenes) { mutableStateOf(savedScenes.ifBlank { "[]" }) }
    var editingSceneIndex by remember { mutableStateOf(-1) }
    var sceneDescription by remember { mutableStateOf("") }
    var characters by remember { mutableStateOf("") }
    var cameraMotion by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var durationChoice by remember { mutableStateOf("5") }
    var customDuration by remember { mutableStateOf("") }
    val shotDescriptions = remember { androidx.compose.runtime.mutableStateListOf("") }
    val shotDurations = remember { androidx.compose.runtime.mutableStateListOf("5") }
    val scenes = runCatching { org.json.JSONArray(scenesJson) }
        .getOrElse { org.json.JSONArray() }
    val selectedSceneDuration = if (durationChoice == "Custom") {
        customDuration.toIntOrNull()
    } else {
        durationChoice.toIntOrNull()
    }
    val activeShotIndices = shotDescriptions.indices.filter {
        shotDescriptions[it].isNotBlank()
    }
    val invalidShotDuration = activeShotIndices.any {
        shotDurations[it].toIntOrNull()?.let { seconds -> seconds > 0 } != true
    }
    val totalShotDuration = activeShotIndices.sumOf {
        shotDurations[it].toIntOrNull() ?: 0
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("New Project — Scenes", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(6.dp))
        Text("Project: $projectName")
        Text("Saved scenes: ${scenes.length()}")
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = sceneDescription,
            onValueChange = { sceneDescription = it },
            label = { Text("Scene Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        OutlinedTextField(
            value = characters,
            onValueChange = { characters = it },
            label = { Text("Characters") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = cameraMotion,
            onValueChange = { cameraMotion = it },
            label = { Text("Camera & Motion") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = instructions,
            onValueChange = { instructions = it },
            label = { Text("Instructions") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Spacer(Modifier.height(10.dp))
        Text("Scene Duration")
        Row {
            listOf("5", "10", "30", "Custom").forEach { option ->
                Button(
                    onClick = { durationChoice = option },
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(if (durationChoice == option) "✓ $option" else option)
                }
            }
        }
        if (durationChoice == "Custom") {
            OutlinedTextField(
                value = customDuration,
                onValueChange = { customDuration = it.filter(Char::isDigit) },
                label = { Text("Custom duration in seconds") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(12.dp))
        Text("Split into Shots", style = MaterialTheme.typography.titleMedium)
        shotDescriptions.indices.forEach { i ->
            Text("Shot ${i + 1}")
            OutlinedTextField(
                value = shotDescriptions[i],
                onValueChange = { shotDescriptions[i] = it },
                label = { Text("Shot description") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = shotDurations[i],
                onValueChange = { shotDurations[i] = it.filter(Char::isDigit) },
                label = { Text("Shot duration in seconds") },
                modifier = Modifier.fillMaxWidth()
            )

            Row {
                Button(
                    onClick = {
                        if (i > 0) {
                            val description = shotDescriptions[i]
                            val duration = shotDurations[i]
                            shotDescriptions[i] = shotDescriptions[i - 1]
                            shotDurations[i] = shotDurations[i - 1]
                            shotDescriptions[i - 1] = description
                            shotDurations[i - 1] = duration
                        }
                    },
                    enabled = i > 0,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text("Move Up")
                }

                Button(
                    onClick = {
                        if (i < shotDescriptions.lastIndex) {
                            val description = shotDescriptions[i]
                            val duration = shotDurations[i]
                            shotDescriptions[i] = shotDescriptions[i + 1]
                            shotDurations[i] = shotDurations[i + 1]
                            shotDescriptions[i + 1] = description
                            shotDurations[i + 1] = duration
                        }
                    },
                    enabled = i < shotDescriptions.lastIndex,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text("Move Down")
                }

                Button(
                    onClick = {
                        if (shotDescriptions.size > 1) {
                            shotDescriptions.removeAt(i)
                            shotDurations.removeAt(i)
                        } else {
                            shotDescriptions[0] = ""
                            shotDurations[0] = "5"
                        }
                    }
                ) {
                    Text("Remove Shot")
                }
            }
        }
        Button(
            onClick = {
                shotDescriptions.add("")
                shotDurations.add("5")
            }
        ) {
            Text("Add Shot")
        }

        Spacer(Modifier.height(12.dp))
        if (activeShotIndices.isNotEmpty()) {
            when {
                invalidShotDuration -> Text(
                    "تنبیہ: ہر درج شدہ Shot کا دورانیہ کم از کم 1 سیکنڈ ہونا چاہیے۔",
                    color = MaterialTheme.colorScheme.error
                )
                selectedSceneDuration == null || selectedSceneDuration <= 0 -> Text(
                    "تنبیہ: Scene کا درست دورانیہ درج کریں۔",
                    color = MaterialTheme.colorScheme.error
                )
                totalShotDuration > selectedSceneDuration -> Text(
                    "دورانیہ زیادہ ہے: Scene ${selectedSceneDuration}s، " +
                        "Shots کا مجموعہ ${totalShotDuration}s۔",
                    color = MaterialTheme.colorScheme.error
                )
                totalShotDuration < selectedSceneDuration -> Text(
                    "دورانیہ کم ہے: Scene ${selectedSceneDuration}s، " +
                        "Shots کا مجموعہ ${totalShotDuration}s۔",
                    color = MaterialTheme.colorScheme.error
                )
                else -> Text(
                    "دورانیہ درست ہے: ${totalShotDuration}s",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val duration = if (durationChoice == "Custom") {
                    customDuration.toIntOrNull()
                } else {
                    durationChoice.toIntOrNull()
                }
                if (projectName.isNotBlank() &&
                    sceneDescription.isNotBlank() &&
                    duration != null && duration > 0 &&
                    (activeShotIndices.isEmpty() ||
                        (!invalidShotDuration && totalShotDuration == duration))
                ) {
                    val updated = org.json.JSONArray(scenesJson.ifBlank { "[]" })
                    val isEditing = editingSceneIndex in 0 until updated.length()
                    val sceneNumber = if (isEditing) {
                        updated.optJSONObject(editingSceneIndex)
                            ?.optInt("scene_number", editingSceneIndex + 1)
                            ?: (editingSceneIndex + 1)
                    } else {
                        updated.length() + 1
                    }
                    val scene = org.json.JSONObject()
                        .put("scene_number", sceneNumber)
                        .put("description", sceneDescription.trim())
                        .put("characters", characters.trim())
                        .put("camera_motion", cameraMotion.trim())
                        .put("instructions", instructions.trim())
                        .put("duration_seconds", duration)
                        .put("approved", true)

                    val shots = org.json.JSONArray()
                    shotDescriptions.indices.forEach { i ->
                        if (shotDescriptions[i].isNotBlank()) {
                            shots.put(
                                org.json.JSONObject()
                                    .put("description", shotDescriptions[i].trim())
                                    .put("duration_seconds", shotDurations[i].toIntOrNull() ?: 5)
                            )
                        }
                    }
                    scene.put("shots", shots)
                    if (isEditing) {
                        updated.put(editingSceneIndex, scene)
                    } else {
                        updated.put(scene)
                    }
                    scenesJson = updated.toString()
                    onSave(scenesJson)
                    editingSceneIndex = -1

                    sceneDescription = ""
                    characters = ""
                    cameraMotion = ""
                    instructions = ""
                    durationChoice = "5"
                    customDuration = ""
                    shotDescriptions.clear()
                    shotDescriptions.add("")
                    shotDurations.clear()
                    shotDurations.add("5")
                }
            },
            enabled = projectName.isNotBlank() &&
                sceneDescription.isNotBlank() &&
                selectedSceneDuration != null && selectedSceneDuration > 0 &&
                (activeShotIndices.isEmpty() ||
                    (!invalidShotDuration &&
                        totalShotDuration == selectedSceneDuration))
        ) {
            Text(if (editingSceneIndex >= 0) "Update & Approve Scene" else "Save & Approve Scene")
        }

        Spacer(Modifier.height(8.dp))
        Button(onClick = onBack) { Text("Back to New Project") }

        Spacer(Modifier.height(16.dp))
        Text("Saved Scenes", style = MaterialTheme.typography.titleLarge)
        val currentScenes = runCatching { org.json.JSONArray(scenesJson) }
            .getOrElse { org.json.JSONArray() }
        for (i in 0 until currentScenes.length()) {
            val item = currentScenes.optJSONObject(i) ?: continue
            Text(
                "Scene ${item.optInt("scene_number", i + 1)} — " +
                    item.optString("description") +
                    " (${item.optInt("duration_seconds")}s) — " +
                    if (item.optBoolean("approved")) "Approved" else "Saved",
                modifier = Modifier
                    .clickable(enabled = allowSavedSceneEditing) {
                        editingSceneIndex = i
                        sceneDescription = item.optString("description")
                        characters = item.optString("characters")
                        cameraMotion = item.optString("camera_motion")
                        instructions = item.optString("instructions")
                        val d = item.optInt("duration_seconds", 5)
                        if (d == 5 || d == 10 || d == 30) {
                            durationChoice = d.toString()
                            customDuration = ""
                        } else {
                            durationChoice = "Custom"
                            customDuration = d.toString()
                        }
                        val shots = item.optJSONArray("shots") ?: org.json.JSONArray()
                        shotDescriptions.clear()
                        shotDurations.clear()
                        for (j in 0 until shots.length()) {
                            val shot = shots.optJSONObject(j) ?: continue
                            shotDescriptions.add(shot.optString("description"))
                            shotDurations.add(shot.optInt("duration_seconds", 5).toString())
                        }
                        if (shotDescriptions.isEmpty()) {
                            shotDescriptions.add("")
                            shotDurations.add("5")
                        }
                    }
                    .padding(vertical = 6.dp)
            )
        }
    }
}


@Composable
fun VisualStyleScreen(
    projectName: String,
    savedStyleJson: String,
    onSave: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val saved = remember(savedStyleJson) {
        runCatching { org.json.JSONObject(savedStyleJson) }
            .getOrElse { org.json.JSONObject() }
    }
    var visualType by remember(savedStyleJson) {
        mutableStateOf(saved.optString("visual_type", "Realistic"))
    }
    var colorStyle by remember(savedStyleJson) {
        mutableStateOf(saved.optString("color_style", "Cinematic"))
    }
    var lighting by remember(savedStyleJson) {
        mutableStateOf(saved.optString("lighting", "Natural"))
    }
    var cameraStyle by remember(savedStyleJson) {
        mutableStateOf(saved.optString("camera_style", "Smooth cinematic"))
    }
    var referenceUri by remember(savedStyleJson) {
        mutableStateOf(saved.optString("reference_image", ""))
    }
    var autoDirector by remember(savedStyleJson) {
        mutableStateOf(saved.optBoolean("auto_director", true))
    }
    var savedStatus by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        referenceUri = uri?.toString().orEmpty()
        savedStatus = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Visual Style",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = projectName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                VisualStyleOption(
                    label = "Visual Type",
                    value = visualType,
                    options = listOf("Realistic", "3D", "Anime", "Cartoon", "Fantasy", "Stop Motion"),
                    onSelected = { visualType = it; savedStatus = false }
                )
            }
            item {
                VisualStyleOption(
                    label = "Color Style",
                    value = colorStyle,
                    options = listOf("Cinematic", "Warm", "Cool", "Vibrant", "Muted", "Black & White"),
                    onSelected = { colorStyle = it; savedStatus = false }
                )
            }
            item {
                VisualStyleOption(
                    label = "Lighting",
                    value = lighting,
                    options = listOf("Natural", "Soft", "Dramatic", "Low-key", "Bright studio", "Golden hour"),
                    onSelected = { lighting = it; savedStatus = false }
                )
            }
            item {
                VisualStyleOption(
                    label = "Camera Style",
                    value = cameraStyle,
                    options = listOf("Smooth cinematic", "Handheld", "Static shots", "Tracking shots", "Wide-angle", "Dynamic"),
                    onSelected = { cameraStyle = it; savedStatus = false }
                )
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Visual Reference",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (referenceUri.isBlank())
                                "No reference image selected"
                            else
                                "Reference image selected",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        OutlinedButton(
                            onClick = { imagePicker.launch("image/*") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Choose Reference Image")
                        }
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Style Preview",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Type: $visualType")
                        Text("Color: $colorStyle")
                        Text("Lighting: $lighting")
                        Text("Camera: $cameraStyle")
                        Text(
                            text = "This is a settings preview, not a generated video.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Auto Director",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Automatically choose suitable visual settings for each scene and shot while aiming to keep the overall style consistent."
                        )
                        OutlinedButton(
                            onClick = {
                                autoDirector = !autoDirector
                                savedStatus = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (autoDirector) "Auto Director: ON" else "Auto Director: OFF")
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }
            Button(
                onClick = {
                    val data = org.json.JSONObject()
                    data.put("visual_type", visualType)
                    data.put("color_style", colorStyle)
                    data.put("lighting", lighting)
                    data.put("camera_style", cameraStyle)
                    data.put("reference_image", referenceUri)
                    data.put("auto_director", autoDirector)
                    onSave(data.toString())
                    savedStatus = true
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (savedStatus) "Saved" else "Save Style")
            }
        }
    }
}


@Composable
fun CameraMotionScreen(
    savedCameraJson: String,
    onSave: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val savedRoot = remember(savedCameraJson) {
        runCatching { org.json.JSONObject(savedCameraJson) }
            .getOrElse { org.json.JSONObject() }
    }
    var recordsJson by remember(savedCameraJson) {
        mutableStateOf(
            (savedRoot.optJSONArray("shots") ?: org.json.JSONArray()).toString()
        )
    }
    var sceneNumber by remember { mutableStateOf("1") }
    var shotNumber by remember { mutableStateOf("1") }
    var cameraStyle by remember { mutableStateOf("Cinematic") }
    var movement by remember { mutableStateOf("Auto") }
    var framing by remember { mutableStateOf("Wide") }
    var speed by remember { mutableStateOf("Normal") }
    var instructions by remember { mutableStateOf("") }
    var autoDirector by remember(savedCameraJson) {
        mutableStateOf(savedRoot.optBoolean("auto_director", true))
    }
    var savedStatus by remember { mutableStateOf(false) }

    val records = runCatching { org.json.JSONArray(recordsJson) }
        .getOrElse { org.json.JSONArray() }

    androidx.compose.runtime.LaunchedEffect(sceneNumber, shotNumber, recordsJson) {
        val found = (0 until records.length()).asSequence()
            .mapNotNull { records.optJSONObject(it) }
            .firstOrNull {
                it.optInt("scene_number", -1) == (sceneNumber.toIntOrNull() ?: -2) &&
                it.optInt("shot_number", -1) == (shotNumber.toIntOrNull() ?: -2)
            }
        if (found != null) {
            cameraStyle = found.optString("camera_style", "Cinematic")
            movement = found.optString("movement", "Auto")
            framing = found.optString("framing", "Wide")
            speed = found.optString("speed", "Normal")
            instructions = found.optString("instructions", "")
        } else {
            cameraStyle = "Cinematic"
            movement = "Auto"
            framing = "Wide"
            speed = "Normal"
            instructions = ""
        }
        savedStatus = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Camera & Motion",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = sceneNumber,
                onValueChange = { sceneNumber = it.filter(Char::isDigit).take(4) },
                label = { Text("Scene Number") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = shotNumber,
                onValueChange = { shotNumber = it.filter(Char::isDigit).take(4) },
                label = { Text("Shot Number") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        VisualStyleOption(
            label = "Camera Style",
            value = cameraStyle,
            options = listOf("Cinematic", "Documentary", "Action", "Dramatic", "Smooth", "Handheld", "Aerial", "Creative"),
            onSelected = { cameraStyle = it; savedStatus = false }
        )
        VisualStyleOption(
            label = "Camera Movement",
            value = movement,
            options = listOf("Static", "Pan Left", "Pan Right", "Tilt Up", "Tilt Down", "Zoom In", "Zoom Out", "Dolly In", "Dolly Out", "Tracking Shot", "Orbit", "Crane Up", "Crane Down", "Handheld", "Follow Shot", "POV", "Auto"),
            onSelected = { movement = it; savedStatus = false }
        )
        VisualStyleOption(
            label = "Camera Framing",
            value = framing,
            options = listOf("Wide", "Medium", "Close-up", "Extreme Close-up", "Over-the-Shoulder", "Low Angle", "High Angle", "Bird's-eye View"),
            onSelected = { framing = it; savedStatus = false }
        )
        VisualStyleOption(
            label = "Movement Speed",
            value = speed,
            options = listOf("Slow", "Normal", "Fast", "Dynamic"),
            onSelected = { speed = it; savedStatus = false }
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = instructions,
            onValueChange = { instructions = it; savedStatus = false },
            label = { Text("Camera Instructions") },
            placeholder = { Text("Describe the camera direction for this shot") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = {
                    val next = (shotNumber.toIntOrNull() ?: 0) + 1
                    shotNumber = next.toString()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Next Shot")
            }
            Button(
                onClick = {
                    val scene = sceneNumber.toIntOrNull()
                    val shot = shotNumber.toIntOrNull()
                    if (scene != null && scene > 0 && shot != null && shot > 0) {
                        val updated = org.json.JSONArray()
                        for (i in 0 until records.length()) {
                            val old = records.optJSONObject(i) ?: continue
                            if (old.optInt("scene_number") != scene ||
                                old.optInt("shot_number") != shot) {
                                updated.put(old)
                            }
                        }
                        updated.put(
                            org.json.JSONObject()
                                .put("scene_number", scene)
                                .put("shot_number", shot)
                                .put("camera_style", cameraStyle)
                                .put("movement", movement)
                                .put("framing", framing)
                                .put("speed", speed)
                                .put("instructions", instructions.trim())
                        )
                        val root = org.json.JSONObject()
                            .put("auto_director", autoDirector)
                            .put("shots", updated)
                        recordsJson = updated.toString()
                        onSave(root.toString())
                        savedStatus = true
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (savedStatus) "Saved" else "Save Camera Settings")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Saved Shots",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        val displayRecords = runCatching { org.json.JSONArray(recordsJson) }
            .getOrElse { org.json.JSONArray() }
        for (i in 0 until displayRecords.length()) {
            val item = displayRecords.optJSONObject(i) ?: continue
            Text(
                text = "Scene ${item.optInt("scene_number")} • Shot ${item.optInt("shot_number")} — ${item.optString("camera_style")}, ${item.optString("movement")}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = {
                val enabled = !autoDirector
                autoDirector = enabled
                val root = org.json.JSONObject()
                    .put("auto_director", enabled)
                    .put("shots", org.json.JSONArray(recordsJson))
                onSave(root.toString())
                savedStatus = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Auto Director — Entire Project: " + if (autoDirector) "ON" else "OFF")
        }
        Text(
            text = "This setting applies to the whole project, across all scenes and shots.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
private fun VisualStyleOption(
    label: String,
    value: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(value)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onSelected(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StoryScriptScreen(
    projectName: String,
    parts: List<String>,
    onPartsChange: (List<String>) -> Unit,
    onSave: (List<String>) -> Unit,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var storySaved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Story / Script",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Build your story part by part.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(parts.size) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Part ${index + 1}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = parts[index],
                            onValueChange = { text ->
                                val updated = parts.toMutableList()
                                updated[index] = text
                                onPartsChange(updated)
                                storySaved = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 5,
                            label = { Text("Part ${index + 1} text") },
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onPartsChange(parts + "")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add Part")
                    }

                    Button(
                        onClick = {
                            onSave(parts)
                            focusManager.clearFocus(force = true)
                            storySaved = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (storySaved) "✓ Saved" else "Save")
                    }
                }
            }
        }
    }
}


@Composable
fun HomeDashboard(
    modifier: Modifier = Modifier,
    profileImageUri: Uri?,
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onVideoFactoryClick: () -> Unit = {},
    onProjectsClick: () -> Unit = {},
    onActiveJobsClick: () -> Unit = {},
    onYourVideosClick: () -> Unit = {},
    onCustomizeVideoClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val profileImageFile = File(context.filesDir, "profile_picture.jpg")
    val preferences = context.getSharedPreferences("vidforge_profile", Context.MODE_PRIVATE)
    val displayName = preferences.getString("display_name", "Profile") ?: "Profile"
    var showSettingsMenu by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onProfileClick() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.tertiary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUri != null) {
                            Image(
                                painter = coil.compose.rememberAsyncImagePainter(
                                    model = if (profileImageFile.exists()) profileImageFile else profileImageUri
                                ),
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = "VF",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "VidForge",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "0 Credits",
                        modifier = Modifier.padding(
                            horizontal = 11.dp,
                            vertical = 7.dp
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box {
                    IconButton(
                        onClick = { showSettingsMenu = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "More"
                        )
                    }

                    DropdownMenu(
                        expanded = showSettingsMenu,
                        onDismissRequest = { showSettingsMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                showSettingsMenu = false
                                onSettingsClick()
                            }
                        )
                    }
                }
            }
        }

        item {
            CreateVideoCard(
                onVideoFactoryClick = onVideoFactoryClick
            )
        }

        item {
            SectionHeader(
                title = "Workspace",
                action = "View all"
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    QuickActionCard(
                        title = "Projects",
                        subtitle = "Your videos",
                        icon = Icons.Default.Folder,
                        onClick = onProjectsClick
                    )
                }

                item {
                    QuickActionCard(
                        title = "Characters",
                        subtitle = "People & entities",
                        icon = Icons.Default.Person
                    )
                }

                item {
                    QuickActionCard(
                        title = "Active Jobs",
                        subtitle = "Generation queue",
                        icon = Icons.Default.AutoAwesome,
                        onClick = onActiveJobsClick
                    )
                }

                item {
                    QuickActionCard(
                        title = "Credits",
                        subtitle = "Balance & packages",
                        icon = Icons.Default.AutoAwesome
                    )
                }

                item {
                    QuickActionCard(
                        title = "Free Credits",
                        subtitle = "Watch an Ad → Earn Credits",
                        icon = Icons.Default.PlayArrow
                    )
                }

                item {
                    QuickActionCard(
                        title = "Tools",
                        subtitle = "Video, audio & subtitles",
                        icon = Icons.Default.AutoAwesome
                    )
                }
            }
        }

        item {
            SectionHeader(
                title = "Your Videos",
                action = "View all",
                onActionClick = onYourVideosClick
            )
        }

        item {
            CustomizeVideoCard(
                onClick = onCustomizeVideoClick
            )
        }

        item {
            SectionHeader(
                title = "Recent Projects",
                action = "See all"
            )
        }

        items(
            listOf(
                ProjectPreview(
                    "No Project and Video Yet",
                    "Create your first video project"
                )
            )
        ) { project ->
            ProjectCard(
    project,
    onOpen = onYourVideosClick
)
        }

        item {
            FactoryStatusCard()
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun CustomizeVideoCard(
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Customize Video",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Add, change or remove media your way.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Voice • Music • Effects • Timing",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CustomizeVideoScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedMedia = remember { mutableStateListOf<Uri>() }
    var selectedMusic by remember { mutableStateOf<Uri?>(null) }
    var musicDurationMs by remember { mutableStateOf(5000L) }
    var musicStartMs by remember { mutableStateOf(0L) }
    var musicEndMs by remember { mutableStateOf(5000L) }
    var videoDurationMs by remember { mutableStateOf(5000L) }
    var trimStartMs by remember { mutableStateOf(0L) }
    var trimEndMs by remember { mutableStateOf(videoDurationMs) }
    var currentPositionMs by remember { mutableStateOf(0L) }
    var seekRequestMs by remember { mutableStateOf(-1L) }
    var videoIsPlaying by remember { mutableStateOf(false) }
    var selectedTool by remember { mutableStateOf("Media") }
    var selectedTrack by remember { mutableStateOf<String?>(null) }
    var selectedClipIndex by remember { mutableStateOf<Int?>(null) }
    var selectedResolution by remember { mutableStateOf("1080p") }
    var showExportOptions by remember { mutableStateOf(false) }
    var showToolPanel by remember { mutableStateOf(true) }

    val tools = listOf(
        "Media",
        "Audio",
        "Voice",
        "Music",
        "SFX",
        "Text",
        "Filters",
        "Effects",
        "Transition",
        "Speed",
        "Volume",
        "Fade",
        "Crop",
        "Rotate",
        "Replace",
        "Split",
        "Delete"
    )

    val previewVideoUri = selectedMedia.firstOrNull()
    val timelinePixelsPerSecond = 24.dp
    val timelineScrollState = rememberScrollState()
    val timelineContentDurationMs = maxOf(
        videoDurationMs + 3000L,
        (musicEndMs - musicStartMs).coerceAtLeast(1000L) + 3000L,
        9250L
    )
    val timelineDensity = LocalDensity.current.density
    val appContext = LocalContext.current

    LaunchedEffect(videoIsPlaying, currentPositionMs, timelineScrollState.maxValue) {
        if (videoIsPlaying && timelineScrollState.viewportSize > 0) {
            val playheadPx = (
                currentPositionMs / 1000f *
                    timelinePixelsPerSecond.value *
                    timelineDensity
            ).toInt()

            val viewportPx = timelineScrollState.viewportSize
            val currentScroll = timelineScrollState.value
            val visibleRightEdge = currentScroll + (viewportPx * 0.75f).toInt()

            if (playheadPx > visibleRightEdge || playheadPx < currentScroll) {
                val targetScroll = (
                    playheadPx - (viewportPx * 0.70f).toInt()
                ).coerceIn(0, timelineScrollState.maxValue)

                timelineScrollState.scrollTo(targetScroll)
            }
        }
    }

    val musicPlayer = remember(selectedMusic) {
        selectedMusic?.let { uri ->
            ExoPlayer.Builder(appContext).build().apply {
                setMediaItem(MediaItem.fromUri(uri))
                prepare()
                playWhenReady = false
            }
        }
    }

    DisposableEffect(musicPlayer) {
        onDispose {
            musicPlayer?.release()
        }
    }

    LaunchedEffect(musicPlayer, videoIsPlaying, seekRequestMs, musicStartMs, musicEndMs) {
        val player = musicPlayer ?: return@LaunchedEffect
        val musicPositionMs = musicStartMs + currentPositionMs

        if (musicPositionMs < musicEndMs) {
            if (kotlin.math.abs(player.currentPosition - musicPositionMs) > 120L) {
                player.seekTo(musicPositionMs)
            }
            player.playWhenReady = videoIsPlaying
        } else {
            player.pause()
        }
    }

    LaunchedEffect(musicPlayer, currentPositionMs, videoIsPlaying, musicStartMs, musicEndMs) {
        val player = musicPlayer ?: return@LaunchedEffect
        val musicPositionMs = musicStartMs + currentPositionMs

        if (musicPositionMs < musicEndMs) {
            player.playWhenReady = videoIsPlaying
        } else {
            player.pause()
        }
    }

    

    LaunchedEffect(previewVideoUri) {
        if (previewVideoUri != null) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(appContext, previewVideoUri)
                val duration = retriever.extractMetadata(
                    MediaMetadataRetriever.METADATA_KEY_DURATION
                )?.toLongOrNull()
                videoDurationMs = if (duration != null && duration > 0L) duration else 5000L
                trimEndMs = if (duration != null && duration > 0L) duration else 5000L
            } catch (_: Exception) {
                videoDurationMs = 5000L
            } finally {
                retriever.release()
            }
        } else {
            videoDurationMs = 5000L
        }
    }


    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedMedia.clear()
            selectedMedia.addAll(uris)
        }
    }

    val pickMusic = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            selectedMusic = uri

            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(appContext, uri)
                val duration = retriever.extractMetadata(
                    MediaMetadataRetriever.METADATA_KEY_DURATION
                )?.toLongOrNull()

                musicDurationMs = if (duration != null && duration > 0L) {
                    duration
                } else {
                    5000L
                }

                musicStartMs = 0L
                musicEndMs = musicDurationMs
            } catch (_: Exception) {
                musicDurationMs = 5000L
                musicStartMs = 0L
                musicEndMs = 5000L
            } finally {
                retriever.release()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Editor",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "VidForge Video Editor",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedButton(
                onClick = { }
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.width(6.dp))

            Button(
                onClick = { showExportOptions = true }
            ) {
                Text("Export")
            }
        }

        Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (previewVideoUri != null) {
                              Box(
                                  modifier = Modifier
                                      .fillMaxWidth()
                                      .aspectRatio(1f)
                                      .padding(vertical = 12.dp)
                              ) {
                            OfflineVideoPreview(
                                videoUri = previewVideoUri,
                                modifier = Modifier.fillMaxSize(),
                                seekPositionMs = seekRequestMs,
                                onPositionChanged = { position -> currentPositionMs = position.coerceIn(trimStartMs, trimEndMs) },
                                onPlaybackChanged = { playing -> videoIsPlaying = playing },
                                onDurationChanged = { duration ->
                                    if (duration > 0L && duration != videoDurationMs) {
                                        videoDurationMs = duration
                                        trimEndMs = duration
                                    }
                                }
                            )
                              }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Preview",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Add media to start editing",
                                    color = Color.White.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "00:00",
                            style = MaterialTheme.typography.labelSmall
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant
                                )
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = String.format("%02d:%02d", videoDurationMs / 60000, (videoDurationMs / 1000) % 60),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Video",
                                    modifier = Modifier.width(58.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .horizontalScroll(timelineScrollState)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(
                                                (timelineContentDurationMs / 1000f *
                                                    timelinePixelsPerSecond.value).dp
                                            )
                                            .height(42.dp)
                                    ) {
                                        repeat(selectedMedia.size.coerceAtLeast(1)) { index ->
                                            val isSelected =
                                                selectedTrack == "Video" &&
                                                    selectedClipIndex == index

                                            Box(
                                                modifier = Modifier
                                                    .width(
                                                        (timelineContentDurationMs / 1000f *
                                                            timelinePixelsPerSecond.value).dp
                                                    )
                                                    .height(42.dp)
                                                    .clip(RoundedCornerShape(7.dp))
                                                    .background(Color(0xFF315A78))
                                                    .clickable {
                                                        if (isSelected) {
                                                            selectedTrack = null
                                                            selectedClipIndex = null
                                                        } else {
                                                            selectedTrack = "Video"
                                                            selectedClipIndex = index
                                                        }
                                                    }
                                                    .then(
                                                        if (isSelected) {
                                                            Modifier.border(
                                                                width = 2.dp,
                                                                color = MaterialTheme.colorScheme.primary,
                                                                shape = RoundedCornerShape(7.dp)
                                                            )
                                                        } else {
                                                            Modifier
                                                        }
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "Clip ${index + 1}",
                                                    color = Color.White,
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            }
                                        }

                                        Box(
                                            modifier = Modifier
                                                .offset(
                                                    x = (trimStartMs / 1000f *
                                                        timelinePixelsPerSecond.value).dp
                                                )
                                                .height(42.dp)
                                                .width(18.dp)
                                                .pointerInput(videoDurationMs, trimEndMs) {
                                                    detectDragGestures { change, dragAmount ->
                                                        change.consume()
                                                        val deltaMs =
                                                            (dragAmount.x /
                                                                timelinePixelsPerSecond.value *
                                                                1000f).toLong()

                                                        trimStartMs =
                                                            (trimStartMs + deltaMs)
                                                                .coerceIn(
                                                                    0L,
                                                                    (trimEndMs - 1000L)
                                                                        .coerceAtLeast(0L)
                                                                )

                                                        currentPositionMs =
                                                            currentPositionMs.coerceIn(
                                                                trimStartMs,
                                                                trimEndMs
                                                            )
                                                    }
                                                }
                                                .background(Color.White.copy(alpha = 0.9f))
                                        )

                                        Box(
                                            modifier = Modifier
                                                .offset(
                                                    x = (trimEndMs / 1000f *
                                                        timelinePixelsPerSecond.value).dp
                                                )
                                                .height(42.dp)
                                                .width(18.dp)
                                                .pointerInput(videoDurationMs, trimStartMs) {
                                                    detectDragGestures { change, dragAmount ->
                                                        change.consume()
                                                        val deltaMs =
                                                            (dragAmount.x /
                                                                timelinePixelsPerSecond.value *
                                                                1000f).toLong()

                                                        trimEndMs =
                                                            (trimEndMs + deltaMs)
                                                                .coerceIn(
                                                                    (trimStartMs + 1000L)
                                                                        .coerceAtMost(videoDurationMs),
                                                                    videoDurationMs
                                                                )

                                                        currentPositionMs =
                                                            currentPositionMs.coerceIn(
                                                                trimStartMs,
                                                                trimEndMs
                                                            )
                                                    }
                                                }
                                                .background(Color.White.copy(alpha = 0.9f))
                                        )

                                        Box(
                                            modifier = Modifier
                                                .offset(
                                                    x = ((currentPositionMs / 1000f *
                                                        timelinePixelsPerSecond.value) - 14f).dp
                                                )
                                                .height(42.dp)
                                                .width(28.dp)
                                                .pointerInput(
                                                    videoDurationMs,
                                                    trimStartMs,
                                                    trimEndMs
                                                ) {
                                                    detectDragGestures { change, dragAmount ->
                                                        change.consume()
                                                        val deltaMs =
                                                            (dragAmount.x /
                                                                timelinePixelsPerSecond.value *
                                                                1000f).toLong()

                                                        val newPositionMs = (currentPositionMs + deltaMs).coerceIn(trimStartMs, trimEndMs)
                                                        currentPositionMs = newPositionMs
                                                        seekRequestMs = newPositionMs
                                                    }
                                                }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Music",
                                    modifier = Modifier.width(58.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                val musicTrackDurationMs =
                                    (musicEndMs - musicStartMs).coerceAtLeast(1000L)

                                val isMusicSelected =
                                    selectedTrack == "Music" && selectedMusic != null

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(30.dp)
                                        .horizontalScroll(timelineScrollState)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(
                                                (timelineContentDurationMs / 1000f *
                                                    timelinePixelsPerSecond.value).dp
                                            )
                                            .height(30.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .offset(
                                                    x = (musicStartMs / 1000f *
                                                        timelinePixelsPerSecond.value).dp
                                                )
                                                .width(
                                                    (musicTrackDurationMs / 1000f *
                                                        timelinePixelsPerSecond.value).dp
                                                )
                                                .height(30.dp)
                                                .clip(RoundedCornerShape(7.dp))
                                                .background(Color(0xFF168FA5))
                                                .clickable(enabled = selectedMusic != null) {
                                                    if (isMusicSelected) {
                                                        selectedTrack = null
                                                        selectedClipIndex = null
                                                    } else {
                                                        selectedTrack = "Music"
                                                        selectedClipIndex = null
                                                    }
                                                }
                                                .then(
                                                    if (isMusicSelected) {
                                                        Modifier.border(
                                                            width = 2.dp,
                                                            color = MaterialTheme.colorScheme.primary,
                                                            shape = RoundedCornerShape(7.dp)
                                                        )
                                                    } else {
                                                        Modifier
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (selectedMusic != null) {
                                                    "Music Track"
                                                } else {
                                                    "Add Music"
                                                },
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Voice",
                                    modifier = Modifier.width(58.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(30.dp)
                                        .horizontalScroll(timelineScrollState)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(
                                                (timelineContentDurationMs / 1000f *
                                                    timelinePixelsPerSecond.value).dp
                                            )
                                            .height(30.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(150.dp)
                                                .height(30.dp)
                                                .clip(RoundedCornerShape(7.dp))
                                                .background(Color(0xFFB08A32)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Voice Track",
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(start = 58.dp)
                        ) {
                            val playheadLimitMs =
                                trimEndMs.coerceIn(0L, videoDurationMs.coerceAtLeast(0L))
                            val playheadPositionMs =
                                currentPositionMs.coerceIn(trimStartMs, playheadLimitMs)

                            Box(
                                modifier = Modifier
                                    .offset(
                                        x = ((playheadPositionMs / 1000f *
                                            timelinePixelsPerSecond.value) -
                                            (timelineScrollState.value /
                                                LocalDensity.current.density)).dp
                                    )
                                    .fillMaxHeight()
                                    .width(2.dp)
                                    .background(Color.Red)
                            )
                        }
                    }

                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 6.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(
                    "Cut",
                    "Split",
                    "Delete",
                    "Duplicate",
                    "Replace",
                    "More"
                ).forEach { action ->
                    OutlinedButton(
                        onClick = {
                            if (action == "More") {
                                selectedTool = "More"
                                showToolPanel = true
                            }
                        },
                        modifier = Modifier.padding(end = 5.dp)
                    ) {
                        Text(action)
                    }
                }
            }

            if (showToolPanel) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 5.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedTool,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )

                            TextButton(
                                onClick = { showToolPanel = false }
                            ) {
                                Text("Close")
                            }
                        }

                        when (selectedTool) {
                            "Media" -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(
                                            rememberScrollState()
                                        )
                                ) {
                                    OutlinedButton(
                                        onClick = { pickMedia.launch(arrayOf("video/*")) }
                                    ) {
                                        Text("Add Video")
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    OutlinedButton(
                                        onClick = { pickMedia.launch(arrayOf("*/*")) }
                                    ) {
                                        Text("Add Media")
                                    }
                                }
                            }

                            "Audio" -> EditorToolPanel(
                                title = "Audio",
                                options = listOf(
                                    "Music",
                                    "Sound Effects",
                                    "Background Audio",
                                    "Audio Track"
                                )
                            )

                            "Voice" -> EditorToolPanel(
                                title = "Voice",
                                options = listOf(
                                    "Voice Track",
                                    "Record Voice",
                                    "Voice Position",
                                    "Voice Volume"
                                )
                            )

                            "Music" -> EditorToolPanel(
                                title = "Music",
                                options = listOf(
                                    "Music Library",
                                    "Add Music",
                                    "Music Volume",
                                    "Fade In / Out"
                                ),
                                onOptionClick = { option ->
                                    if (option == "Add Music") {
                                        pickMusic.launch(arrayOf("audio/*"))
                                    }
                                }
                            )

                            "SFX" -> EditorToolPanel(
                                title = "SFX",
                                options = listOf(
                                    "Add SFX",
                                    "SFX Volume",
                                    "SFX Position",
                                    "Fade"
                                )
                            )

                            "Text" -> EditorToolPanel(
                                title = "Text",
                                options = listOf(
                                    "Add Text",
                                    "Text Style",
                                    "Text Animation",
                                    "Text Position"
                                )
                            )

                            "Filters" -> EditorToolPanel(
                                title = "Filters",
                                options = listOf(
                                    "Original",
                                    "Warm",
                                    "Cool",
                                    "Cinematic",
                                    "Classic"
                                )
                            )

                            "Effects" -> EditorToolPanel(
                                title = "Effects",
                                options = listOf(
                                    "Video Effects",
                                    "Motion Effects",
                                    "Blur",
                                    "Glow"
                                )
                            )

                            "Transition" -> EditorToolPanel(
                                title = "Transition",
                                options = listOf(
                                    "None",
                                    "Fade",
                                    "Dissolve",
                                    "Slide",
                                    "Wipe"
                                )
                            )

                            "Speed" -> EditorToolPanel(
                                title = "Speed",
                                options = listOf(
                                    "0.25x",
                                    "0.5x",
                                    "1x",
                                    "1.5x",
                                    "2x"
                                )
                            )

                            "Volume" -> EditorToolPanel(
                                title = "Volume",
                                options = listOf(
                                    "Original Sound",
                                    "Mute",
                                    "Volume",
                                    "Audio Balance"
                                )
                            )

                            "Fade" -> EditorToolPanel(
                                title = "Fade",
                                options = listOf(
                                    "Fade In",
                                    "Fade Out",
                                    "Audio Fade",
                                    "Video Fade"
                                )
                            )

                            "Crop" -> EditorToolPanel(
                                title = "Crop",
                                options = listOf(
                                    "Free",
                                    "16:9",
                                    "9:16",
                                    "1:1",
                                    "4:5"
                                )
                            )

                            "Rotate" -> EditorToolPanel(
                                title = "Rotate",
                                options = listOf(
                                    "Rotate Left",
                                    "Rotate Right",
                                    "Flip Horizontal",
                                    "Flip Vertical"
                                )
                            )

                            "Replace" -> EditorToolPanel(
                                title = "Replace",
                                options = listOf(
                                    "Replace Selected",
                                    "Replace Media",
                                    "Replace Audio"
                                )
                            )

                            "Split" -> EditorToolPanel(
                                title = "Split",
                                options = listOf(
                                    "Split Clip",
                                    "Split Audio",
                                    "Split at Playhead"
                                )
                            )

                            "Delete" -> EditorToolPanel(
                                title = "Delete",
                                options = listOf(
                                    "Delete Clip",
                                    "Delete Track",
                                    "Delete Audio"
                                )
                            )

                            "More" -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState())
                                    ) {
                                        OutlinedButton(
                                            onClick = { pickMedia.launch(arrayOf("video/*")) }
                                        ) {
                                            Text("Add Video")
                                        }

                                        Spacer(modifier = Modifier.width(5.dp))

                                        OutlinedButton(
                                            onClick = { pickMedia.launch(arrayOf("*/*")) }
                                        ) {
                                            Text("Add Media")
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState())
                                    ) {
                                        listOf(
                                            "Media",
                                            "Audio",
                                            "Voice",
                                            "Music",
                                            "SFX"
                                        ).forEach { tool ->
                                            OutlinedButton(
                                                onClick = {
                                                    selectedTool = tool
                                                    showToolPanel = true
                                                },
                                                modifier = Modifier.padding(end = 5.dp)
                                            ) {
                                                Text(tool)
                                            }
                                        }
                                    }

                                    tools.drop(5).forEach { tool ->
                                        OutlinedButton(
                                            onClick = {
                                                selectedTool = tool
                                                showToolPanel = true
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp)
                                        ) {
                                            Text(tool)
                                        }
                                    }

                                    EditorToolPanel(
                                        title = "More",
                                        options = listOf(
                                            "Adjust",
                                            "Canvas",
                                            "Background",
                                            "Overlay",
                                            "Mask",
                                            "Properties"
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    if (showExportOptions) {
        AlertDialog(
            onDismissRequest = {
                showExportOptions = false
            },
            title = {
                Text("Export Video")
            },
            text = {
                Column {
                    Text(
                        text = "Choose export resolution",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    listOf(
                        "360p",
                        "480p",
                        "720p",
                        "1080p",
                        "4K"
                    ).forEach { resolution ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedResolution = resolution
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = resolution,
                                modifier = Modifier.weight(1f)
                            )

                            if (selectedResolution == resolution) {
                                Text(
                                    text = "Selected",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExportOptions = false
                    }
                ) {
                    Text("Export $selectedResolution")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showExportOptions = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

}

@Composable
fun EditorToolPanel(
    title: String,
    options: List<String>,
    onOptionClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(14.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                
        ) {
            options.forEach { option ->
                OutlinedButton(
                    onClick = { onOptionClick(option) },
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(option)
                }
            }
        }
    }
}

@Composable
fun OfflineVideoPreview(
    videoUri: Uri,
    modifier: Modifier = Modifier,
    seekPositionMs: Long = -1L,
    onPositionChanged: (Long) -> Unit = {},
    onDurationChanged: (Long) -> Unit = {},
    onPlaybackChanged: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current

    val player = remember(videoUri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(player) {
        onDispose { player.release() }
    }

    LaunchedEffect(player) {
        while (true) {
            val duration = player.duration
            if (duration > 0L) {
                onDurationChanged(duration)
            }
            onPositionChanged(player.currentPosition.coerceAtLeast(0L))
            onPlaybackChanged(player.isPlaying)
            delay(50L)
        }
    }

    LaunchedEffect(seekPositionMs) {
        if (seekPositionMs >= 0L) {
            val current = player.currentPosition.coerceAtLeast(0L)
            if (kotlin.math.abs(seekPositionMs - current) > 40L) {
                player.seekTo(seekPositionMs)
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                this.player = player
                useController = false
                resizeMode =
                    androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT
                setShutterBackgroundColor(android.graphics.Color.BLACK)

                var downX = 0f
                var downY = 0f
                var moved = false

                val scaleDetector = ScaleGestureDetector(
                    ctx,
                    object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                        override fun onScale(detector: ScaleGestureDetector): Boolean {
                            scaleX = (scaleX * detector.scaleFactor).coerceIn(1f, 3f)
                            scaleY = scaleX
                            moved = true
                            return true
                        }
                    }
                )

                setOnTouchListener { view, event ->
                    scaleDetector.onTouchEvent(event)

                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            downX = event.x
                            downY = event.y
                            moved = false
                            true
                        }

                        MotionEvent.ACTION_MOVE -> {
                            if (event.pointerCount == 1 && !scaleDetector.isInProgress) {
                                val dx = event.x - downX
                                val dy = event.y - downY
                                if (kotlin.math.abs(dx) > 6f || kotlin.math.abs(dy) > 6f) {
                                    moved = true
                                }
                                if (moved) {
                                    translationX += dx
                                    translationY += dy
                                    downX = event.x
                                    downY = event.y
                                }
                            }
                            true
                        }

                        MotionEvent.ACTION_UP -> {
                            if (!moved && !scaleDetector.isInProgress) {
                                player.playWhenReady = !player.isPlaying
                                onPlaybackChanged(player.playWhenReady)
                            }
                            true
                        }

                        MotionEvent.ACTION_CANCEL -> true
                        else -> true
                    }
                }
            }
        },
        update = { view ->
            view.player = player
            view.useController = false
        }
    )
}

@Composable
fun CreateVideoCard(
    onVideoFactoryClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF111827),
                        Color(0xFF1E1B4B),
                        Color(0xFF312E81)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(150.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xFF6366F1).copy(alpha = 0.42f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier.padding(22.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White.copy(alpha = 0.10f)
            ) {
                Text(
                    text = "AI VIDEO FACTORY",
                    modifier = Modifier.padding(
                        horizontal = 11.dp,
                        vertical = 7.dp
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Turn ideas into cinematic videos.",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create scenes, characters, audio and final video from one workspace.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.72f)
            )

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onVideoFactoryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(17.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    "Start Creating",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    action: String,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = action,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit = {}
) {
    val accent = when (title) {
        "Projects" -> Color(0xFF2563EB)
        "Characters" -> Color(0xFF7C3AED)
        "Active Jobs" -> Color(0xFF0891B2)
        "Credits" -> Color(0xFFD97706)
        "Free Credits" -> Color(0xFF16A34A)
        "Tools" -> Color(0xFF4F46E5)
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier
            .width(164.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            accent,
                            accent.copy(alpha = 0.35f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(accent.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = accent
                )
            }

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class ProjectPreview(
    val title: String,
    val subtitle: String
)

@Composable
fun ProjectCard(
    project: ProjectPreview,
    onOpen: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF111827),
                                Color(0xFF3730A3),
                                Color(0xFF0891B2)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(15.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = project.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = onOpen,
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Open")
            }
        }
    }
}

data class VideoPreview(
    val title: String,
    val subtitle: String,
    val status: String = "completed",
    val progress: Int = 0
)

@Composable
fun VideoCard(
    video: VideoPreview,
    onOpen: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF111827),
                                    Color(0xFF3730A3),
                                    Color(0xFF0891B2)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = video.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = video.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (
                video.status.equals("running", ignoreCase = true) ||
                video.status.equals("queued", ignoreCase = true)
            ) {
                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = {
                        video.progress.coerceIn(0, 100) / 100f
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (video.status.equals("queued", ignoreCase = true))
                        "Queued"
                    else
                        "Generating",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun YourVideosScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val jobs = JobStore.loadJobs(context).filter {
        it.status.equals("running", ignoreCase = true) ||
            it.status.equals("queued", ignoreCase = true) ||
            it.status.equals("completed", ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Your Videos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (jobs.isEmpty()) {
        } else {
            jobs.forEach { job ->
                VideoCard(
                    VideoPreview(
                        title = job.title.ifBlank { "Generated Video" },
                        subtitle = job.projectName.ifBlank { job.type },
                        status = job.status,
                        progress = job.progress
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun FactoryStatusCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111827)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF16A34A).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(13.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4ADE80))
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Factory Ready",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Video generation services are ready",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.65f)
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF16A34A).copy(alpha = 0.15f)
            ) {
                Text(
                    text = "READY",
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4ADE80)
                )
            }
        }
    }
}

data class GoogleAccountInfo(
    val uniqueId: String,
    val idToken: String,
    val displayName: String?,
    val email: String?,
    val profilePictureUri: Uri?
)

suspend fun connectGoogleToBackend(idToken: String, onStep: (String) -> Unit = {}): Int? {
    return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        onStep("STEP 5A — Backend URL کھولا جا رہا ہے")
        val connection = (URL("http://127.0.0.1:8000/api/auth/google").openConnection() as HttpURLConnection)

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Connection", "close")
            connection.doOutput = true
            connection.connectTimeout = 60000
            connection.readTimeout = 60000

            onStep("STEP 5B — Backend request تیار")
            val body = JSONObject()
                .put("id_token", idToken)
                .toString()

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(body)
                writer.flush()
            }

            android.util.Log.d("VidForgeAuth", "STEP 5B-1: Request body sent")
            onStep("STEP 5C — Backend response کا انتظار")
            android.util.Log.d("VidForgeAuth", "STEP 5C-1: Calling responseCode")

            try {
                val responseCode = connection.responseCode
                android.util.Log.d("VidForgeAuth", "STEP 5C-2: responseCode returned: $responseCode")
                onStep("STEP 5C-2 — responseCode واپس آ گیا: $responseCode")

                val stream = if (responseCode in 200..299) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }

                onStep("STEP 5D — Backend response موصول")
                val response = stream?.bufferedReader()?.use { it.readText() } ?: ""
                android.util.Log.d("VidForgeAuth", "STEP 5D-1: Response body length=${response.length}")
                android.util.Log.d("VidForgeAuth", "STEP 5D-2: Response body received")

                val authResponse = JSONObject(response)
                val backendUserId = authResponse.optInt("user_id", -1)
                return@withContext if (
                    responseCode in 200..299 &&
                    authResponse.optString("status") == "connected" &&
                    backendUserId > 0
                ) backendUserId else null
            } catch (e: Exception) {
                android.util.Log.e(
                    "VidForgeAuth",
                    "BACKEND NETWORK ERROR: ${e::class.java.name}: ${e.message}",
                    e
                )
                onStep("STEP 5 ERROR — ${e::class.java.simpleName}: ${e.message ?: "unknown error"}")
                return@withContext null
            }
        } finally {
            connection.disconnect()
        }
    }
}

suspend fun postVidForgeJson(path: String, payload: JSONObject): JSONObject {
    return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val connection = (URL("http://127.0.0.1:8000$path").openConnection() as HttpURLConnection)
        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Connection", "close")
            connection.doOutput = true
            connection.connectTimeout = 30000
            connection.readTimeout = 30000

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(payload.toString())
                writer.flush()
            }

            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val body = stream?.bufferedReader()?.use { it.readText() } ?: ""
            if (code !in 200..299) {
                throw IllegalStateException("Server HTTP $code: $body")
            }
            JSONObject(body)
        } finally {
            connection.disconnect()
        }
    }
}

suspend fun getVidForgeJson(path: String): JSONObject {
    return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val connection = URL("http://127.0.0.1:8000$path")
            .openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.setRequestProperty("Connection", "close")
            connection.connectTimeout = 10000
            connection.readTimeout = 15000

            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val body = stream?.bufferedReader()?.use { it.readText() } ?: ""
            if (code !in 200..299) {
                throw IllegalStateException("Server HTTP $code: $body")
            }
            JSONObject(body)
        } finally {
            connection.disconnect()
        }
    }
}

suspend fun uploadVidForgeReferenceImage(context: Context, uri: Uri): String {
    return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val mimeType = context.contentResolver.getType(uri)
            ?: throw IllegalStateException("Image ka type maloom nahi.")

        val connection = URL("http://127.0.0.1:8000/api/reference-images")
            .openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", mimeType)
            connection.setRequestProperty("Connection", "close")
            connection.doOutput = true
            connection.connectTimeout = 30000
            connection.readTimeout = 30000

            val input = context.contentResolver.openInputStream(uri)
                ?: throw IllegalStateException("Image file khul nahi saki.")

            input.use { source ->
                connection.outputStream.use { output ->
                    source.copyTo(output)
                }
            }

            val code = connection.responseCode
            val stream = if (code in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }
            val body = stream?.bufferedReader()?.use { it.readText() } ?: ""

            if (code !in 200..299) {
                throw IllegalStateException("Image upload HTTP $code: $body")
            }

            val path = JSONObject(body).optString("reference_image_path")
            if (path.isBlank()) {
                throw IllegalStateException("Server ne image path nahi diya.")
            }
            path
        } finally {
            connection.disconnect()
        }
    }
}

suspend fun clearGoogleCredentialState(context: Context) {
    val credentialManager = CredentialManager.create(context)
    credentialManager.clearCredentialState(ClearCredentialStateRequest())
}


suspend fun signInWithGoogle(context: Context): GoogleAccountInfo? {
    android.util.Log.d("VidForgeAuth", "STEP 1: Google Credential request started")
    val credentialManager = CredentialManager.create(context)

    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(context.getString(R.string.google_web_client_id))
        .setAutoSelectEnabled(false)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    android.util.Log.d("VidForgeAuth", "STEP 2: Calling Credential Manager")
    val result = credentialManager.getCredential(
        context = context,
        request = request
    )

    android.util.Log.d("VidForgeAuth", "STEP 3: Credential Manager returned")
    val credential = result.credential

    if (
        credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) {
        android.util.Log.d("VidForgeAuth", "STEP 4: Google credential received")
        val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)

        return GoogleAccountInfo(
            uniqueId = googleCredential.id,
            idToken = googleCredential.idToken,
            displayName = googleCredential.displayName,
            email = googleCredential.id,
            profilePictureUri = googleCredential.profilePictureUri
        )
    }

    return null
}

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    data class SettingItem(
        val title: String,
        val subtitle: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val accent: Color
    )

    val settingsItems = listOf(
        SettingItem("App Preferences", "General app behavior and preferences", Icons.Default.Tune, Color(0xFF68758A)),
        SettingItem("Language", "Choose your preferred app language", Icons.Default.Person, Color(0xFF4D86C7)),
        SettingItem("Theme / Appearance", "Light, dark and visual appearance", Icons.Default.AutoAwesome, Color(0xFF7657C8)),
        SettingItem("Notifications", "Alerts and notification preferences", Icons.Default.MoreHoriz, Color(0xFFD18A4A)),
        SettingItem("Sound & Vibration", "Sound, voice and vibration controls", Icons.Default.Mic, Color(0xFF3F9D91)),
        SettingItem("Privacy", "Privacy controls and data preferences", Icons.Default.Person, Color(0xFF4C9E9A)),
        SettingItem("Security", "Account and app security settings", Icons.Default.AdminPanelSettings, Color(0xFF59627A)),
        SettingItem("Server Connection", "Server status and connection settings", Icons.Default.Cloud, Color(0xFF4E91C9)),
        SettingItem("Storage & Cache", "Storage usage and cached data", Icons.Default.Folder, Color(0xFFD18A4A)),
        SettingItem("About VidForge", "Version, information and app details", Icons.Default.AutoAwesome, Color(0xFF5B6FE8)),
        SettingItem("Data & Permissions", "App access and permission controls", Icons.Default.Tune, Color(0xFF68758A)),
        SettingItem("Help & Support", "Help, guidance and support options", Icons.Default.Person, Color(0xFF4F9B6C))
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Shape your VidForge experience",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF1F3FF)
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color(0xFFE1E4F5)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF5B6FE8).copy(alpha = 0.13f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        tint = Color(0xFF5B6FE8)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "VidForge Preferences",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Personalize the way your workspace feels and works.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 30.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(settingsItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFDFDFF)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        item.accent.copy(alpha = 0.16f)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(item.accent.copy(alpha = 0.11f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(23.dp),
                                tint = item.accent
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(item.accent.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "›",
                                style = MaterialTheme.typography.titleLarge,
                                color = item.accent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    profileImageUri: Uri?,
    onProfileImageSelected: (Uri?) -> Unit,
    googleAccount: GoogleAccountInfo?,
    onGoogleSignIn: (GoogleAccountInfo?) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current
    val profileImageFile = File(context.filesDir, "profile_picture.jpg")
    val preferences = context.getSharedPreferences("vidforge_profile", Context.MODE_PRIVATE)
    var displayName by remember {
        mutableStateOf(preferences.getString("display_name", "VidForge User") ?: "VidForge User")
    }
    var nameSaved by remember {
        mutableStateOf(preferences.getBoolean("display_name_saved", false))
    }
    var profileImageSaved by remember {
        mutableStateOf(preferences.getBoolean("profile_image_saved", profileImageFile.exists()))
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    var googleSignInError by remember { mutableStateOf<String?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    profileImageFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                onProfileImageSelected(Uri.fromFile(profileImageFile))
            profileImageSaved = false
            preferences.edit().putBoolean("profile_image_saved", false).apply()
            } catch (e: Exception) {
                onProfileImageSelected(null)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .size(92.dp)
                .clip(CircleShape)
                .clickable {
                    imagePicker.launch("image/*")
                }
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                Image(
                    painter = coil.compose.rememberAsyncImagePainter(
                        model = if (profileImageFile.exists()) profileImageFile else profileImageUri
                    ),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "VF",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                profileImageSaved = true
                preferences.edit().putBoolean("profile_image_saved", true).apply()
            },
            enabled = !profileImageSaved
        ) {
            Text(if (profileImageSaved) "Picture Saved" else "Save Picture")
        }

        Spacer(modifier = Modifier.height(16.dp))

        androidx.compose.material3.OutlinedTextField(
            value = displayName,
            onValueChange = {
                displayName = it
                nameSaved = false
            },
            label = { Text("Display Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                preferences.edit()
                    .putString("display_name", displayName.trim())
                    .putBoolean("display_name_saved", true)
                    .apply()
                nameSaved = true
                keyboardController?.hide()
                focusManager.clearFocus()
            },
            enabled = !nameSaved
        ) {
            Text(if (nameSaved) "Saved" else "Save Name")
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Personal video factory workspace",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Account",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (googleAccount != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "G",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = googleAccount.displayName ?: "Google Account",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = googleAccount.email ?: "Google Account",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Google Account Connected",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF16A34A)
                    )
                } else {
                    Text(
                        text = "Google Account",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            googleSignInError = null
                            coroutineScope.launch {
                                try {
                                    val account = signInWithGoogle(context)
                                    onGoogleSignIn(account)
                                } catch (e: Exception) {
                                    googleSignInError = e.message ?: "Google Sign-In failed"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign in with Google")
                    }
                }

                googleSignInError?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Credits",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "0",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Switch Account",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Sign in with a different Google account.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        googleSignInError = null
                        coroutineScope.launch {
                            try {
                                clearGoogleCredentialState(context)

                                val account = signInWithGoogle(context)

                                if (account != null) {
                                    googleSignInError =
                                        "Google selected: ${account.displayName ?: "No name"} | ID: ${account.uniqueId}"
                                    val connected = connectGoogleToBackend(account.idToken)

                                    if (connected != null) {
                                        context.getSharedPreferences(
                                            "vidforge_profile",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                            .putInt("backend_user_id", connected)
                                            .apply()
                                        onGoogleSignIn(account)
                                        (context as? ComponentActivity)?.recreate()
                                    } else {
                                        googleSignInError =
                                            "Google account could not connect to VidForge server."
                                    }
                                } else {
                                    googleSignInError =
                                        "Google Sign-In was not completed."
                                }
                            } catch (e: Exception) {
                                googleSignInError =
                                    e.message ?: "Google account switch failed"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Switch Account")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Sign out of this Google account on this device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
) {
    data class MoreItem(
        val title: String,
        val subtitle: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val accent: Color
    )

    val moreItems = listOf(
        MoreItem("Video Settings", "Quality, format and video preferences", Icons.Default.VideoSettings, Color(0xFF5B6FE8)),
        MoreItem("Image Settings", "Image quality and visual preferences", Icons.Default.Image, Color(0xFFB05CC7)),
        MoreItem("Audio & Voice", "Voice, sound and audio controls", Icons.Default.Mic, Color(0xFF3F9D91)),
        MoreItem("AI Generation", "Generation engines and AI preferences", Icons.Default.AutoAwesome, Color(0xFF7657C8)),
        MoreItem("Projects", "Organize and manage your projects", Icons.Default.Folder, Color(0xFF4D86C7)),
        MoreItem("Media & Files", "Your media, assets and files", Icons.Default.Folder, Color(0xFFD18A4A)),
        MoreItem("Usage & History", "Activity, generations and usage", Icons.Default.History, Color(0xFF4C9E9A)),
        MoreItem("Credits", "Balance and credit activity", Icons.Default.CreditCard, Color(0xFFC28A3A)),
        MoreItem("Cloud / GPU", "Compute resources and cloud settings", Icons.Default.Cloud, Color(0xFF4E91C9)),
        MoreItem("Export & Sharing", "Export and share your creations", Icons.Default.Share, Color(0xFF4F9B6C)),
        MoreItem("Advanced", "Advanced controls and options", Icons.Default.Tune, Color(0xFF68758A)),
        MoreItem("Owner Mode", "Owner-only controls and management", Icons.Default.AdminPanelSettings, Color(0xFF59627A))
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "More",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Everything you need, in one place",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 4.dp,
                bottom = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            items(moreItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFDFDFF)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFFE9EBF2)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(item.accent.copy(alpha = 0.10f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(23.dp),
                                tint = item.accent
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "›",
                            style = MaterialTheme.typography.headlineSmall,
                            color = item.accent.copy(alpha = 0.75f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectsScreen(
    modifier: Modifier = Modifier,
) {
    data class ProjectItem(
        val title: String,
        val subtitle: String,
        val description: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val accent: Color
    )

    val projectItems = listOf(
        ProjectItem("My Projects", "All your projects", "Browse and manage everything you have created in VidForge.", Icons.Default.Folder, Color(0xFF5B6FE8)),
        ProjectItem("New Project", "Start something new", "Create a fresh workspace for your next video, image or creative idea.", Icons.Default.Add, Color(0xFF4D86C7)),
        ProjectItem("Recent Projects", "Continue where you left off", "Quickly return to projects you recently opened or edited.", Icons.Default.History, Color(0xFF3F9D91)),
        ProjectItem("Draft Projects", "Unfinished work", "Keep incomplete projects organized until they are ready to continue.", Icons.Default.Tune, Color(0xFFD18A4A)),
        ProjectItem("Video Projects", "Your video work", "Manage projects focused on scenes, generation, editing and final video.", Icons.Default.VideoSettings, Color(0xFF7657C8)),
        ProjectItem("Image Projects", "Your visual work", "Manage image-based projects, references and generated visuals.", Icons.Default.Image, Color(0xFFB05CC7)),
        ProjectItem("Project Templates", "Start faster", "Use reusable project structures for common creative workflows.", Icons.Default.Layers, Color(0xFF68758A)),
        ProjectItem("Shared Projects", "Projects shared with others", "Keep shared and collaborative project spaces organized.", Icons.Default.Share, Color(0xFF4F9B6C)),
        ProjectItem("Archived Projects", "Stored away safely", "Keep older projects out of the main workspace without losing them.", Icons.Default.Folder, Color(0xFF59627A)),
        ProjectItem("Project Assets", "Media and resources", "Manage characters, images, audio, references and other project assets.", Icons.Default.Image, Color(0xFFD16B55)),
        ProjectItem("Project Versions", "History and versions", "Keep track of different versions of your project as it evolves.", Icons.Default.History, Color(0xFF4C9E9A)),
        ProjectItem("Project Backup", "Protect your work", "Prepare project data for backup, restore and future migration.", Icons.Default.Cloud, Color(0xFF4E91C9))
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Projects",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Build, organize and manage your creative work",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF5B6FE8).copy(alpha = 0.11f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                    tint = Color(0xFF5B6FE8)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF1F3FF)
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color(0xFFE1E4F5)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(Color(0xFF5B6FE8).copy(alpha = 0.13f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = Color(0xFF5B6FE8)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Project Studio",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Your creative workspace for projects, assets, versions and everything connected to your work.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 30.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(projectItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    shape = RoundedCornerShape(21.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFDFDFF)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        item.accent.copy(alpha = 0.18f)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(item.accent.copy(alpha = 0.11f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = item.accent
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = item.accent
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(item.accent.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "›",
                                style = MaterialTheme.typography.titleLarge,
                                color = item.accent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
) {
    data class CreateItem(
        val title: String,
        val subtitle: String,
        val description: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val accent: Color
    )

    val createItems = listOf(
        CreateItem(
            "Create Video",
            "Build a complete video",
            "Create scenes, add characters, voice, audio and generate the final video.",
            Icons.Default.VideoSettings,
            Color(0xFF5B6FE8)
        ),
        CreateItem(
            "Create Image",
            "Generate visual content",
            "Create images from prompts, references or your saved characters.",
            Icons.Default.Image,
            Color(0xFFB05CC7)
        ),
        CreateItem(
            "Create Scene",
            "Build a single scene",
            "Set the environment, characters, action, camera and visual direction.",
            Icons.Default.AutoAwesome,
            Color(0xFF7657C8)
        ),
        CreateItem(
            "Create Character",
            "Design a new character",
            "Create human, animal, bird, alien, fantasy, robot or creature characters.",
            Icons.Default.Person,
            Color(0xFF4D86C7)
        ),
        CreateItem(
            "Create Voice",
            "Create or select a voice",
            "Prepare narration and character voices for your content.",
            Icons.Default.Mic,
            Color(0xFF3F9D91)
        ),
        CreateItem(
            "Create Audio",
            "Music and sound",
            "Create or prepare music, narration, ambience and sound effects.",
            Icons.Default.Mic,
            Color(0xFF4F9B6C)
        ),
        CreateItem(
            "Create Script",
            "Story and dialogue",
            "Build stories, scenes, narration, dialogue and structured scripts.",
            Icons.Default.AutoAwesome,
            Color(0xFFD18A4A)
        ),
        CreateItem(
            "Create Project",
            "Start a new workspace",
            "Create a complete project and organize everything inside it.",
            Icons.Default.Folder,
            Color(0xFF4E91C9)
        ),
        CreateItem(
            "AI Generate",
            "Generate with AI",
            "Use the available generation engines to create new content.",
            Icons.Default.AutoAwesome,
            Color(0xFF7657C8)
        ),
        CreateItem(
            "Templates",
            "Start faster",
            "Use ready-made creation structures and reusable project templates.",
            Icons.Default.Layers,
            Color(0xFF68758A)
        ),
        CreateItem(
            "Import / Upload",
            "Bring your own media",
            "Add images, videos, audio, references and other project assets.",
            Icons.Default.Add,
            Color(0xFF4D86C7)
        ),
        CreateItem(
            "Remix / Transform",
            "Transform existing content",
            "Remix, convert or creatively transform your existing media.",
            Icons.Default.AutoFixHigh,
            Color(0xFF9A5BC7)
        ),
        CreateItem(
            "Effects & Editing",
            "Polish your creation",
            "Apply visual effects, adjustments, timing and editing controls.",
            Icons.Default.Tune,
            Color(0xFFD16B55)
        ),
        CreateItem(
            "Preview & Render",
            "Review before final output",
            "Preview your work and prepare the final render.",
            Icons.Default.PlayArrow,
            Color(0xFF3F9D91)
        ),
        CreateItem(
            "Export & Share",
            "Deliver your creation",
            "Export finished content and share it wherever you need.",
            Icons.Default.Share,
            Color(0xFF4F9B6C)
        )
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Create",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Everything you need to bring your ideas to life",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF5B6FE8).copy(alpha = 0.11f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                    tint = Color(0xFF5B6FE8)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF1F3FF)
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color(0xFFE1E4F5)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF5B6FE8).copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = Color(0xFF5B6FE8)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Creation Studio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Start with a video, image, scene, character, audio or any other creative workflow.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 30.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(createItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    shape = RoundedCornerShape(21.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFDFDFF)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        item.accent.copy(alpha = 0.18f)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(17.dp))
                                .background(item.accent.copy(alpha = 0.11f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(25.dp),
                                tint = item.accent
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = item.accent
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .size(31.dp)
                                .clip(CircleShape)
                                .background(item.accent.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "›",
                                style = MaterialTheme.typography.titleLarge,
                                color = item.accent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharactersScreen(
    modifier: Modifier = Modifier,
) {
    data class CharacterItem(
        val title: String,
        val subtitle: String,
        val description: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val accent: Color
    )

    val characterItems = listOf(
        CharacterItem(
            "Human Characters",
            "People & human identities",
            "Face, hair, body, clothing, style, personality and expressions",
            Icons.Default.Face,
            Color(0xFF5B6FE8)
        ),
        CharacterItem(
            "Animal Characters",
            "Tigers, lions, wolves & more",
            "Species, fur, body structure, eyes, colors and animal traits",
            Icons.Default.Pets,
            Color(0xFF4F9B6C)
        ),
        CharacterItem(
            "Bird Characters",
            "Eagles, parrots, owls & more",
            "Feathers, wings, beak, colors, species and flight details",
            Icons.Default.Flight,
            Color(0xFF3F8FC7)
        ),
        CharacterItem(
            "Fantasy Creatures",
            "Dragons, unicorns & mythical beings",
            "Wings, scales, horns, magical traits and unique anatomy",
            Icons.Default.AutoFixHigh,
            Color(0xFF9A5BC7)
        ),
        CharacterItem(
            "Aliens & Extraterrestrials",
            "Other-worldly characters",
            "Species, skin, eyes, anatomy and futuristic visual identity",
            Icons.Default.AutoAwesome,
            Color(0xFF4C9E9A)
        ),
        CharacterItem(
            "Robots & Androids",
            "Mechanical & synthetic characters",
            "Body design, armor, materials, lights, screens and robotic traits",
            Icons.Default.SmartToy,
            Color(0xFF68758A)
        ),
        CharacterItem(
            "Monsters & Creatures",
            "Fictional creatures & monsters",
            "Custom anatomy, texture, body features and creature identity",
            Icons.Default.Warning,
            Color(0xFFD16B55)
        ),
        CharacterItem(
            "My Characters",
            "Your saved character collection",
            "View and organize every character created for your projects",
            Icons.Default.Person,
            Color(0xFF7657C8)
        ),
        CharacterItem(
            "Create Character",
            "Build a new character",
            "Create a reusable character with its own visual identity",
            Icons.Default.Create,
            Color(0xFF4D86C7)
        ),
        CharacterItem(
            "Character Profiles",
            "Complete character information",
            "Store appearance, personality, voice, references and identity",
            Icons.Default.Badge,
            Color(0xFFC28A3A)
        ),
        CharacterItem(
            "Character Consistency",
            "Keep characters visually stable",
            "Maintain the same identity and appearance across scenes and videos",
            Icons.Default.Layers,
            Color(0xFF3F9D91)
        ),
        CharacterItem(
            "Character Library",
            "Reusable character collection",
            "Keep approved characters ready to use across future projects",
            Icons.Default.Folder,
            Color(0xFF5B6FE8)
        )
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Characters",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Create, organize and keep every character consistent",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFF7657C8).copy(alpha = 0.11f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF7657C8)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF4F1FF)
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color(0xFFE6DFFF)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(Color(0xFF7657C8).copy(alpha = 0.13f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = Color(0xFF7657C8)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Character Studio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Humans, animals, aliens, fantasy creatures and more — each with its own identity.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 30.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(characterItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    shape = RoundedCornerShape(21.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFDFDFF)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        item.accent.copy(alpha = 0.18f)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(17.dp))
                                .background(item.accent.copy(alpha = 0.11f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(25.dp),
                                tint = item.accent
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = item.accent
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .size(31.dp)
                                .clip(CircleShape)
                                .background(item.accent.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "›",
                                style = MaterialTheme.typography.titleLarge,
                                color = item.accent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(
    title: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This workspace is coming next.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ActiveJobsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var jobs by remember { mutableStateOf(JobStore.loadJobs(context)) }
    var serverJobs by remember { mutableStateOf<List<JobItem>>(emptyList()) }
    var serverJobsLoading by remember { mutableStateOf(true) }
    var serverJobsError by remember { mutableStateOf("") }
    var selectedJob by remember { mutableStateOf<JobItem?>(null) }
    var runningExpanded by remember { mutableStateOf(true) }
    var queuedExpanded by remember { mutableStateOf(true) }
    var failedExpanded by remember { mutableStateOf(true) }
    var completedExpanded by remember { mutableStateOf(true) }
    var showJobManagement by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val backendUserId = context.getSharedPreferences(
                "vidforge_profile", Context.MODE_PRIVATE
            ).getInt("backend_user_id", -1)

            if (backendUserId <= 0) {
                serverJobsError = "VidForge server account is not connected."
            } else {
                val response = getVidForgeJson("/api/jobs?user_id=$backendUserId")
                val array = response.optJSONArray("jobs") ?: org.json.JSONArray()

                serverJobs = buildList {
                    for (i in 0 until array.length()) {
                        val item = array.optJSONObject(i) ?: continue
                        val serverId = item.optInt("id", -1)
                        if (serverId <= 0) continue

                        val rawStatus = item.optString("status", "queued").uppercase()
                        val normalizedStatus = when (rawStatus) {
                            "PENDING", "WAITING", "QUEUED" -> "queued"
                            "GENERATING", "RUNNING", "PROCESSING", "IN_PROGRESS" -> "running"
                            "DONE", "COMPLETED", "SUCCESS", "SUCCEEDED" -> "completed"
                            "ERROR", "FAILED" -> "failed"
                            else -> rawStatus.lowercase()
                        }

                        val prompt = item.optString("prompt").trim()
                        val createdAt = runCatching {
                            java.text.SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss",
                                java.util.Locale.US
                            ).parse(item.optString("created_at"))?.time
                        }.getOrNull() ?: System.currentTimeMillis()

                        add(
                            JobItem(
                                id = "server-$serverId",
                                type = "Video",
                                status = normalizedStatus,
                                title = prompt.ifBlank { "Video Job #$serverId" },
                                projectName = "Project #${item.optInt("project_id")}",
                                createdAt = createdAt,
                                error = if (normalizedStatus == "failed") {
                                    "Server job failed."
                                } else {
                                    ""
                                },
                                isServerJob = true
                            )
                        )
                    }
                }
                serverJobsError = ""
            }
        } catch (e: Exception) {
            serverJobsError =
                "Could not load server jobs: ${e.message ?: e.javaClass.simpleName}"
        } finally {
            serverJobsLoading = false
        }
    }

    val allJobs = jobs + serverJobs
    val runningJobs = allJobs.filter { it.status.equals("running", ignoreCase = true) }
    val queuedJobs = allJobs.filter { it.status.equals("queued", ignoreCase = true) }
    val failedJobs = allJobs.filter { it.status.equals("failed", ignoreCase = true) }
    val recentJobs = allJobs.filter {
        it.status.equals("completed", ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Active Jobs",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Track your creations and processing tasks",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (serverJobsLoading || serverJobsError.isNotBlank()) {
            item {
                Text(
                    text = if (serverJobsLoading) {
                        "Loading server jobs..."
                    } else {
                        serverJobsError
                    },
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().clickable {
                    runningExpanded = !runningExpanded
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Running",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${runningJobs.size}  " + if (runningExpanded) "Hide" else "Show",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (runningExpanded) {
        if (runningJobs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = "No running jobs",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Jobs currently being processed will appear here.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(runningJobs, key = { it.id }) { job ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { selectedJob = job },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = job.title.ifBlank { job.type },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${job.type} • ${job.status}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { (job.progress.coerceIn(0, 100) / 100f) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${job.progress.coerceIn(0, 100)}%",
                            style = MaterialTheme.typography.labelMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            enabled = !job.isServerJob,
                            onClick = {
                                JobStore.updateJob(
                                    context,
                                    job.copy(status = "cancelled")
                                )
                                jobs = JobStore.loadJobs(context)
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }

        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().clickable {
                    queuedExpanded = !queuedExpanded
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Queued",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${queuedJobs.size}  " + if (queuedExpanded) "Hide" else "Show",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (queuedExpanded) {
        if (queuedJobs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = "No queued jobs",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Waiting jobs will appear here with priority and queue information.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(queuedJobs, key = { it.id }) { job ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { selectedJob = job },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = job.title.ifBlank { job.type },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${job.type} • Queued",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            enabled = !job.isServerJob,
                            onClick = {
                                JobStore.updateJob(
                                    context,
                                    job.copy(status = "cancelled")
                                )
                                jobs = JobStore.loadJobs(context)
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }

        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().clickable {
                    failedExpanded = !failedExpanded
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Failed",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${failedJobs.size}  " + if (failedExpanded) "Hide" else "Show",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (failedExpanded) {
        if (failedJobs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = "No failed jobs",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Failed jobs will appear here with retry and delete actions.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(failedJobs, key = { it.id }) { job ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { selectedJob = job },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = job.title.ifBlank { job.type },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = job.error.ifBlank { "Job failed." },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                enabled = !job.isServerJob,
                                onClick = {
                                    JobStore.updateJob(
                                        context,
                                        job.copy(
                                            status = "queued",
                                            progress = 0,
                                            startedAt = 0L,
                                            processingTime = 0L,
                                            error = ""
                                        )
                                    )
                                    jobs = JobStore.loadJobs(context)
                                }
                            ) {
                                Text("Retry")
                            }

                            OutlinedButton(
                                enabled = !job.isServerJob,
                                onClick = {
                                    JobStore.deleteJob(context, job.id)
                                    jobs = JobStore.loadJobs(context)
                                }
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }

        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().clickable {
                    completedExpanded = !completedExpanded
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Completed",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${recentJobs.size}  " + if (completedExpanded) "Hide" else "Show",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (completedExpanded) {
        if (recentJobs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = "No completed jobs",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Completed jobs will appear here.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(recentJobs, key = { it.id }) { job ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { selectedJob = job },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = job.title.ifBlank { job.type },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${job.type} • ${job.status}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        }

        item {
            Text(
                text = "Professional",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().clickable { showJobManagement = true },
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Job Management",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Search • Filter • Sort • Job Details • History",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Advanced controls will be connected to the job system.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    selectedJob?.let { job ->
        AlertDialog(
            onDismissRequest = { selectedJob = null },
            title = {
                Text(job.title.ifBlank { job.type })
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Job ID: ${job.id}")
                    Text("Type: ${job.type}")
                    Text("Status: ${job.status}")
                    Text("Project: ${job.projectName.ifBlank { "—" }}")
                    Text("Progress: ${job.progress.coerceIn(0, 100)}%")
                    Text(
                        "Created: " + java.text.SimpleDateFormat(
                            "yyyy-MM-dd HH:mm",
                            java.util.Locale.US
                        ).format(java.util.Date(job.createdAt))
                    )
                    if (job.error.isNotBlank()) {
                        Text("Details: ${job.error}")
                    }
                    if (job.isServerJob) {
                        Text("Server actions are not connected yet.")
                    }
                }
            },
            confirmButton = {
                Button(onClick = { selectedJob = null }) {
                    Text("Close")
                }
            }
        )
    }


    if (showJobManagement) {
        AlertDialog(
            onDismissRequest = { showJobManagement = false },
            title = { Text("Job Management") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Total jobs: ${allJobs.size}")
                    Text("Running: ${runningJobs.size}")
                    Text("Queued: ${queuedJobs.size}")
                    Text("Failed: ${failedJobs.size}")
                    Text("Completed: ${recentJobs.size}")
                    Text("Tap a job card to view its details.")
                    Text("Search, filtering, sorting, history controls, and server-side actions are not connected yet.")
                }
            },
            confirmButton = {
                Button(onClick = { showJobManagement = false }) {
                    Text("Close")
                }
            }
        )
    }

}



@Composable
fun VoiceDialogueScreen(
    savedVoiceJson: String,
    onSave: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val saved = remember(savedVoiceJson) {
        runCatching { org.json.JSONObject(savedVoiceJson) }
            .getOrElse { org.json.JSONObject() }
    }
    var characterId by remember(savedVoiceJson) {
        mutableStateOf(saved.optString("character_id", ""))
    }
    var sceneNumber by remember(savedVoiceJson) {
        mutableStateOf(saved.optString("scene_number", ""))
    }
    var shotNumber by remember(savedVoiceJson) {
        mutableStateOf(saved.optString("shot_number", ""))
    }
    var dialogueText by remember(savedVoiceJson) {
        mutableStateOf(
            saved.optString(
                "dialogue_text",
                saved.optString("dialogue_scenes", "")
            )
        )
    }
    var voicePreview by remember(savedVoiceJson) {
        mutableStateOf(saved.optString("voice_preview", ""))
    }
    var reviewNotes by remember(savedVoiceJson) {
        mutableStateOf(saved.optString("review_notes", ""))
    }
    var autoDirectorStatus by remember(savedVoiceJson) {
        mutableStateOf(saved.optString("auto_director_status", "Not started"))
    }
    var voiceStatus by remember(savedVoiceJson) {
        mutableStateOf(saved.optString("voice_status", "Engine not connected"))
    }
    var voiceApproved by remember(savedVoiceJson) {
        mutableStateOf(saved.optBoolean("voice_approved", false))
    }
    var savedStatus by remember { mutableStateOf(false) }
    var voiceNotice by remember { mutableStateOf<String?>(null) }

    fun persist() {
        val json = org.json.JSONObject()
            .put("character_id", characterId.trim())
            .put("scene_number", sceneNumber.trim())
            .put("shot_number", shotNumber.trim())
            .put("dialogue_text", dialogueText.trim())
            .put(
                "dialogue_scenes",
                "Scene ${sceneNumber.trim()} / Shot ${shotNumber.trim()}: ${dialogueText.trim()}"
            )
            .put("voice_preview", voicePreview.trim())
            .put("review_notes", reviewNotes.trim())
            .put("auto_director_status", autoDirectorStatus)
            .put("voice_status", voiceStatus)
            .put("voice_approved", voiceApproved)
        onSave(json.toString())
        savedStatus = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Voice & Dialogue",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            "Save the character voice and dialogue for each scene/shot here.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(14.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("1. Character Voice", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = characterId,
                    onValueChange = { characterId = it; savedStatus = false },
                    label = { Text("Character ID (e.g., CHAR-001)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = voicePreview,
                    onValueChange = { voicePreview = it; savedStatus = false },
                    label = { Text("Voice Style / Accent / Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                Text(
                    "Reuse the same Character ID across scenes to keep the character voice consistent.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("2. Dialogue Assignment", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = sceneNumber,
                        onValueChange = { sceneNumber = it; savedStatus = false },
                        label = { Text("Scene Number") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = shotNumber,
                        onValueChange = { shotNumber = it; savedStatus = false },
                        label = { Text("Shot Number") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = dialogueText,
                    onValueChange = {
                        dialogueText = it
                        voiceApproved = false
                        savedStatus = false
                    },
                    label = { Text("Dialogue — Spoken Lines") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("3. Generate Voice & Preview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        voiceNotice = "The voice engine is not connected yet. Connect an engine to generate audio."
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Generate Voice")
                }
                OutlinedButton(
                    onClick = {
                        voiceNotice = "The voice engine is not connected yet. Connect an engine to preview audio."
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Play Voice Preview")
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("4. Review & Approve",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(
                    if (voiceApproved) "Voice Approved" else "Voice has not been approved yet",
                    color = MaterialTheme.colorScheme.primary
                )
                OutlinedTextField(
                    value = reviewNotes,
                    onValueChange = { reviewNotes = it; savedStatus = false },
                    label = { Text("Review Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (voiceStatus != "Generated") {
                            voiceNotice = "Generate and review the audio before approving this voice."
                        } else {
                            voiceApproved = true
                            persist()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Approve Voice")
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("5. Auto Director",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("Status: $autoDirectorStatus")
                Text(
                    "Prepare dialogue, generate character voices, and synchronize audio with video.",
                    style = MaterialTheme.typography.bodySmall
                )
                OutlinedButton(
                    onClick = {
                        autoDirectorStatus = "Configured — awaiting connected AI engine"
                        savedStatus = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Set Up Auto Director")
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        Button(
            onClick = { persist() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (savedStatus) "Saved" else "Save Voice & Dialogue")
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
        Spacer(Modifier.height(20.dp))
    }

    if (voiceNotice != null) {
        AlertDialog(
            onDismissRequest = { voiceNotice = null },
            title = { Text("Voice Engine Status") },
            text = { Text(voiceNotice.orEmpty()) },
            confirmButton = {
                Button(onClick = { voiceNotice = null }) {
                    Text("OK")
                }
            }
        )
    }
}


@Composable
fun MusicSoundScreen(
    savedMusicJson: String,
    onSave: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val saved = remember(savedMusicJson) {
        runCatching { org.json.JSONObject(savedMusicJson) }
            .getOrElse { org.json.JSONObject() }
    }
    var values by remember(savedMusicJson) {
        mutableStateOf(run {
            val map = mutableMapOf<String, String>()
            val keys = saved.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                map[key] = saved.optString(key, "")
            }
            map.toMap()
        })
    }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedItem by remember { mutableStateOf<String?>(null) }
    var notice by remember { mutableStateOf<String?>(null) }

    val categories = listOf(
        "Background Music" to listOf("Music Style", "Mood", "Music Library", "Custom Music"),
        "Sound Effects" to listOf("Action Sounds", "Nature Sounds", "Custom Effects"),
        "Ambient Sounds" to listOf("Indoor", "Outdoor", "Weather"),
        "Audio Timeline" to listOf("Scene Audio", "Shot Audio", "Audio Timing"),
        "Volume & Mixing" to listOf("Music Volume", "Effects Volume", "Dialogue Volume"),
        "Audio Preview & Approval" to listOf("Preview", "Save Audio", "Approve Audio")
    )

    fun updateValue(key: String, value: String) {
        values = values + (key to value)
    }

    fun persist(approved: Boolean = values["audio_approved"] == "true") {
        val data = org.json.JSONObject()
        values.forEach { (key, value) -> data.put(key, value) }
        data.put("audio_approved", approved)
        values = values + ("audio_approved" to approved.toString())
        onSave(data.toString())
    }

    fun persistNextShot() {
        val entries = runCatching {
            org.json.JSONArray(values["saved_shot_entries"] ?: "[]")
        }.getOrElse { org.json.JSONArray() }

        val snapshot = org.json.JSONObject()
        values.filterKeys { it != "saved_shot_entries" }.forEach { (key, value) ->
            snapshot.put(key, value)
        }
        snapshot.put("audio_approved", values["audio_approved"] == "true")
        entries.put(snapshot)

        values = mapOf(
            "saved_shot_entries" to entries.toString(),
            "audio_approved" to "false"
        )
        persist()
        selectedCategory = null
        selectedItem = null
        notice = "Shot saved. Ready for the next shot."
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                if (selectedItem != null) selectedItem = null
                else if (selectedCategory != null) selectedCategory = null
                else onBack()
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                selectedItem ?: selectedCategory ?: "Music & Sound",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = values["scene_number"].orEmpty(),
                onValueChange = { updateValue("scene_number", it) },
                label = { Text("Scene Number") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = values["shot_number"].orEmpty(),
                onValueChange = { updateValue("shot_number", it) },
                label = { Text("Shot Number") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Spacer(Modifier.height(12.dp))

        if (selectedCategory == null) {
            categories.forEach { (category, _) ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        .clickable { selectedCategory = category }
                ) {
                    Text(
                        category,
                        modifier = Modifier.padding(18.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Text(
                if (values["audio_approved"] == "true") "Status: Approved"
                else "Status: Not approved",
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        persist()
                        notice = "Music & Sound settings saved."
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Save") }

                Spacer(Modifier.width(8.dp))

                OutlinedButton(
                    onClick = {
                        persist(true)
                        notice = "Settings approved. Audio generation engine is not connected yet."
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Approve") }
            }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { persistNextShot() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Add Next Shot") }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    updateValue("auto_director_status", "Configured — awaiting connected AI engine")
                    persist()
                    notice = "AI Auto Director is not connected to an audio engine yet."
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("AI Auto Director") }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Back")
            }
        } else if (selectedItem == null) {
            val items = categories.first { it.first == selectedCategory }.second
            items.forEach { item ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        .clickable { selectedItem = item }
                ) {
                    Text(
                        item,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        } else {
            val key = "${selectedCategory}_${selectedItem}".replace(" ", "_").lowercase()
            Text(
                "Settings for $selectedItem",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = values[key].orEmpty(),
                onValueChange = { updateValue(key, it) },
                label = { Text("Settings / Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(Modifier.height(10.dp))
            Text(
                "Audio engine is not connected. This screen saves settings only.",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = {
                    persist()
                    notice = "$selectedItem settings saved."
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save Settings") }
        }
    }

    if (notice != null) {
        AlertDialog(
            onDismissRequest = { notice = null },
            title = { Text("Music & Sound") },
            text = { Text(notice.orEmpty()) },
            confirmButton = {
                Button(onClick = { notice = null }) { Text("OK") }
            }
        )
    }
}
