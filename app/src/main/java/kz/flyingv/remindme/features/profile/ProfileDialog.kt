package kz.flyingv.remindme.features.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDialog(
    viewModel: ProfileViewModel = viewModel(),
    hide: () -> Unit?
) {

    val uiState = viewModel.provideState().collectAsStateWithLifecycle()

    val googleAuthLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            //viewModel.signInGoogleAuth(credential)
        } catch (e: ApiException) {
            //viewModel.signInFailed(e)
            e.printStackTrace()
        }
    }

    BasicAlertDialog(
        onDismissRequest = { hide() },
        properties = DialogProperties()
    ) {


    }




}