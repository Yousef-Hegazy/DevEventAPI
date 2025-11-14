package com.example.DevEvent.services.impl

import com.cloudinary.Cloudinary
import com.example.DevEvent.services.ImageService
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageServiceImpl : ImageService {

    @Value("\${cloudinary.cloud-name}")
    private lateinit var cloudName: String

    @Value("\${cloudinary.api-key}")
    private lateinit var apiKey: String

    @Value("\${cloudinary.api-secret}")
    private lateinit var apiSecret: String

    private lateinit var cloudinary: Cloudinary

    @PostConstruct
    fun init() {
        cloudinary = Cloudinary(
            mapOf(
                "cloud_name" to cloudName,
                "api_key" to apiKey,
                "api_secret" to apiSecret
            )
        )
    }

    override fun uploadImage(file: MultipartFile): String {
        val uploadResult = cloudinary.uploader().upload(
            file.bytes,
            mapOf(
                "folder" to "events",
                "resource_type" to "auto"
            )
        )

        return uploadResult["secure_url"] as String
    }

}