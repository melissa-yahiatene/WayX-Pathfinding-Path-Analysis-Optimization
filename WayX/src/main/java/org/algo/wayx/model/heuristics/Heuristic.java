package org.algo.wayx.model.heuristics;

/**
 * Interface représentant une heuristique utilisée par l'algorithme A*.
 *
 * <p>
 * Une heuristique est une fonction qui estime la distance restante entre un
 * sommet courant et le sommet d'arrivée. Elle permet à l'algorithme A* de
 * guider la recherche vers la cible plus efficacement que Dijkstra.
 * </p>
 *
 * <p>
 * Les implémentations courantes incluent :
 * <ul>
 *     <li>{@code ManhattanHeuristic} — distance de Manhattan</li>
 *     <li>{@code EuclideanHeuristic} — distance euclidienne</li>
 *     <li>{@code ZeroHeuristic} — équivalent à Dijkstra</li>
 * </ul>
 * </p>
 *
 * <p>
 * Les coordonnées des sommets sont déduites de leur index linéaire en utilisant
 * le nombre de colonnes de la carte.
 * </p>
 */
public interface Heuristic {

    /**
     * Estime la distance entre un sommet courant et le sommet d'arrivée.
     *
     * @param current index du sommet courant
     * @param goal    index du sommet d'arrivée
     * @param ncols   nombre de colonnes de la carte (permet de convertir un index en coordonnées)
     * @return        une estimation de la distance restante
     */
    double estimate(int current, int goal, int ncols);
}
