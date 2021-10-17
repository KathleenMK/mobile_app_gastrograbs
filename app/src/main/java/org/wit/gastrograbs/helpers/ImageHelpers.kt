package org.wit.gastrograbs.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import org.wit.gastrograbs.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_grab_image.toString())
    intentLauncher.launch(chooseFile)
}