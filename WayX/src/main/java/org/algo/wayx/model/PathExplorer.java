package org.algo.wayx.model;

import org.algo.wayx.model.util.Graph;

import java.util.LinkedList;

/**
 * Classe abstraite représentant un explorateur de chemin dans un graphe pondéré.
 *
 * <p>
 * Cette classe sert de base commune pour les différents algorithmes de recherche
 * de chemin (ex. : Dijkstra, A*). Elle stocke les informations
 * essentielles nécessaires à la recherche :
 * </p>
 *
 * <ul>
 *     <li>Le graphe dans lequel effectuer la recherche</li>
 *     <li>Le sommet de départ</li>
 *     <li>Le sommet d'arrivée</li>
 * </ul>
 *
 * <p>
 * Les classes dérivées doivent implémenter la méthode {@link #findPath()},
 * qui exécute l'algorithme de recherche et retourne le chemin optimal sous
 * forme d'une liste chaînée d'identifiants de sommets.
 * </p>
 */
public abstract class PathExplorer {

    /** Graphe dans lequel le chemin doit être recherché. */
    protected Graph graph;

    /** Index du sommet de départ. */
    protected int start;

    /** Index du sommet d'arrivée. */
    protected int goal;

    /**
     * Constructeur commun aux différents explorateurs de chemin.
     *
     * @param graph graphe contenant les sommets et arêtes
     * @param start index du sommet de départ
     * @param goal  index du sommet d'arrivée
     */
    public PathExplorer(Graph graph, int start, int goal) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
    }

    /**
     * Exécute l'algorithme de recherche de chemin et retourne la liste
     * des sommets constituant le chemin optimal.
     *
     * @return une {@link LinkedList} contenant les indices des sommets du chemin
     */
    public abstract LinkedList<Integer> findPath();
}


