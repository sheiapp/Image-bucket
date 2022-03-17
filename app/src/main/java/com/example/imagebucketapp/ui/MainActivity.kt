package com.example.imagebucketapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.imagebucketapp.R
import com.example.imagebucketapp.databinding.ActivityMainBinding
import com.example.imagebucketapp.di.collectLatestLifecycleFlow
import com.example.imagebucketapp.di.showSnackBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("DEPRECATION")
@SuppressLint("MissingSuperCall")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        collectSearchResult()
        collectMessageToUser()
        binding.selectImg.setOnClickListener { openMediaStore() }
        binding.grantPermissionButton.setOnClickListener { openMediaStore() }
        binding.searchButton.setOnClickListener { retrieveUploadedImageByName() }
    }

    private fun collectMessageToUser() {
        collectLatestLifecycleFlow(viewModel.messageToUser) {
            binding.root.showSnackBar(it, Snackbar.LENGTH_LONG)
        }
    }

    private fun collectSearchResult() {
        collectLatestLifecycleFlow(viewModel.searchImageResult) { bitmap ->
            bitmap?.let {
                binding.imgView.setImageBitmap(it)
            }
        }
    }

    private fun retrieveUploadedImageByName() {
        viewModel.getPersistedImageWithFileName(binding.searchImg.text.toString())
    }

    private fun openMediaStore() {
        if (haveStoragePermission()) {
            showImages()
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(
                this, permissions,
                READ_EXTERNAL_STORAGE_REQUEST
            )
        }
    }

    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED


    private fun goToSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        ).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showImages()
                } else {
                    val showRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    if (showRationale) {
                        showNoAccess()
                    } else {
                        goToSettings()
                    }
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                viewModel.persistImageIntoDatabase(data?.data)
            }
        }
    }

    private fun showImages() {
        imageChooser()
        binding.welcomeView.visibility = View.VISIBLE
        binding.permissionRationaleView.visibility = View.GONE
    }

    private fun showNoAccess() {
        binding.welcomeView.visibility = View.GONE
        binding.permissionRationaleView.visibility = View.VISIBLE
    }

    private fun imageChooser() {
        val i = Intent()
        i.type = IMAGE_TYPE
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, TITLE_FOR_INTENT), SELECT_PICTURE)
    }

    companion object {
        private const val READ_EXTERNAL_STORAGE_REQUEST = 0x1045
        private const val SELECT_PICTURE = 200
        private const val TITLE_FOR_INTENT = "Select Picture"
        private const val IMAGE_TYPE = "image/*"
    }
}