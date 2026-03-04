package org.algo.wayx.model;

import org.algo.wayx.model.util.Graph;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Classe utilitaire responsable du chargement d'une carte depuis un fichier texte
 * et de la construction du graphe correspondant.
 *
 * <p>
 * Le fichier doit respecter le format défini dans l'énoncé :
 * </p>
 *
 * <ul>
 *     <li>Section <b>=Size=</b> : dimensions de la carte</li>
 *     <li>Section <b>=Types=</b> : types de terrain, temps associés et couleurs</li>
 *     <li>Section <b>==Graph==</b> : grille de caractères représentant les terrains</li>
 *     <li>Section <b>==Path==</b> : coordonnées du départ et de l'arrivée</li>
 * </ul>
 *
 * <p>
 * Une fois la carte lue, la classe construit un graphe pondéré où :
 * </p>
 *
 * <ul>
 *     <li>Chaque case est un sommet</li>
 *     <li>Chaque sommet est connecté à ses 8 voisins (connexité étendue)</li>
 *     <li>Le poids d'une arête A→B est :
 *         <pre>
 *             (tA + tB) / 2            pour un déplacement horizontal/vertical
 *             (tA + tB) / 2 × √2       pour un déplacement diagonal
 *         </pre>
 *     </li>
 * </ul>
 *
 * <p>
 * La classe fournit également les coordonnées linéarisées du point de départ
 * et du point d'arrivée, utilisables directement par Dijkstra et A*.
 * </p>
 */
public final class MapManager{

    /**
     * Structure contenant toutes les données extraites du fichier :
     * <ul>
     *     <li>le graphe pondéré</li>
     *     <li>les dimensions de la carte</li>
     *     <li>les couleurs associées aux types de terrain</li>
     *     <li>les indices linéarisés du départ et de l'arrivée</li>
     * </ul>
     */
    public static class MapData {
        public Graph graph;
        public int nlines;
        public int ncols;
        public int startV;
        public int endV;
        public HashMap<Integer, String> groundColor;
    }

    /**
     * Charge une carte depuis un fichier texte et construit le graphe associé.
     *
     * @param filename chemin du fichier à lire
     * @return un objet {@link MapData} contenant toutes les informations de la carte,
     *         ou {@code null} en cas d'erreur de lecture
     */
    public static MapData loadMap(String filename) {
        MapData map = new MapData();
        map.graph = new Graph();
        map.groundColor = new HashMap<>();

        try (Scanner sc = new Scanner(new File(filename))) {

            Map<String, Integer> groundTypes = new HashMap<>();

            if (!readSize(sc, map)) return null;
            if (!readTypes(sc, map, groundTypes)) return null;

            double[][] timeGrid = readGrid(sc, map, groundTypes);
            if (timeGrid == null) return null;

            map.graph = buildGraph(map.nlines, map.ncols, timeGrid);

            if (!readPath(sc, map)) return null;

            return map;

        } catch (Exception e) {
            System.err.println("[ERREUR] Impossible de lire le fichier : " + e.getMessage());
            return null;
        }
    }


    // SECTION : SIZE
    private static boolean readSize(Scanner sc, MapData map) {
        try {
            if (!skipTo(sc, "=Size=")) {
                System.err.println("[ERREUR] Section =Size= manquante");
                return false;
            }

            map.nlines = readInt(sc.nextLine());
            map.ncols = readInt(sc.nextLine());

            if (map.nlines <= 0 || map.ncols <= 0) {
                System.err.println("[ERREUR] Dimensions invalides : " + map.nlines + "x" + map.ncols);
                return false;
            }

            return true;

        } catch (Exception e) {
            System.err.println("[ERREUR] Lecture de la section Size : " + e.getMessage());
            return false;
        }
    }


    // SECTION : TYPES
    private static boolean readTypes(Scanner sc, MapData map, Map<String, Integer> groundTypes) {
        try {
            if (!skipTo(sc, "=Types=")) {
                System.err.println("[ERREUR] Section =Types= manquante");
                return false;
            }

            String line = sc.nextLine();

            while (!line.equals("==Graph==")) {

                if (line.trim().isEmpty()) {
                    line = sc.nextLine();
                    continue;
                }

                String[] parts = line.split("=");
                if (parts.length != 2) {
                    System.err.println("[ERREUR] Format type invalide : " + line);
                    return false;
                }

                String symbol = parts[0].trim();
                int time = Integer.parseInt(parts[1].trim());

                if (!sc.hasNextLine()) {
                    System.err.println("[ERREUR] Couleur manquante pour " + symbol);
                    return false;
                }

                String color = sc.nextLine().trim();

                groundTypes.put(symbol, time);
                map.groundColor.put(time, color);

                if (!sc.hasNextLine()) {
                    System.err.println("[ERREUR] Fin de section Types inattendue");
                    return false;
                }

                line = sc.nextLine();
            }

            return true;

        } catch (Exception e) {
            System.err.println("[ERREUR] Lecture de la section Types : " + e.getMessage());
            return false;
        }
    }


