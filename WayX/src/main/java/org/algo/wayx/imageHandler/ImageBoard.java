package org.algo.wayx.imageHandler;

import org.algo.wayx.model.util.Graph;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Composant Swing chargé d'afficher une image en niveaux de gris
 * redimensionnée dynamiquement à la taille de la fenêtre, avec un
 * chemin superposé entre deux pixels.
 *
 * <p>
 * L'image est fournie sous forme d'une matrice d'intensités (0–255),
 * où chaque case représente un pixel. Le composant se charge de
 * redessiner l'image à l'échelle de la fenêtre tout en conservant
 * les proportions, grâce à un facteur d'échelle calculé
 * automatiquement.
 * </p>
 *
 * <p>
 * Si un chemin est défini via {@link #setPath(LinkedList)}, celui-ci
 * est dessiné par-dessus l'image sous forme de segments rouges reliant
 * les pixels dans l'ordre fourni. Les indices du chemin doivent être
 * linéarisés selon la formule :
 * </p>
 *
 * <pre>
 * index = y * largeur + x
 * </pre>
 *
 * <p>
 * Le composant utilise {@link Graphics2D} avec interpolation bilinéaire
 * et anticrénelage pour un rendu visuel plus propre lors du
 * redimensionnement.
 * </p>
 */
public class ImageBoard extends JComponent {

    /** Matrice des intensités en niveaux de gris (0–255). */
    private int[][] intensities;

    /** Graphe associé à l'image (utilisé pour les algorithmes de chemin). */
    private Graph graph;

    /** Chemin à afficher, sous forme d'indices linéarisés. */
    private LinkedList<Integer> path;

    /** Largeur et hauteur de l'image en pixels. */
    private int imgW, imgH;

    /**
     * Construit un panneau d'affichage pour une image en niveaux de gris.
     *
     * @param intensities matrice des intensités représentant l'image
     * @param graph       graphe associé reliant les pixels entre eux
     */
    public ImageBoard(int[][] intensities, Graph graph) {
        this.intensities = intensities;
        this.graph = graph;
        this.imgH = intensities.length;
        this.imgW = intensities[0].length;
    }

    /**
     * Définit le chemin à afficher sur l'image et déclenche un
     * rafraîchissement du composant.
     *
     * @param path liste des indices linéarisés du chemin
     */
    public void setPath(LinkedList<Integer> path) {
        this.path = path;
        repaint();
    }

    /**
     * Redessine l'image redimensionnée et, si présent, le chemin superposé.
     *
     * <p>
     * Le redimensionnement conserve les proportions de l'image et la
     * centre dans le composant. Le chemin est dessiné par-dessus en rouge.
     * </p>
     *
     * @param g contexte graphique utilisé pour le rendu
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int panelW = getWidth();
        int panelH = getHeight();

        // Calcul du facteur d’échelle
        double scale = Math.min(
                (double) panelW / imgW,
                (double) panelH / imgH
        );

        int drawW = (int) (imgW * scale);
        int drawH = (int) (imgH * scale);

        // Centrage
        int offsetX = (panelW - drawW) / 2;
        int offsetY = (panelH - drawH) / 2;

        // Dessin de l’image redimensionnée
        for (int i = 0; i < imgH; i++) {
            for (int j = 0; j < imgW; j++) {
                int v = intensities[i][j];
                g2.setColor(new Color(v, v, v));

                int x = offsetX + (int) (j * scale);
                int y = offsetY + (int) (i * scale);

                g2.fillRect(x, y, (int) scale + 1, (int) scale + 1);
            }
        }

        // Dessin du chemin
        if (path != null) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2f));

            for (int k = 1; k < path.size(); k++) {
                int a = path.get(k - 1);
                int b = path.get(k);

                int ax = a % imgW;
                int ay = a / imgW;
                int bx = b % imgW;
                int by = b / imgW;

                int x1 = offsetX + (int) (ax * scale + scale / 2);
                int y1 = offsetY + (int) (ay * scale + scale / 2);
                int x2 = offsetX + (int) (bx * scale + scale / 2);
                int y2 = offsetY + (int) (by * scale + scale / 2);

                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }
}
