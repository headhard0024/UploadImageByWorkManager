
package no3ratii.mohammad.dev.app.workmanagersample1.model.clazz

import com.google.gson.annotations.SerializedName

class PostImageResponse {

    @SerializedName("data")
    val data: UploadedImage? = null

    @SerializedName("success")
    val isSuccess: Boolean = false

    @SerializedName("status")
    val status: Int = 0

    class UploadedImage {
        @SerializedName("id")
        val id: String? = null

        @SerializedName("link")
        val link: String? = null
    }

}
