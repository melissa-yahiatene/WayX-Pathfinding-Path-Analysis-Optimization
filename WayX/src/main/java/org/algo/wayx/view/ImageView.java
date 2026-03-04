package org.algo.wayx.view;

import org.algo.wayx.WayXApp;
import org.algo.wayx.imageHandler.ImageBoard;
import org.algo.wayx.imageHandler.GraphBuilder;
import org.algo.wayx.imageHandler.ImageLoader;
import org.algo.wayx.model.algorithms.AStar;
import org.algo.wayx.model.algorithms.Dijkstra;
import org.algo.wayx.model.heuristics.EuclideanHeuristic;
import org.algo.wayx.model.heuristics.Heuristic;
import org.algo.wayx.model.util.Graph;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.LinkedList;

/**
 * Vue permettant de charger une image en niveaux de gris, de l'afficher
 * redimensionnée dynamiquement, et d'exécuter des algorithmes de plus
 * court chemin (Dijkstra ou A*) entre deux pixels sélectionnés.
 *
 * <p>
 * Le fonctionnement général est le suivant :
 * <ul>
 *     <li>L'utilisateur choisit une image via un {@link JFileChooser}.</li>
 *     <li>L'image est convertie en matrice d'intensités puis en graphe
 *         pondéré via {@link GraphBuilder}.</li>
 *     <li>Un clic sur l'image définit un pixel de départ, un second clic
 *         définit le pixel d'arrivée.</li>
 *     <li>Selon le mode sélectionné (Dijkstra ou A*), le chemin optimal
 *         est calculé et affiché sur l'image.</li>
 * </ul>
 * </p>
 *
 * <p>
 * L'affichage est assuré par {@link ImageBoard}, qui gère le
 * redimensionnement, le centrage et le dessin du chemin.
 * </p>
 */
public class ImageView {

    /**
     * Affiche la fenêtre du mode image et initialise toute la logique
     * d'interaction (chargement, affichage, sélection des pixels,
     * exécution des algorithmes).
     */
    public void show() {

        // --- Sélection du fichier image ---
        JFileChooser chooser = new JFileChooser("ImportedMaps");
        chooser.setDialogTitle("Choisir une image");

        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            return;

        File file = chooser.getSelectedFile();

        try {
            // Chargement image + graphe
            int[][] intensities = ImageLoader.loadGrayscale(file.getAbsolutePath());
            Graph graph = GraphBuilder.buildGraph(intensities);

            ImageBoard board = new ImageBoard(intensities, graph);

            JFrame window = new JFrame("WayX - Mode Image");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setLayout(new BorderLayout());


            /*-- BOUTON RETOUR AU MENU (GAUCHE) --*/

            JButton btnBack = new JButton("Retour au menu");
            btnBack.addActionListener(e -> {
                window.dispose();
                WayXApp.main(null);
            });

            JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topLeftPanel.add(btnBack);


            /*-- BOUTONS Dijkstra / A* / Reset (CENTRE) --*/

            JButton btnDijkstra = new JButton("Dijkstra");
            JButton btnAStar = new JButton("A*");
            String[] heuristics = {"Aucune", "Euclidienne", "Manhattan"};
            JComboBox<String> heuristicSelector = new JComboBox<>(heuristics);

            JButton btnReset = new JButton("Reset");

            JPanel topCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            topCenterPanel.add(btnDijkstra);
            topCenterPanel.add(btnAStar);
            topCenterPanel.add(new JLabel("Heuristique :"));
            topCenterPanel.add(heuristicSelector);
            topCenterPanel.add(btnReset);


            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(topLeftPanel, BorderLayout.WEST);
            topPanel.add(topCenterPanel, BorderLayout.CENTER);

            window.add(topPanel, BorderLayout.NORTH);
            window.add(board, BorderLayout.CENTER);

            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            window.setVisible(true);


            /*-- LOGIQUE DES BOUTONS --*/

            final int[] start = {-1};
            final boolean[] useAStar = {false};
            final Heuristic[] selectedHeuristic = {new EuclideanHeuristic()};

            heuristicSelector.addActionListener(e -> {
                String choice = (String) heuristicSelector.getSelectedItem();

                switch (choice) {
                    case "Aucune":
                        selectedHeuristic[0] = (a, b, ncols) -> 0; // heuristique nulle
                        break;
                    case "Euclidienne":
                        selectedHeuristic[0] = new EuclideanHeuristic();
                        break;
                    case "Manhattan":
                        selectedHeuristic[0] = (a, b, ncols) -> {
                            int ax = a % ncols;
                            int ay = a / ncols;
                            int bx = b % ncols;
                            int by = b / ncols;
                            return Math.abs(ax - bx) + Math.abs(ay - by);
                        };

                        break;
                }

                System.out.println("Heuristique sélectionnée : " + choice);
            });


            btnReset.addActionListener(e -> {
                board.setPath(null);
                start[0] = -1;
                board.repaint();
            });

            btnDijkstra.addActionListener(e -> {
                useAStar[0] = false;
                System.out.println("Mode Dijkstra activé");
            });

            btnAStar.addActionListener(e -> {
                useAStar[0] = true;
                System.out.println("Mode A* activé");
            });


            /*-- SÉLECTION DES PIXELS AU CLIC --*/

            board.addMouseListener(new java.awt.event.MouseAdapter() {

                /**
                 * Gère la sélection du pixel de départ et du pixel d'arrivée,
                 * puis déclenche l'exécution de l'algorithme choisi.
                 */
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {

                    int panelW = board.getWidth();
                    int panelH = board.getHeight();
                    int imgW = intensities[0].length;
                    int imgH = intensities.length;

                    // Conversion coordonnées écran → coordonnées image
                    double scale = Math.min((double) panelW / imgW, (double) panelH / imgH);
                    int offsetX = (panelW - (int)(imgW * scale)) / 2;
                    int offsetY = (panelH - (int)(imgH * scale)) / 2;

                    int x = (int) ((e.getX() - offsetX) / scale);
                    int y = (int) ((e.getY() - offsetY) / scale);

                    if (x < 0 || x >= imgW || y < 0 || y >= imgH)
                        return;

                    int index = y * imgW + x;

                    // Premier clic = départ
                    if (start[0] == -1) {
                        start[0] = index;
                        System.out.println("Départ sélectionné : " + index);
                        return;
                    }

                    // Deuxième clic = arrivée
                    int end = index;
                    System.out.println("Arrivée sélectionnée : " + end);

                    LinkedList<Integer> path;

                    // Choix de l'algorithme
                    if (useAStar[0]) {
                        path = AStar.compute(graph, start[0], end, imgW, selectedHeuristic[0]);
                        System.out.println("Using A* avec heuristique sélectionnée\n__________________________________________\n");
                    }
                    else {
                        path = Dijkstra.compute(graph, start[0], end);
                        System.out.println("Using Dijkstra\n__________________________________________\n");
                    }

                    board.setPath(path);
                    start[0] = -1;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'image.");
        }
    }
}
