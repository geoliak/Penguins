/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 *
 * @author rozandq
 */
public class Music {
    public Music() throws URISyntaxException{
        String uriString = getClass().getResource("/home/r/rozandq/NetBeansProjects/Penguins/ressources/audio/Plongee-Nocturne.mp3").toURI().toString();
        MediaPlayer player = new MediaPlayer( new Media(uriString));
        player.play();
    }
}
