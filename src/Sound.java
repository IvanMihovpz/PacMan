package PacMan.src;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

public class Sound {
   Clip clip;
   URL soundURL []=new URL[30];


   public Sound() {
       soundURL[0]=getClass().getResource("PacMan\\sounds\\pacman_beginning.wav");
   }

   public void setFile(int i) {

       try {
           AudioInputStream aos=AudioSystem.getAudioInputStream(soundURL[i]);
           clip=AudioSystem.getClip();
           clip.open(aos);
       }
       catch (Exception e) {

       }

   }

   public void play() {

       clip.start();

   }

   public void loop() {

       clip.loop(Clip.LOOP_CONTINUOUSLY);

   }
   public void stop() {

       clip.stop();

   }


}
