package no3ratii.mohammad.dev.app.workmanagersample1.view

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import androidx.work.workDataOf
import com.bumptech.glide.Glide
import com.example.background.ImageOperations
import no3ratii.mohammad.dev.app.workmanagersample1.base.Constants
import no3ratii.mohammad.dev.app.workmanagersample1.base.StockImages
import no3ratii.mohammad.dev.app.workmanagersample1.databinding.ActivityMainBinding
import no3ratii.mohammad.dev.app.workmanagersample1.viewModel.UploadImageViewModel
import java.util.jar.Pack200.Packer.PROGRESS

class MainActivity : AppCompatActivity() {

    private var mViewModel: UploadImageViewModel? = null
    private var view: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view!!.root)
        mViewModel = ViewModelProvider(this).get(UploadImageViewModel(application)::class.java)
        mViewModel!!.setImageUri(StockImages.randomStockImage())

        test1()

        if (mViewModel!!.getImageUri() != null) {
            Glide.with(this).load(mViewModel!!.getImageUri())
                .into(view!!.img)
        }

        view!!.exit.setOnClickListener {
            mViewModel!!.cancel()
        }

        view!!.txt.setOnClickListener {

            val imageOperations = ImageOperations.Builder(applicationContext, mViewModel!!.getImageUri()!!)
                .setApplyFilter(true)
                .setApplyUpload(true)
                .build()

            mViewModel!!.apply(imageOperations)

            view!!.progress.visibility = View.VISIBLE
        }

    }

    private fun test1() {
        mViewModel!!.outputStatus.observe(this, Observer { listOfInfos ->
            if (listOfInfos == null || listOfInfos.isEmpty()) {
                Log.i(Constants.TAG, "B")
                return@Observer
            }
            Log.i(Constants.TAG, "listOfInfos[0] ${listOfInfos[0]}")
            val info = listOfInfos[0]
            val finished = info.state.isFinished
            if (!finished) {
                Log.i(Constants.TAG, "A")
            } else {
                val outputData = info.outputData
                val outputImageUri = outputData.getString(Constants.KEY_IMAGE_URI)
                Log.i(Constants.TAG, "C -> $outputImageUri")
                Log.i(Constants.TAG, "C -> $outputData")
                if (!TextUtils.isEmpty(outputImageUri)) {
                    view!!.txt.text = Uri.parse(outputImageUri).toString()
                    view!!.progress.visibility = View.GONE
                    Log.i("MONO", "onCreate: " + Uri.parse(outputImageUri))
                    Glide.with(this).load(outputImageUri)
                        .into(view!!.imgAfter)
                }
            }
        })
    }
}