package areolsen.sound;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/** Static sound player class. All sounds are lazy loaded, cached, and played upon request. */
public final class SoundPlayer {
  private static final Map<String, Clip> sounds = new HashMap<>();

  private SoundPlayer() {}

  /**
   * Plays a .wav sound file.
   *
   * @param soundFile Name of soundfile stored in resources folder.
   */
  public static void playSound(String soundFile) {
    try {
      Clip clip = sounds.get(soundFile);

      if (clip == null) {
        clip = AudioSystem.getClip();
        clip.open(
            AudioSystem.getAudioInputStream(new File("src/main/resources/sounds/" + soundFile)));
        sounds.put(soundFile, clip);
      }

      clip.setFramePosition(0);
      clip.start();
      clip.drain();
    } catch (Exception e) {
      System.out.println("Error playing:" + soundFile + e);
    }
  }
}
