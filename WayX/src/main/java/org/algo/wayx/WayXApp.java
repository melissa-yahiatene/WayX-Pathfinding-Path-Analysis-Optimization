package org.algo.wayx;

import org.algo.wayx.view.GraphView;
import org.algo.wayx.view.ImageView;

import javax.swing.*;
import java.awt.*;

/**
 * Point d'entrée principal de l'application WayX.
 *
 * <p>
 * Cette classe affiche le menu principal permettant à l'utilisateur
 * de choisir entre les deux modes de fonctionnement :
 * </p>
 *
 * <ul>
 *     <li><strong>Mode Graphe</strong> — visualisation et exécution
 *         d'algorithmes de plus court chemin sur une carte discrète.</li>
 *     <li><strong>Mode Image</strong> — calcul de chemins sur une image
 *         en niveaux de gris convertie en graphe pondéré.</li>
 * </ul>
 *
 * <p>
 * Le menu est affiché en plein écran et propose deux boutons centrés
 * verticalement. Chaque bouton ouvre la vue correspondante et ferme
 * le menu principal.
 * </p>
 */
public class WayXApp {

    /**
     * Méthode principale lançant l'application et affichant le menu.
     *
     * @param args arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {

        JFrame menu = new JFrame("WayX - Menu principal");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Mise en page verticale centrée
        menu.setLayout(new BoxLayout(menu.getContentPane(), BoxLayout.Y_AXIS));

        JButton btnGraph = new JButton("Mode Graphe");
        JButton btnImage = new JButton("Mode Image");

        // Taille maximale pour centrer et étirer verticalement
        Dimension btnSize = new Dimension(300, 3000);
        btnGraph.setMaximumSize(btnSize);
        btnImage.setMaximumSize(btnSize);

        btnGraph.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Placement vertical
        menu.add(Box.createVerticalGlue());
        menu.add(btnGraph);
        menu.add(Box.createRigidArea(new Dimension(0, 20)));
        menu.add(btnImage);
        menu.add(Box.createVerticalGlue());

        // Actions des boutons
        btnGraph.addActionListener(e -> {
            menu.dispose();
            new GraphView().show();
        });

        btnImage.addActionListener(e -> {
            menu.dispose();
            new ImageView().show();
        });

        menu.setVisible(true);
    }
}
