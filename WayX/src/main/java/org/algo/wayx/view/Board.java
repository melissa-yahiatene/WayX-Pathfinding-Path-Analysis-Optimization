package org.algo.wayx.view;

import org.algo.wayx.model.util.Graph;
import org.algo.wayx.model.util.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Composant graphique responsable de l'affichage de la carte, des terrains,
 * de l'exploration des algorithmes (Dijkstra, A*) et du chemin final trouvé.
 *
 * <p>
 * Le composant {@code Board} affiche :
 * </p>
 *
 * <ul>
 *     <li>la grille représentant la carte</li>
 *     <li>les couleurs associées aux types de terrain</li>
 *     <li>le point de départ (blanc)</li>
 *     <li>le point d'arrivée (noir)</li>
 *     <li>l'exploration des algorithmes (points gris dégradés + flèches)</li>
 *     <li>le chemin final (ligne rouge épaisse)</li>
 * </ul>
 *
 * <p>
 * L'affichage est recalculé à chaque appel de {@link #repaint()}.
 * </p>
 */
public class Board extends JComponent {

    /** Graphe contenant les sommets et arêtes de la carte. */
    private Graph graph;

    /** Nombre de colonnes de la grille. */
    private int ncols;

    /** Nombre de lignes de la grille. */
    private int nlines;

    /** Couleurs associées aux types de terrain. */
    private HashMap<Integer, String> colors;

    /** Index du sommet de départ. */
    private int start;

    /** Index du sommet d'arrivée. */
    private int end;

    /** Valeur maximale utilisée pour normaliser l'affichage de l'exploration. */
    private double max_distance;

    /** Sommet actuellement exploré (pour animation). */
    private int current;

    /** Chemin final calculé par l'algorithme. */
    private LinkedList<Integer> path;

    /** Indique si l'exploration doit être affichée. */
    private boolean showExploration = true;

    /**
     * Construit un composant graphique représentant la carte.
     *
     * @param graph  graphe contenant les sommets et arêtes
     * @param pixelSizeIgnored paramètre ignoré (hérité d'une version précédente)
     * @param ncols  nombre de colonnes de la carte
     * @param nlines nombre de lignes de la carte
     * @param colors table associant un temps de terrain à une couleur
     * @param start  index du sommet de départ
     * @param end    index du sommet d'arrivée
     */
    public Board(Graph graph, int pixelSizeIgnored, int ncols, int nlines,
                 HashMap<Integer, String> colors, int start, int end) {

        this.graph = graph;
        this.ncols = ncols;
        this.nlines = nlines;
        this.colors = colors;
        this.start = start;
        this.end = end;
        this.max_distance = ncols * nlines;
        this.current = -1;
        this.path = null;

        setPreferredSize(new Dimension(ncols * 10, nlines * 10));
    }

    /**
     * Dessine la carte, l'exploration et le chemin final.
     *
     * <p>
     * Cette méthode est appelée automatiquement par Swing lorsque le composant
     * doit être redessiné.
     * </p>
     *
     * @param g contexte graphique fourni par Swing
     */
    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int W = getWidth();
        int H = getHeight();

        int pixelSize = Math.min(W / ncols, H / nlines);

        int gridWidth = pixelSize * ncols;
        int gridHeight = pixelSize * nlines;

