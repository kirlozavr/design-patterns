package structural_patterns

internal fun facade() {
    println("\nFacade Pattern")

    val audioPlayer = AudioPlayer()
    val videoPlayer = VideoPlayer()
    val mediaHosting = SomeMediaHosting(
        audioPlayer = audioPlayer,
        videoPlayer = videoPlayer
    )
    mediaHosting.watchVideo("The best video")
}

internal interface Player {
    fun play(name: String)
}

internal class AudioPlayer: Player {
    override fun play(name: String) {
        println("AudioPlayer is playing the \"$name\"")
    }
}

internal class VideoPlayer: Player {
    override fun play(name: String) {
        println("VideoPlayer is playing the \"$name\"")
    }
}

internal interface VideoHosting {
    fun watchVideo(name: String)
}

internal class SomeMediaHosting constructor(
    private val audioPlayer: AudioPlayer,
    private val videoPlayer: VideoPlayer
): VideoHosting {

    override fun watchVideo(name: String) {
        audioPlayer.play(name = name)
        videoPlayer.play(name = name)
    }
}