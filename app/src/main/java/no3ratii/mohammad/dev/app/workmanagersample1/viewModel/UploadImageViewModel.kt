package no3ratii.mohammad.dev.app.workmanagersample1.viewModel

import android.app.Application
import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.background.ImageOperations
import no3ratii.mohammad.dev.app.workmanagersample1.base.Constants

class UploadImageViewModel(application: Application) : AndroidViewModel(application) {

    private var mWorkManager: WorkManager? = null
    private var mImageUri: Uri? = null

    internal val outputStatus: LiveData<List<WorkInfo>>
        get() = mWorkManager!!.getWorkInfosByTagLiveData(Constants.TAG_OUTPUT)

    fun setImageUri(uri: String) {
        mImageUri = uriOrNull(uri)
    }

    fun getImageUri(): Uri? {
        return mImageUri
    }

    internal fun apply(imageOperations: ImageOperations) {
        imageOperations.continuation.enqueue()
    }

    internal fun cancel() {
        mWorkManager?.cancelUniqueWork(Constants.IMAGE_MANIPULATION_WORK_NAME)
    }

    private fun uriOrNull(uriString: String): Uri? {
        return if (!TextUtils.isEmpty(uriString)) {
            Uri.parse(uriString)
        } else null
    }

    init {
        mWorkManager = WorkManager.getInstance(application)
    }
}
