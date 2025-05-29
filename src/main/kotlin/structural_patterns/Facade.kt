package structural_patterns

internal fun facade() {
    println("\nFacade Pattern")

    val audioPlayer = AudioPlayer()
    val videoPlayer = VideoPlayer()
    val facade = Facade(
        audioPlayer = audioPlayer,
        videoPlayer = videoPlayer
    )
    facade.play("file name")
}

internal class AudioPlayer {
    internal fun play(fileName: String) {
        println("AudioPlayer is playing")
    }
}

internal class VideoPlayer {
    internal fun play(fileName: String) {
        println("VideoPlayer is playing")
    }
}

internal class Facade constructor(
    private val audioPlayer: AudioPlayer,
    private val videoPlayer: VideoPlayer
) {

    internal fun play(fileName: String) {
        audioPlayer.play(fileName = fileName)
        videoPlayer.play(fileName = fileName)
    }
}