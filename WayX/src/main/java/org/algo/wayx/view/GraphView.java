package org.algo.wayx.view;

import org.algo.wayx.WayXApp;
import org.algo.wayx.model.MapManager;
import org.algo.wayx.presenter.PathPresenter;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Vue permettant de charger un fichier de graphe, d'afficher la carte associée
 * et d'exécuter des algorithmes de plus court chemin (Dijkstra ou A*) entre
 * deux sommets prédéfinis.
 *
 * <p>
 * Le fonctionnement général est le suivant :
 * <ul>
 *     <li>L'utilisateur choisit un fichier de carte via un {@link JFileChooser}.</li>
 *     <li>Le fichier est chargé par {@link MapManager}, qui reconstruit :
 *         <ul>
 *             <li>le graphe pondéré,</li>
 *             <li>les dimensions de la carte,</li>
 *             <li>les couleurs du terrain,</li>
 *             <li>les sommets de départ et d'arrivée.</li>
 *         </ul>
 *     </li>
 *     <li>La carte est affichée dans un {@link Board}, qui gère le rendu graphique.</li>
 *     <li>Les boutons permettent d'exécuter Dijkstra, A*, réinitialiser l'affichage
 *         ou afficher/masquer les explorations.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Cette vue constitue l'interface principale du mode "Graphe" de WayX.
 * Elle permet de visualiser les algorithmes de pathfinding sur une carte
 * discrète, typiquement issue d'un fichier .map ou similaire.
 * </p>
 */
public class GraphView {

    /**
     * Affiche la fenêtre du mode graphe et initialise l'ensemble des composants
     * graphiques et des interactions utilisateur.
     */
    public void show() {

        // --- Sélection du fichier graphe ---
        JFileChooser chooser = new JFileChooser("ImportedMaps");
        chooser.setDialogTitle("Choisir un fichier de graphe");

        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            return;

        File file = chooser.getSelectedFile();

        // Chargement des données de la carte
        MapManager.MapData mapData = MapManager.loadMap(file.getAbsolutePath());

        // --- Création du Board (zone d'affichage de la carte) ---
        Board board = new Board(
                mapData.graph,
                10,
                mapData.ncols,
                mapData.nlines,
                mapData.groundColor,
                mapData.startV,
                mapData.endV
        );

        // Présentateur gérant l'exécution des algorithmes
        PathPresenter presenter = new PathPresenter(mapData.graph, board, mapData.ncols);

        // --- Fenêtre principale ---
        JFrame window = new JFrame("WayX - Mode Graphe");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());


        /*-- BOUTON RETOUR AU MENU (ALIGNÉ À GAUCHE) --*/
        JButton btnBack = new JButton("Retour au menu");
        btnBack.addActionListener(e -> {
            window.dispose();
            WayXApp.main(null);
        });

        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.add(btnBack);


        /*-- BOUTONS DE CONTRÔLE (ALIGNÉS AU CENTRE) --*/

        JButton btnDijkstra = new JButton("Dijkstra");
        JButton btnAStar = new JButton("A*");
        JButton btnReset = new JButton("Reset");
        JButton btnToggleExploration = new JButton("Afficher/Cacher explorations");

        String[] heuristics = {"Aucune", "Euclidienne", "Manhattan"};
        JComboBox<String> heuristicSelector = new JComboBox<>(heuristics);


        JPanel topCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topCenterPanel.add(btnDijkstra);
        topCenterPanel.add(btnAStar);
        topCenterPanel.add(new JLabel("Heuristique :"));
        topCenterPanel.add(heuristicSelector);
        topCenterPanel.add(btnReset);
        topCenterPanel.add(btnToggleExploration);




        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topCenterPanel, BorderLayout.CENTER);


        window.add(topPanel, BorderLayout.NORTH);
        window.add(board, BorderLayout.CENTER);


        btnDijkstra.addActionListener(e -> presenter.runDijkstra(mapData.startV, mapData.endV));
        heuristicSelector.addActionListener(e -> {
            String selected = (String) heuristicSelector.getSelectedItem();
            presenter.setHeuristic(selected);
        });

        btnAStar.addActionListener(e -> presenter.runAStar(mapData.startV, mapData.endV));
        btnReset.addActionListener(e -> board.reset());
        btnToggleExploration.addActionListener(e -> board.setShowExploration(!board.isShowExploration()));

        //AFFICHAGE PLEIN ÉCRAN
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
    }
}
