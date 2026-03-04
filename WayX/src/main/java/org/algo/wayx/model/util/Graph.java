package org.algo.wayx.model.util;

import java.util.ArrayList;

/**
 * Représente un graphe orienté composé de sommets ({@link Vertex}) et d'arêtes ({@link Edge}).
 * <p>
 * Le graphe est stocké sous forme de liste d'adjacence :
 * chaque sommet contient une liste d'arêtes sortantes.
 * </p>
 *
 * <h2>Fonctionnalités principales :</h2>
 * <ul>
 *     <li>Ajout de sommets avec un coût individuel</li>
 *     <li>Ajout d'arêtes orientées entre deux sommets</li>
 *     <li>Accès à la liste complète des sommets</li>
 *     <li>Gestion automatique des identifiants de sommets</li>
 * </ul>
 *
 * Cette structure est utilisée par les algorithmes de plus court chemin
 * tels que Dijkstra et A*.
 */
public class Graph {

    /** Liste des sommets du graphe. */
    private ArrayList<Vertex> vertexlist;

    /** Nombre actuel de sommets dans le graphe (sert aussi d'identifiant auto-incrémenté). */
    private int num_v = 0;

    /**
     * Construit un graphe vide.
     * La liste des sommets est initialisée mais aucun sommet n'est ajouté.
     */
    public Graph() {
        vertexlist = new ArrayList<>();
    }

    /**
     * Retourne la liste des sommets du graphe.
     *
     * @return liste des sommets
     */
    public ArrayList<Vertex> getVertexlist() {
        return vertexlist;
    }

    /**
     * Remplace la liste des sommets du graphe.
     *
     * @param vertexlist nouvelle liste de sommets
     */
    public void setVertexlist(ArrayList<Vertex> vertexlist) {
        this.vertexlist = vertexlist;
    }

    /**
     * Retourne le nombre de sommets actuellement présents dans le graphe.
     *
     * @return nombre de sommets
     */
    public int getNum_v() {
        return num_v;
    }

    /**
     * Modifie le nombre de sommets du graphe.
     *
     * @param num_v nouveau nombre de sommets
     */
    public void setNum_v(int num_v) {
        this.num_v = num_v;
    }

    /**
     * Ajoute un nouveau sommet au graphe.
     * <p>
     * Le sommet reçoit automatiquement un identifiant unique basé sur {@code num_v}.
     * </p>
     *
     * @param indivTime coût individuel du sommet (ex : coût du terrain)
     */
    public void addVertex(double indivTime) {
        Vertex v = new Vertex(num_v);
        v.setIndivTime(indivTime);
        vertexlist.add(v);
        num_v++;
    }

    /**
     * Ajoute une arête orientée entre deux sommets.
     *
     * @param source      indice du sommet source
     * @param destination indice du sommet destination
     * @param weight      poids (coût) de l'arête
     */
    public void addEgde(int source, int destination, double weight) {
        Edge edge = new Edge(source, destination, weight);
        vertexlist.get(source).addNeighbor(edge);
    }

}
