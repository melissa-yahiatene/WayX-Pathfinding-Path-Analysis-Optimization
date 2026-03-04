package org.algo.wayx.presenter;

import org.algo.wayx.model.algorithms.AStar;
import org.algo.wayx.model.algorithms.Dijkstra;
import org.algo.wayx.model.heuristics.EuclideanHeuristic;
import org.algo.wayx.model.heuristics.Heuristic;
import org.algo.wayx.model.heuristics.ManhattanHeuristic;
import org.algo.wayx.model.heuristics.ZeroHeuristic;
import org.algo.wayx.model.util.*;
import org.algo.wayx.view.Board;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Classe responsable de la coordination entre les algorithmes de recherche de chemin
 * (Dijkstra, A*) et l'affichage graphique du résultat sur le plateau {@link Board}.
 *
 * <p>
 * Le rôle du {@code PathPresenter} est de :
 * </p>
 *
 * <ul>
 *     <li>Exécuter l'algorithme choisi (Dijkstra ou A*)</li>
 *     <li>Récupérer le chemin calculé sous forme de liste de sommets</li>
 *     <li>Transmettre ce chemin au composant graphique {@code Board}</li>
 * </ul>
 *
 * <p>
 * La classe ne contient aucune logique algorithmique : elle délègue entièrement
 * le calcul aux classes {@link Dijkstra} et {@link AStar}.
 * </p>
 */
public class PathPresenter {

    /** Graphe dans lequel les chemins doivent être recherchés. */
    private Graph graph;

    /** Composant graphique chargé d'afficher le chemin trouvé. */
    private Board board;

    /** Nombre de colonnes de la carte (utile pour les heuristiques). */
    private int ncols;

    /**
     * Heuristique actuellement sélectionnée pour l'algorithme A*.
     *
     * Cette instance est utilisée lors du calcul du chemin optimal
     * lorsque le mode A* est activé. Elle peut être modifiée dynamiquement
     * via l'interface (sélecteur d’heuristique).
     */
    private Heuristic currentHeuristic;

    /**
     * Table de correspondance entre le nom affiché dans l'interface
     * (ex. "Aucune", "Euclidienne", "Manhattan") et l'instance
     * concrète de l'heuristique correspondante.
     *
     * Cette map permet de récupérer rapidement l'heuristique choisie
     * par l'utilisateur sans utiliser de switch-case.
     */
    private final Map<String, Heuristic> heuristicMap = new HashMap<>();


    /**
     * Construit un présentateur chargé de lancer les algorithmes de recherche
     * et d'afficher les résultats.
     *
     * @param graph graphe contenant les sommets et arêtes
     * @param board composant graphique affichant la carte et les chemins
     * @param ncols nombre de colonnes de la carte
     */
    public PathPresenter(Graph graph, Board board, int ncols) {
        this.graph = graph;
        this.board = board;
        this.ncols = ncols;

        heuristicMap.put("Aucune", new ZeroHeuristic());
        heuristicMap.put("Euclidienne", new EuclideanHeuristic());
        heuristicMap.put("Manhattan", new ManhattanHeuristic());

        // Heuristique par défaut
        currentHeuristic = heuristicMap.get("Euclidienne");

    }

    /**
     * Exécute l'algorithme de Dijkstra pour trouver le plus court chemin
     * entre deux sommets.
     *
     * @param start index du sommet de départ
     * @param end   index du sommet d'arrivée
     * @return      la liste des sommets constituant le chemin optimal
     */
    public LinkedList<Integer> runDijkstra(int start, int end) {
        Dijkstra.compute(graph, start, end);
        LinkedList<Integer> path = Dijkstra.getPath(graph.getVertexlist().get(end));
        board.addPath(graph, path);
        return path;
    }

    public void setHeuristic(String name) {
        if (heuristicMap.containsKey(name)) {
            currentHeuristic = heuristicMap.get(name);
            System.out.println("Heuristique sélectionnée : " + name);
        } else {
            System.out.println("Heuristique inconnue, utilisation de ZeroHeuristic.");
            currentHeuristic = new ZeroHeuristic();
        }
    }


    /**
     * Exécute l'algorithme A* avec l'heuristique choisie.
     *
     * @param start index du sommet de départ
     * @param end   index du sommet d'arrivée
     * @return      la liste des sommets constituant le chemin optimal
     */
    public LinkedList<Integer> runAStar(int start, int end) {

        AStar.compute(graph, start, end, ncols, currentHeuristic);

        LinkedList<Integer> path = AStar.getPath(graph.getVertexlist().get(end));
        board.addPath(graph, path);
        return path;
    }
}
