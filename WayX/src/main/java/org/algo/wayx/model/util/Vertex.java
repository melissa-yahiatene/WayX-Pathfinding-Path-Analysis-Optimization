package org.algo.wayx.model.util;

import java.util.LinkedList;

/**
 * Représente un sommet (vertex) dans un graphe orienté.
 * <p>
 * Cette classe est utilisée par les algorithmes de plus court chemin
 * (Dijkstra, A*) pour stocker les informations nécessaires au calcul.
 * </p>
 */
public class Vertex {

    /** Coût individuel du sommet */
    private double indivTime;

    /** Distance minimale depuis la source calculée par l'algorithme. */
    private double timeFromSource;

    /** Valeur heuristique utilisée par A* pour estimer la distance au but. */
    private double heuristic;

    /** Sommet précédent dans le chemin optimal. */
    private Vertex prev;

    /** Liste des arêtes sortantes depuis ce sommet. */
    private LinkedList<Edge> adjacencylist;

    /** Identifiant unique du sommet dans le graphe. */
    private int num;

    /**
     * Crée un sommet avec un identifiant donné.
     * <p>
     * Les valeurs {@code indivTime}, {@code timeFromSource} et {@code heuristic}
     * sont initialisées à l'infini ou à -1 selon leur rôle.
     * </p>
     *
     * @param num identifiant du sommet
     */
    public Vertex(int num) {
        this.indivTime = Double.POSITIVE_INFINITY;
        this.timeFromSource = Double.POSITIVE_INFINITY;
        this.heuristic = -1;
        this.prev = null;
        this.adjacencylist = new LinkedList<>();
        this.num = num;
    }

    /** @return le coût individuel du sommet */
    public double getIndivTime() {
        return indivTime;
    }

    /** @return la distance depuis la source */
    public double getTimeFromSource() {
        return timeFromSource;
    }

    /** @return la valeur heuristique utilisée par A* */
    public double getHeuristic() {
        return heuristic;
    }

    /** @return le sommet précédent dans le chemin optimal */
    public Vertex getPrev() {
        return prev;
    }

    /** @return la liste des arêtes sortantes */
    public LinkedList<Edge> getAdjacencylist() {
        return adjacencylist;
    }

    /** @return l'identifiant du sommet */
    public int getNum() {
        return num;
    }

    /** @param indivTime nouveau coût individuel du sommet */
    public void setIndivTime(double indivTime) {
        this.indivTime = indivTime;
    }

    /** @param timeFromSource nouvelle distance depuis la source */
    public void setTimeFromSource(double timeFromSource) {
        this.timeFromSource = timeFromSource;
    }

    /** @param heuristic nouvelle valeur heuristique */
    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    /** @param prev nouveau sommet précédent */
    public void setPrev(Vertex prev) {
        this.prev = prev;
    }

    /** @param adjacencylist nouvelle liste d'arêtes sortantes */
    public void setAdjacencylist(LinkedList<Edge> adjacencylist) {
        this.adjacencylist = adjacencylist;
    }

    /** @param num nouvel identifiant du sommet */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * Ajoute une arête sortante à ce sommet.
     *
     * @param e arête à ajouter
     */
    public void addNeighbor(Edge e) {
        this.adjacencylist.addFirst(e);
    }
}
