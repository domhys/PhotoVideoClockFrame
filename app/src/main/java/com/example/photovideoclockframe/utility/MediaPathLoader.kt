package com.example.photovideoclockframe.utility

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import com.example.photovideoclockframe.presentation.main.MEDIA_TYPE

class MediaPathLoader {

    fun loadMediaPaths(contentResolver: ContentResolver): ArrayList<Pair<String, MEDIA_TYPE>> {
        val mediaPaths = ArrayList<Pair<String, MEDIA_TYPE>>()
        mediaPaths.addAll(loadImagesPaths(contentResolver))
//        mediaPaths.addAll(loadVideoPaths(contentResolver))
        mediaPaths.shuffle()
        return mediaPaths
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
            mediaPaths.add(Pair(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)),mediaType))
        }
        cursor?.close()
        return mediaPaths
    }
}