        int offsetX = (W - gridWidth) / 2;
        int offsetY = (H - gridHeight) / 2;

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, W, H);

        g2.setPaint(Color.LIGHT_GRAY);
        for (int i = 0; i <= nlines; i++) {
            g2.drawLine(offsetX, offsetY + i * pixelSize,
                    offsetX + gridWidth, offsetY + i * pixelSize);
        }
        for (int j = 0; j <= ncols; j++) {
            g2.drawLine(offsetX + j * pixelSize, offsetY,
                    offsetX + j * pixelSize, offsetY + gridHeight);
        }

        int num = 0;
        for (Vertex v : graph.getVertexlist()) {

            int i = num / ncols;
            int j = num % ncols;

            g2.setPaint(getColorForType(colors.get((int) v.getIndivTime())));
            g2.fill(new Rectangle2D.Double(
                    offsetX + j * pixelSize,
                    offsetY + i * pixelSize,
                    pixelSize, pixelSize));

            if (num == current) {
                g2.setPaint(Color.RED);
                g2.fill(new Ellipse2D.Double(
                        offsetX + j * pixelSize + pixelSize / 2.0 - 4,
                        offsetY + i * pixelSize + pixelSize / 2.0 - 4,
                        8, 8));
            }

            if (num == start) {
                g2.setPaint(Color.WHITE);
                g2.fill(new Ellipse2D.Double(
                        offsetX + j * pixelSize + pixelSize / 2.0 - 3,
                        offsetY + i * pixelSize + pixelSize / 2.0 - 3,
                        6, 6));
            }

            if (num == end) {
                g2.setPaint(Color.BLACK);
                g2.fill(new Ellipse2D.Double(
                        offsetX + j * pixelSize + pixelSize / 2.0 - 3,
                        offsetY + i * pixelSize + pixelSize / 2.0 - 3,
                        6, 6));
            }

            num++;
        }

        num = 0;
        for (Vertex v : graph.getVertexlist()) {

            int i = num / ncols;
            int j = num % ncols;

            if (showExploration && v.getTimeFromSource() < Double.POSITIVE_INFINITY) {

                float gval = (float) (1 - v.getTimeFromSource() / max_distance);
                gval = Math.max(0, gval);

                g2.setPaint(new Color(gval, gval, gval));
                g2.fill(new Ellipse2D.Double(
                        offsetX + j * pixelSize + pixelSize / 2.0 - 2,
                        offsetY + i * pixelSize + pixelSize / 2.0 - 2,
                        4, 4));

                Vertex prev = v.getPrev();
                if (prev != null) {
                    int i2 = prev.getNum() / ncols;
                    int j2 = prev.getNum() % ncols;

                    g2.setPaint(Color.BLACK);
                    g2.draw(new Line2D.Double(
                            offsetX + j * pixelSize + pixelSize / 2.0,
                            offsetY + i * pixelSize + pixelSize / 2.0,
                            offsetX + j2 * pixelSize + pixelSize / 2.0,
                            offsetY + i2 * pixelSize + pixelSize / 2.0));
                }
            }

            num++;
        }

        if (path != null) {
            g2.setStroke(new BasicStroke(3.0f));
            int prev = -1;
            for (int cur : path) {
                if (prev != -1) {
                    int i1 = prev / ncols;
                    int j1 = prev % ncols;
                    int i2 = cur / ncols;
                    int j2 = cur % ncols;

                    g2.setPaint(Color.RED);
                    g2.draw(new Line2D.Double(
                            offsetX + j1 * pixelSize + pixelSize / 2.0,
                            offsetY + i1 * pixelSize + pixelSize / 2.0,
                            offsetX + j2 * pixelSize + pixelSize / 2.0,
                            offsetY + i2 * pixelSize + pixelSize / 2.0));
                }
                prev = cur;
            }
        }
    }

    /**
     * Convertit une chaîne de couleur logique (green, gray, blue, yellow)
     * en une couleur réelle utilisée pour dessiner les cases.
     *
     * @param c nom de la couleur logique
     * @return  couleur correspondante ou magenta si inconnue
     */
    private Color getColorForType(String c) {
        if (c == null) return Color.MAGENTA;
        c = c.toLowerCase();
        if (c.equals("green")) return new Color(34, 139, 34);
        if (c.equals("gray")) return new Color(169, 169, 169);
        if (c.equals("blue")) return new Color(70, 130, 180);
        if (c.equals("yellow")) return new Color(255, 215, 0);
        return Color.MAGENTA;
    }

    /**
     * Met à jour l'état du plateau pendant l'exploration d'un algorithme.
     *
     * @param graph   graphe mis à jour
     * @param current sommet actuellement exploré
     */
    public void update(Graph graph, int current) {
        this.graph = graph;
        this.current = current;
        repaint();
    }

    /**
     * Affiche le chemin final trouvé par l'algorithme.
     *
     * @param graph graphe utilisé
     * @param path  liste des sommets constituant le chemin optimal
     */
    public void addPath(Graph graph, LinkedList<Integer> path) {
        this.graph = graph;
        this.path = path;
        this.current = -1;
        repaint();
    }

    /**
     * Réinitialise l'affichage : exploration effacée, chemin supprimé,
     * distances et prédécesseurs remis à l'état initial.
     */
    public void reset() {
        this.path = null;
        this.current = -1;

        for (Vertex v : graph.getVertexlist()) {
            v.setPrev(null);
            v.setTimeFromSource(Double.POSITIVE_INFINITY);
        }

        repaint();
    }

    /**
     * Active ou désactive l'affichage de l'exploration (points gris + flèches).
     *
     * @param show true pour afficher l'exploration, false pour la masquer
     */
    public void setShowExploration(boolean show) {
        this.showExploration = show;
        repaint();
    }

    /**
     * Indique si l'affichage de l'exploration est activé.
     *
     * @return true si l'exploration est visible, false sinon
     */
    public boolean isShowExploration() {
        return showExploration;
    }
}






