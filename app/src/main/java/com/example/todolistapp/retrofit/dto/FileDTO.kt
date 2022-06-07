package com.example.todolistapp.retrofit.dto

import com.google.gson.annotations.SerializedName
import java.io.File

class FileDTO(

    @SerializedName("profile_pic")
    val profilePic:File

) {
}