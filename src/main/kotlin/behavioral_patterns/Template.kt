package behavioral_patterns

internal fun template() {
    println("\nTemplate Pattern")

    val profileImageSaver = ProfileImageSaver()
    val profileImage = Image(photoPath = "Profile photo path", description = "Profile Image")
    profileImageSaver.saveImage(profileImage)

    val messageImageSaver = MessageImageSaver()
    val messageImage = Image(photoPath = "Message photo path", description = "Message Image")
    messageImageSaver.saveImage(messageImage)
}

internal data class Image(
    val photoPath: String,
    val description: String
)

internal abstract class ImageSaver {
    internal fun saveImage(image: Image) {
        val uploadLink = getUploadLink()
        uploadImage(localPath = image.photoPath, uploadLink = uploadLink)
        saveImageInfo(localPath = image.photoPath, description = image.description)
    }

    abstract fun getUploadLink(): String

    abstract fun uploadImage(localPath: String, uploadLink: String)

    abstract fun saveImageInfo(localPath: String, description: String)
}

internal class ProfileImageSaver: ImageSaver() {
    override fun getUploadLink(): String {
        return "profile upload link"
    }

    override fun uploadImage(localPath: String, uploadLink: String) {
        println("Profile Image with localPath=$localPath and uploadLink=$uploadLink have been successfully uploaded")
    }

    override fun saveImageInfo(localPath: String, description: String) {
        println("Profile Image Info with localPath=$localPath and description=$description have been successfully uploaded")
    }
}

internal class MessageImageSaver: ImageSaver() {
    override fun getUploadLink(): String {
        return "message upload link"
    }

    override fun uploadImage(localPath: String, uploadLink: String) {
        println("Message Image with localPath=$localPath and uploadLink=$uploadLink have been successfully uploaded")
    }

    override fun saveImageInfo(localPath: String, description: String) {
        println("Message Image Info with localPath=$localPath and description=$description have been successfully uploaded")
    }
}