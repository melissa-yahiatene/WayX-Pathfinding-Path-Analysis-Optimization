package org.algo.wayx.model.algorithms;

import org.algo.wayx.model.util.*;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Implémentation de l'algorithme de Dijkstra pour la recherche du plus court chemin dans un graphe pondéré.
 *
 * <p>
 * Dijkstra calcule la distance minimale depuis un sommet source vers tous les autres sommets
 * en utilisant une approche gloutonne : à chaque étape, il sélectionne le sommet non traité
 * ayant la plus petite distance temporaire.
 * </p>
 *
 * <p>
 * L'algorithme s'arrête dès que le sommet d'arrivée est extrait de la file de priorité.
 * Le chemin optimal est ensuite reconstruit en suivant les pointeurs {@code prev} des sommets.
 * </p>
 */
public class Dijkstra {

    /**
     * Exécute l'algorithme de Dijkstra sur un graphe pondéré.
     *
     * @param graph le graphe contenant les sommets et arêtes
     * @param start identifiant du sommet de départ
     * @param end identifiant du sommet d'arrivée
     *
     * @return une liste chaînée contenant les identifiants des sommets du chemin optimal
     */
    public static LinkedList<Integer> compute(Graph graph, int start, int end) {

        int number_tries = 0;

        // Initialisation des sommets
        for (Vertex v : graph.getVertexlist()) {
            v.setTimeFromSource(Double.POSITIVE_INFINITY);
            v.setPrev(null);
        }

        Vertex source = graph.getVertexlist().get(start);
        source.setTimeFromSource(0);

        // File de priorité triée selon la distance g(n)
        PriorityQueue<Vertex> pq = new PriorityQueue<>(
                Comparator.comparingDouble(Vertex::getTimeFromSource)
        );
        pq.add(source);

        // Boucle principale de l'algorithme
        while (!pq.isEmpty()) {
            Vertex u = pq.poll();
            number_tries++;

            if (u.getNum() == end) break; // Arrivée atteinte

            for (Edge e : u.getAdjacencylist()) {
                Vertex v = graph.getVertexlist().get(e.getDestination());
                double newDist = u.getTimeFromSource() + e.getWeight();

                if (newDist < v.getTimeFromSource()) {
                    v.setTimeFromSource(newDist);
                    v.setPrev(u);
                    pq.remove(v);
                    pq.add(v);
                }
            }
        }

        // Affichage des statistiques
        System.out.println("Done! Using Dijkstra:");
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
     * Reconstruit le chemin optimal en remontant les pointeurs {@code prev} depuis un sommet cible.
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
