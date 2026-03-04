package org.algo.wayx;

import org.algo.wayx.model.algorithms.AStar;
import org.algo.wayx.model.algorithms.Dijkstra;
import org.algo.wayx.model.heuristics.EuclideanHeuristic;
import org.algo.wayx.model.heuristics.ManhattanHeuristic;
import org.algo.wayx.model.heuristics.ZeroHeuristic;
import org.algo.wayx.model.util.Edge;
import org.algo.wayx.model.util.Graph;
import org.algo.wayx.model.util.Vertex;

import java.util.LinkedList;

public class GraphTest {
    public static void main(String[] args) {

        // --- Création du graphe ---
        Graph graph = new Graph();
        graph.addVertex(10); // 0
        graph.addVertex(10); // 1
        graph.addVertex(10); // 2
        graph.addVertex(10); // 3
        graph.addVertex(10); // 4
        graph.addVertex(10); // 5

        graph.addEgde(0, 1, 4);
        graph.addEgde(0, 2, 3);
        graph.addEgde(1, 3, 2);
        graph.addEgde(1, 2, 5);
        graph.addEgde(2, 3, 7);
        graph.addEgde(3, 4, 2);
        graph.addEgde(4, 0, 4);
        graph.addEgde(4, 1, 4);
        graph.addEgde(4, 5, 6);

        System.out.println("=== Graphe chargé ===");
        for (Vertex v : graph.getVertexlist()) {
            System.out.print("Sommet " + v.getNum() + " -> ");
            for (Edge e : v.getAdjacencylist()) {
                System.out.print(e.getDestination() + "(w=" + e.getWeight() + ") ");
            }
            System.out.println();
        }

        int start = 0;
        int end = 5;
        int ncols = 3; // pour les heuristiques (3 colonnes par exemple)

        // --- Test Dijkstra ---
        System.out.println("\n=== Test Dijkstra ===");
        Dijkstra.compute(graph, start, end);
        LinkedList<Integer> pathD = Dijkstra.getPath(graph.getVertexlist().get(end));
        System.out.println("Chemin Dijkstra : " + pathD);
        System.out.println("Coût total : " + graph.getVertexlist().get(end).getTimeFromSource());

        // --- Reset des valeurs pour A* ---
        for (Vertex v : graph.getVertexlist()) {
            v.setPrev(null);
            v.setTimeFromSource(Double.POSITIVE_INFINITY);
        }

        // --- Test A* Euclidienne ---
        System.out.println("\n=== Test A* (Euclidienne) ===");
        AStar.compute(graph, start, end, ncols, new EuclideanHeuristic());
        LinkedList<Integer> pathE = AStar.getPath(graph.getVertexlist().get(end));
        System.out.println("Chemin A* : " + pathE);
        System.out.println("Coût total : " + graph.getVertexlist().get(end).getTimeFromSource());

        // --- Test A* Manhattan ---
        System.out.println("\n=== Test A* (Manhattan) ===");
        AStar.compute(graph, start, end, ncols, new ManhattanHeuristic());
        LinkedList<Integer> pathM = AStar.getPath(graph.getVertexlist().get(end));
        System.out.println("Chemin A* : " + pathM);
        System.out.println("Coût total : " + graph.getVertexlist().get(end).getTimeFromSource());

        // --- Test A* Zero ---
        System.out.println("\n=== Test A* (Zero) ===");
        AStar.compute(graph, start, end, ncols, new ZeroHeuristic());
        LinkedList<Integer> pathZ = AStar.getPath(graph.getVertexlist().get(end));
        System.out.println("Chemin A* : " + pathZ);
        System.out.println("Coût total : " + graph.getVertexlist().get(end).getTimeFromSource());
    }

}
