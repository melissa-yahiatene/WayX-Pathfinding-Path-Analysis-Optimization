package org.algo.wayx.imageHandler;

import org.algo.wayx.model.util.Graph;

/**
 * Construit un graphe pondéré à partir d'une image en niveaux de gris.
 * Chaque pixel devient un sommet, et les arêtes sont créées selon la 4-connexité.
 * Le poids d'une arête est la différence d'intensité entre les deux pixels.
 */
public class GraphBuilder {

    /**
     * Transforme une matrice d'intensités en graphe pondéré.
     *
     * @param intensities matrice des intensités (0–255)
     * @return graphe pondéré
     */
    public static Graph buildGraph(int[][] intensities) {
        int h = intensities.length;
        int w = intensities[0].length;

        Graph graph = new Graph();

        // Ajouter les sommets
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                graph.addVertex(intensities[i][j]);
            }
        }

        // Ajouter les arêtes (4-connexité)
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int A = i * w + j;
                int tA = intensities[i][j];

                for (int[] d : dirs) {
                    int ni = i + d[0];
                    int nj = j + d[1];

                    if (ni >= 0 && ni < h && nj >= 0 && nj < w) {
                        int B = ni * w + nj;
                        int tB = intensities[ni][nj];
                        double weight = Math.abs(tA - tB);
                        graph.addEgde(A, B, weight);
                    }
                }
            }
        }

        return graph;
    }
}


