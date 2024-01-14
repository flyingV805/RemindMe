package kz.flyingv.remindme.features.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kz.flyingv.remindme.BuildConfig
import kz.flyingv.remindme.features.remindtime.RemindTimeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDialog(
    viewModel: ProfileViewModel = viewModel(),
    hide: () -> Unit?
) {

    val uiState by viewModel.provideState().collectAsStateWithLifecycle()

    val googleAuthLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            viewModel.reduce(ProfileAction.SignIn(credential))
        } catch (e: ApiException) {
            viewModel.reduce(ProfileAction.SignInFailed)
            e.printStackTrace()
        }
    }

    val context = LocalContext.current

    BasicAlertDialog(
        onDismissRequest = { hide() },
        properties = DialogProperties()
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Crossfade(targetState = uiState.authorized, label = "") {
                when(it){
                    true -> Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(uiState.displayName, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .width(86.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape)

                        ){

                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = uiState.avatarUrl,
                                ),
                                contentDescription = "avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                        }

                    }
                    false -> Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text("Sign In", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Sign in to sync reminders across all your devices", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(BuildConfig.FB_AUTH_CLIENT_ID)
                                    .requestEmail()
                                    .build()
                                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                                googleAuthLauncher.launch(googleSignInClient.signInIntent)
                            }
                        ) {
                            Text("Sign In with Google")
                        }
                    }
                }
            }

        }



    }

}

@Composable
fun ProfileView() {

}