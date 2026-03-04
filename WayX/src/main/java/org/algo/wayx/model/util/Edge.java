package org.algo.wayx.model.util;

/**
 * Représente une arête orientée dans un graphe.
 * <p>
 * Une arête relie un sommet source à un sommet destination,
 * et possède un poids (coût) utilisé par les algorithmes de plus court chemin
 * tels que Dijkstra ou A*.
 * </p>
 */
public class Edge {

    /** Numéro du sommet source de l'arête. */
    private int source;

    /** Numéro du sommet destination de l'arête. */
    private int destination;

    /** Poids (coût) associé à cette arête. */
    private double weight;

    /**
     * Construit une nouvelle arête orientée.
     *
     * @param source      indice du sommet source
     * @param destination indice du sommet destination
     * @param weight      poids (coût) de l'arête
     */
    public Edge(int source, int destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    /**
     * Retourne l'indice du sommet destination.
     *
     * @return indice du sommet destination
     */
    public int getDestination() {
        return destination;
    }

    /**
     * Retourne le poids de l'arête.
     *
     * @return poids de l'arête
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Modifie le sommet source.
     *
     * @param source nouvel indice du sommet source
     */
    public void setSource(int source) {
        this.source = source;
    }

    /**
     * Modifie le sommet destination.
     *
     * @param destination nouvel indice du sommet destination
     */
    public void setDestination(int destination) {
        this.destination = destination;
    }

    /**
     * Modifie le poids de l'arête.
     *
     * @param weight nouveau poids de l'arête
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Retourne l'indice du sommet source.
     *
     * @return indice du sommet source
     */
    public int getSource() {
        return source;
    }
}
