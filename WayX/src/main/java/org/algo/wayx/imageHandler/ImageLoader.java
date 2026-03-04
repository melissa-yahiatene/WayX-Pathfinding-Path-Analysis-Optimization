package org.algo.wayx.imageHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Utilitaire pour charger une image et en extraire les intensités en niveaux de gris.
 */
public class ImageLoader {

    /**
     * Charge une image et retourne une matrice d'intensités (0–255).
     *
     * @param filename chemin du fichier image
     * @return matrice des intensités en niveaux de gris
     * @throws Exception si le fichier est introuvable ou illisible
     */
    public static int[][] loadGrayscale(String filename) throws Exception {
        BufferedImage img = ImageIO.read(new File(filename));
        int w = img.getWidth();
        int h = img.getHeight();

        int[][] intensities = new int[h][w];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int rgb = img.getRGB(j, i);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                intensities[i][j] = (r + g + b) / 3;
            }
        }

        return intensities;
    }
}

