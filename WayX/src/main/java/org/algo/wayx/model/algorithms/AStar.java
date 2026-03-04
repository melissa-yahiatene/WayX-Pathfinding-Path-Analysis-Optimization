package org.algo.wayx.model.algorithms;

import org.algo.wayx.model.heuristics.Heuristic;
import org.algo.wayx.model.util.*;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Implémentation de l'algorithme A* pour la recherche du plus court chemin dans un graphe pondéré.
 *
 * <p>
 * A* est une extension de l'algorithme de Dijkstra : au lieu de sélectionner le sommet
 * ayant la plus petite distance temporaire, A* sélectionne celui minimisant :
 * <pre>
 *     f(n) = g(n) + h(n)
 * </pre>
 * où :
 * <ul>
 *     <li><b>g(n)</b> est la distance réelle depuis la source jusqu'au sommet n</li>
 *     <li><b>h(n)</b> est une heuristique estimant la distance restante jusqu'à la destination</li>
 * </ul>
 * </p>
 *
 * <p>
 * L'algorithme s'arrête dès que le sommet d'arrivée est extrait de la file de priorité.
 * Le chemin final est reconstruit en suivant les pointeurs {@code prev} des sommets.
 * </p>
 */
public class AStar {

    /**
     * Exécute l'algorithme A* sur un graphe pondéré.
     *
     * @param graph le graphe contenant les sommets et arêtes
     * @param start identifiant du sommet de départ
     * @param end identifiant du sommet d'arrivée
     * @param ncols nombre de colonnes de la carte (utile pour l'heuristique)
     * @param h heuristique utilisée pour estimer la distance au but
     *
     * @return une liste chaînée contenant les identifiants des sommets du chemin optimal
     */
    public static LinkedList<Integer> compute(Graph graph, int start, int end, int ncols, Heuristic h) {

        int number_tries = 0;

        // Initialisation des sommets
        for (Vertex v : graph.getVertexlist()) {
            v.setTimeFromSource(Double.POSITIVE_INFINITY);
            v.setPrev(null);
            v.setHeuristic(h.estimate(v.getNum(), end, ncols));
        }

        Vertex source = graph.getVertexlist().get(start);
        source.setTimeFromSource(0);

        // File de priorité triée selon f(n) = g(n) + h(n)
        PriorityQueue<Vertex> openSet = new PriorityQueue<>(
                Comparator.comparingDouble(v -> v.getTimeFromSource() + v.getHeuristic())
        );
        openSet.add(source);

        // Boucle principale de l'algorithme
        while (!openSet.isEmpty()) {
            Vertex u = openSet.poll();
            number_tries++;

            if (u.getNum() == end) break; // Arrivée atteinte

            for (Edge e : u.getAdjacencylist()) {
                Vertex v = graph.getVertexlist().get(e.getDestination());
                double tentativeG = u.getTimeFromSource() + e.getWeight();

                if (tentativeG < v.getTimeFromSource()) {
                    v.setTimeFromSource(tentativeG);
                    v.setPrev(u);
                    openSet.remove(v);
                    openSet.add(v);
                }
            }
        }

        // Affichage des statistiques
        System.out.println("Done! Using A*:");
        System.out.println("\tNumber of nodes explored: " + number_tries);
        System.out.println("\tTotal time of the path: " + graph.getVertexlist().get(end).getTimeFromSource());

        // Reconstruction du chemin optimal
        LinkedList<Integer> path = new LinkedList<>();
        Vertex cur = graph.getVertexlist().get(end);
        while (cur != null) {
            path.addFirst(cur.getNum());
            cur = cur.getPrev();
        }

        return path;
    }

    /**
     * Reconstruit le chemin optimal en remontant les pointeurs {@code prev}
     * depuis un sommet cible.
     *
     * @param target le sommet d'arrivée
     * @return la liste des sommets formant le chemin optimal
     */
    public static LinkedList<Integer> getPath(Vertex target) {
        LinkedList<Integer> path = new LinkedList<>();
        Vertex cur = target;
        while (cur != null) {
            path.addFirst(cur.getNum());
            cur = cur.getPrev();
        }
        return path;
    }
}
