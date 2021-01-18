/*
 *
 *  * Copyright (C) 2018 The Android Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.example.background

import UploadWorker
import android.content.Context
import android.net.Uri
import androidx.work.*
import com.example.background.workers.CleanupWorker
import no3ratii.mohammad.dev.app.workmanagersample1.base.Constants
import no3ratii.mohammad.dev.app.workmanagersample1.data.workers.UploadFilterWorker

/**
 * Builds and holds WorkContinuation based on supplied filters.
 */
internal class ImageOperations private constructor(val continuation: WorkContinuation) {

    internal class Builder(private val mContext: Context, private val mImageUri: Uri) {
        private var mApplyBlur: Boolean = false
        private var mApplyUpload: Boolean = false

        fun setApplyFilter(applyBlur: Boolean): Builder {
            mApplyBlur = applyBlur
            return this
        }

        fun setApplyUpload(applyUpload: Boolean): Builder {
            mApplyUpload = applyUpload
            return this
        }

        fun build(): ImageOperations {
            var hasInputData = false
            var continuation = WorkManager.getInstance(mContext)
                .beginUniqueWork(
                    Constants.IMAGE_MANIPULATION_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    OneTimeWorkRequest.from(CleanupWorker::class.java)
                )

            if (mApplyBlur) {
                val filterBuilder = OneTimeWorkRequestBuilder<UploadFilterWorker>()
                if (!hasInputData) {
                    filterBuilder.setInputData(createInputData())
                }
                val blur = filterBuilder.build()
                continuation = continuation.then(blur)
            }
            if (mApplyUpload) {
                val upload = OneTimeWorkRequestBuilder<UploadWorker>()
                    .setInputData(createInputData())
                    .addTag(Constants.TAG_OUTPUT)
                    .build()
                continuation = continuation.then(upload)
            }
            return ImageOperations(continuation)
        }

        private fun createInputData(): Data {
            return workDataOf(Constants.KEY_IMAGE_URI to mImageUri.toString())
        }
    }
}
