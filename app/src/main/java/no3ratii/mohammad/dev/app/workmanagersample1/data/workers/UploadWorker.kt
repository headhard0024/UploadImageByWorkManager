import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import no3ratii.mohammad.dev.app.workmanagersample1.base.Constants
import no3ratii.mohammad.dev.app.workmanagersample1.base.connection.ImgurApi

class UploadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {


    override fun doWork(): Result {

        var imageUriInput = inputData.getString(Constants.KEY_IMAGE_URI)
        if (imageUriInput.isNullOrEmpty()) {
            imageUriInput = "file:///android_asset/images/wrong_way.jpg"
        }

        try {
            val imageUri = Uri.parse(imageUriInput)
            val imgurApi = ImgurApi.instance.value

            // Upload the image to Imgur.
            val response = imgurApi.uploadImage(imageUri).execute()
            // Check to see if the upload succeeded.
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()
                val error = errorBody?.string()
                val message = String.format("Request failed %s (%s)", imageUriInput, error)
                Log.e("MONO", "upload is not Successful -> $message")
                return Result.failure()
            } else {
                val imageResponse = response.body()
                var outputData = workDataOf()
                if (imageResponse != null) {
                    val imgurLink = imageResponse.data!!.link
                    // Set the result of the worker by calling setOutputData().
                    outputData = Data.Builder()
                        .putString(Constants.KEY_IMAGE_URI, imgurLink)
                        .build()
                }
                return Result.success(outputData)
            }
        } catch (e: Exception) {
            val message = String.format("Failed to upload image with URI %s", imageUriInput)
            Log.e("MONO",   "clash in uploadWorker -> $message" )
            Log.e("MONO",   "clash in uploadWorker -> $e" )
            return Result.failure()
        }
    }
}
