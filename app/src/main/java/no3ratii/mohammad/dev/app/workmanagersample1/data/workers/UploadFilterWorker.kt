package no3ratii.mohammad.dev.app.workmanagersample1.data.workers

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.RequiresApi
import androidx.work.WorkerParameters

class UploadFilterWorker(context: Context, parameters: WorkerParameters)
    : BaseFilterWorker(context, parameters) {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun applyFilter(input: Bitmap): Bitmap {
        var rsContext: RenderScript? = null
        try {
            val output = Bitmap.createBitmap(input.width, input.height, input.config)
            // Create a RenderScript context.
            rsContext = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)

            // Creates a RenderScript allocation for the blurred result.
            val inAlloc = Allocation.createFromBitmap(rsContext, input)
            val outAlloc = Allocation.createTyped(rsContext, inAlloc.type)

            // Use the ScriptIntrinsicBlur intrinsic.
            val theIntrinsic = ScriptIntrinsicBlur.create(rsContext, Element.U8_4(rsContext))
            theIntrinsic.setRadius(25f)
            theIntrinsic.setInput(inAlloc)
            theIntrinsic.forEach(outAlloc)

            // Copy to the output input from the allocation.
            outAlloc.copyTo(output)
            return output
        } finally {
            rsContext?.finish()
        }
        return input
    }
}
