package com.example.DevEvent.services

import org.springframework.web.multipart.MultipartFile

interface ImageService {
    fun uploadImage(file: MultipartFile): String
}