    // SECTION : GRID
    private static double[][] readGrid(Scanner sc, MapData map, Map<String, Integer> groundTypes) {
        try {
            double[][] grid = new double[map.nlines][map.ncols];

            for (int i = 0; i < map.nlines; i++) {

                if (!sc.hasNextLine()) {
                    System.err.println("[ERREUR] Ligne de grille manquante (ligne " + i + ")");
                    return null;
                }

                String row = sc.nextLine();

                if (row.length() != map.ncols) {
                    System.err.println("[ERREUR] Ligne " + i + " invalide : " + row.length() + " colonnes");
                    return null;
                }

                for (int j = 0; j < map.ncols; j++) {
                    char c = row.charAt(j);
                    Integer time = groundTypes.get(String.valueOf(c));

                    if (time == null) {
                        System.err.println("[ERREUR] Caractère inconnu dans la grille : '" + c + "'");
                        return null;
                    }

                    grid[i][j] = time;
                }
            }

            return grid;

        } catch (Exception e) {
            System.err.println("[ERREUR] Lecture de la grille : " + e.getMessage());
            return null;
        }
    }


    // GRAPH BUILDING
    /**
     * Construit un graphe pondéré à partir d'une grille de types de terrain.
     * Connexité : 8 voisins.
     * Poids d'une arête A→B :
     *   - horizontal/vertical : (tA + tB) / 2
     *   - diagonal : (tA + tB) / 2 × sqrt(2)
     *
     * @param nlines nombre de lignes
     * @param ncols nombre de colonnes
     * @param times tableau des temps par case (déjà converti depuis les types)
     * @return graphe pondéré conforme aux consignes
     */
    public static Graph buildGraph(int nlines, int ncols, double[][] times) {

        Graph graph = new Graph();

        // Ajouter tous les sommets
        for (int i = 0; i < nlines; i++) {
            for (int j = 0; j < ncols; j++) {
                graph.addVertex(times[i][j]); // indivTime = tA
            }
        }

        // Ajouter les arêtes (8 directions)
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},     // 4 voisins cardinaux
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}    // 4 diagonales
        };

        for (int i = 0; i < nlines; i++) {
            for (int j = 0; j < ncols; j++) {

                int A = i * ncols + j; // index du sommet A
                double tA = times[i][j];

                for (int[] d : directions) {

                    int ni = i + d[0];
                    int nj = j + d[1];

                    // Vérifier que le voisin est dans la grille
                    if (ni < 0 || ni >= nlines || nj < 0 || nj >= ncols)
                        continue;

                    int B = ni * ncols + nj;
                    double tB = times[ni][nj];

                    // Poids de base
                    double base = (tA + tB) / 2.0;

                    // Diagonale ?
                    boolean diagonal = (d[0] != 0 && d[1] != 0);

                    double weight = diagonal ? base * Math.sqrt(2) : base;

                    graph.addEgde(A, B, weight);
                }
            }
        }

        return graph;
    }

    // PATH
    /**
     * Lit la section ==Path== du fichier et extrait les coordonnées du point de départ
     * et du point d'arrivée.
     *
     * <p>
     * Le format attendu est :
     * </p>
     *
     * <pre>
     * ==Path==
     * Start = row,col
     * Finish = row,col
     * </pre>
     *
     * <p>
     * Les coordonnées (ligne, colonne) sont ensuite converties en indices linéarisés
     * utilisables directement par les algorithmes de recherche (Dijkstra, A*).
     * </p>
     *
     * @param sc   scanner lisant le fichier de carte
     * @param map  structure contenant les données de la carte
     * @return     {@code true} si la section a été lue correctement, {@code false} sinon
     */
    private static boolean readPath(Scanner sc, MapData map) {
        try {
            if (!skipTo(sc, "==Path==")) {
                System.err.println("[ERREUR] Section ==Path== manquante");
                return false;
            }

            String startLine = sc.nextLine();
            String endLine = sc.nextLine();

            String[] s = startLine.split("=")[1].split(",");
            String[] e = endLine.split("=")[1].split(",");

            map.startV = Integer.parseInt(s[0]) * map.ncols + Integer.parseInt(s[1]);
            map.endV = Integer.parseInt(e[0]) * map.ncols + Integer.parseInt(e[1]);

            return true;

        } catch (Exception ex) {
            System.err.println("[ERREUR] Lecture du Path : " + ex.getMessage());
            return false;
        }
    }



    // UTILITAIRES
    /**
     * Avance dans le fichier jusqu'à trouver une ligne correspondant exactement
     * à la chaîne cible.
     *
     * <p>
     * Cette méthode est utilisée pour naviguer entre les différentes sections
     * du fichier de carte (ex. : =Size=, =Types=, ==Graph==, ==Path==).
     * </p>
     *
     * @param sc      scanner lisant le fichier
     * @param target  chaîne à rechercher (doit correspondre exactement à une ligne)
     * @return        {@code true} si la ligne a été trouvée, {@code false} sinon
     */
    private static boolean skipTo(Scanner sc, String target) {
        while (sc.hasNextLine()) {
            if (sc.nextLine().trim().equals(target)) return true;
        }
        return false;
    }

    /**
     * Extrait un entier d'une ligne au format "clé = valeur".
     *
     * <p>
     * Exemple de ligne valide :
     * </p>
     *
     * <pre>
     * nlines = 20
     * </pre>
     *
     * @param line ligne contenant un entier après un signe '='
     * @return     la valeur entière extraite
     * @throws NumberFormatException si la ligne ne contient pas un entier valide
     */
    private static int readInt(String line) {
        return Integer.parseInt(line.split("=")[1].trim());
    }
}


