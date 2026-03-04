package org.algo.wayx.model.heuristics;

/**
 * Implémentation de l'heuristique euclidienne pour l'algorithme A*.
 *
 * <p>
 * Cette heuristique estime la distance entre un sommet courant et le sommet
 * d'arrivée en utilisant la distance euclidienne classique dans un plan 2D :
 * </p>
 *
 * <pre>
 *     h(n) = sqrt( (x1 - x2)^2 + (y1 - y2)^2 )
 * </pre>
 *
 * <p>
 * Elle est admissible lorsque les déplacements diagonaux sont autorisés,
 * ce qui garantit que l'algorithme A* trouve toujours un chemin optimal.
 * </p>
 *
 * <p>
 * Les coordonnées (x, y) des sommets sont déduites de leur index linéaire
 * en utilisant le nombre de colonnes de la carte.
 * </p>
 */
public class EuclideanHeuristic implements Heuristic {

    /**
     * Calcule la distance euclidienne estimée entre un sommet courant et le sommet d'arrivée.
     *
     * @param c     index du sommet courant
     * @param g     index du sommet d'arrivée
     * @param ncols nombre de colonnes de la carte (permet de convertir un index en coordonnées)
     * @return      la distance euclidienne estimée entre les deux sommets
     */
    @Override
    public double estimate(int c, int g, int ncols) {
        int x1 = c / ncols, y1 = c % ncols;
        int x2 = g / ncols, y2 = g % ncols;
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}
