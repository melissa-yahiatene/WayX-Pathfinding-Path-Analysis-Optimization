package org.algo.wayx.model.heuristics;

/**
 * Implémentation de l'heuristique de Manhattan pour l'algorithme A*.
 *
 * <p>
 * Cette heuristique estime la distance entre un sommet courant et le sommet
 * d'arrivée en utilisant la distance de Manhattan, définie comme :
 * </p>
 *
 * <pre>
 *     h(n) = |x1 - x2| + |y1 - y2|
 * </pre>
 *
 * <p>
 * Elle est particulièrement adaptée aux environnements où les déplacements
 * se font principalement horizontalement et verticalement. Elle reste
 * admissible même si les diagonales sont autorisées, tant qu'elle ne
 * surestime jamais la distance réelle.
 * </p>
 *
 * <p>
 * Les coordonnées (x, y) des sommets sont déduites de leur index linéaire
 * en utilisant le nombre de colonnes de la carte.
 * </p>
 */
public class ManhattanHeuristic implements Heuristic {

    /**
     * Calcule la distance de Manhattan estimée entre un sommet courant
     * et le sommet d'arrivée.
     *
     * @param c     index du sommet courant
     * @param g     index du sommet d'arrivée
     * @param ncols nombre de colonnes de la carte (permet de convertir un index en coordonnées)
     * @return      la distance de Manhattan estimée entre les deux sommets
     */
    @Override
    public double estimate(int c, int g, int ncols) {
        int x1 = c % ncols, y1 = c / ncols;
        int x2 = g % ncols, y2 = g / ncols;
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
