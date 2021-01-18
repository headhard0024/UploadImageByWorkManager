
package no3ratii.mohammad.dev.app.workmanagersample1.data.responses

import no3ratii.mohammad.dev.app.workmanagersample1.model.clazz.PostImageResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImgurService {
    @Multipart
    @POST("image")
    fun postImage(@Part image: MultipartBody.Part): Call<PostImageResponse>
}
