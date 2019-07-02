package com.example.photovideoclockframe.utility

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import com.example.photovideoclockframe.presentation.main.MEDIA_TYPE

class MediaPathLoader {

    private val mediaPaths = mutableListOf<Pair<String, MEDIA_TYPE>>()
    private var imageIndex = 0

    fun loadMediaPaths(contentResolver: ContentResolver) {
        val paths = ArrayList<Pair<String, MEDIA_TYPE>>()
        paths.addAll(loadImagesPaths(contentResolver))
        paths.addAll(loadVideoPaths(contentResolver))
        paths.shuffle()
        mediaPaths.addAll(paths)
    }

    fun getNextMediaPath() : Pair<String, MEDIA_TYPE> {
        val result = mediaPaths[imageIndex]
        imageIndex = (imageIndex + 1) % mediaPaths.size
        return result
    }


    private fun loadImagesPaths(contentResolver: ContentResolver): ArrayList<Pair<String, MEDIA_TYPE>> {
        return loadMediaPaths(
            contentResolver,
            arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID),
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MEDIA_TYPE.PHOTO
        )
    }

    private fun loadVideoPaths(contentResolver: ContentResolver): ArrayList<Pair<String, MEDIA_TYPE>> {
        return loadMediaPaths(
            contentResolver,
            arrayOf(MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID),
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            MEDIA_TYPE.VIDEO
        )
    }

    private fun loadMediaPaths(
        contentResolver: ContentResolver,
        columns: Array<String>,
        uri: Uri,
        mediaType: MEDIA_TYPE
    ): ArrayList<Pair<String, MEDIA_TYPE>> {
        val mediaPaths = ArrayList<Pair<String, MEDIA_TYPE>>()
        val cursor = contentResolver.query(
            uri,
            columns,
            null,
            null,
            null
        )
        while (cursor?.moveToNext() == true) {
            mediaPaths.add(Pair(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)), mediaType))
        }
        cursor?.close()
        return mediaPaths
    }
}