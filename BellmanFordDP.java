import java.util.*;
import java.io.*;

/**
 * BellmanFordDP - Shortest path algorithm using dynamic programming (Bellman-Ford).
 *
 * The Bellman-Ford algorithm computes the shortest paths from a single source
 * vertex (vertex 0) to all other vertices in a weighted directed graph.
 * Unlike Dijkstra's algorithm, it handles negative edge weights and can
 * detect negative-weight cycles.
 *
 * Input file format:
 *   - First line: integer n (number of vertices)
 *   - Next n lines: n integers each, representing the n×n adjacency/weight matrix
 *     where w[i][j] is the weight of the edge from i to j (0 means no edge)
 *
 * Output (to console and outputs/output_<inputname>.txt):
 *   - If a negative cycle is reachable, prints "There is a negative cycle"
 *   - Otherwise, for each vertex v: "v, distance, [path from 0 to v]"
 */
public class BellmanFordDP {
    public static void main(String[] args) throws IOException {
        // Read the adjacency matrix from the file specified as a command-line argument
        Scanner sc = new Scanner(new File(args[0]));
        int n = sc.nextInt(); // number of vertices
        int[][] w = new int[n][n]; // weight matrix: w[i][j] = weight of edge i->j (0 = no edge)
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                w[i][j] = sc.nextInt();
        sc.close();

        // Build the edge list from the adjacency matrix.
        // Each edge is stored as {from, to, weight}.
        // Self-loops (i == j) and absent edges (weight == 0) are skipped.
        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (i != j && w[i][j] != 0)
                    edges.add(new int[]{i, j, w[i][j]});

        // Use INF = MAX_VALUE/2 to avoid integer overflow when adding edge weights
        final int INF = Integer.MAX_VALUE / 2;
        int[] dist = new int[n];  // dist[v] = shortest known distance from source (vertex 0) to v
        int[] pred = new int[n];  // pred[v] = predecessor of v on the shortest path from source
        Arrays.fill(dist, INF);   // initialize all distances to infinity
        Arrays.fill(pred, -1);    // initialize all predecessors to none
        dist[0] = 0;              // distance from source to itself is 0

        // Bellman-Ford relaxation: repeat (n-1) times to propagate shortest paths.
        // After k iterations, dist[v] holds the shortest path using at most k edges.
        for (int i = 0; i < n - 1; i++)
            for (int[] e : edges)
                // Relax edge e[0]->e[1] with weight e[2] if a shorter path is found
                if (dist[e[0]] < INF && (long) dist[e[0]] + e[2] < dist[e[1]]) {
                    dist[e[1]] = dist[e[0]] + e[2];
                    pred[e[1]] = e[0];
                }

        // Negative-cycle detection: if any distance can still be improved after
        // (n-1) relaxations, there must be a negative-weight cycle in the graph.
        for (int[] e : edges)
            if (dist[e[0]] < INF && (long) dist[e[0]] + e[2] < dist[e[1]]) {
                writeOutput("There is a negative cycle", args[0]);
                return;
            }

        // Build and format the output: for each vertex, print its shortest distance
        // and the actual path from the source reconstructed via the predecessor array.
        StringBuilder sb = new StringBuilder();
        for (int v = 0; v < n; v++) {
            // Reconstruct path to v by following predecessors back to the source
            List<Integer> path = new ArrayList<>();
            for (int cur = v; cur != -1; cur = pred[cur])
                path.add(0, cur); // prepend to build path in source-to-v order
            if (v > 0) sb.append('\n');
            // Format: "vertex, distance, [path]"
            sb.append(v).append(", ").append(dist[v]).append(", ").append(path);
        }
        writeOutput(sb.toString(), args[0]);
    }

    /**
     * Writes the result to standard output and to a file in the outputs/ directory.
     * The output file is named "output_<inputname>.txt".
     *
     * @param result    the string to write
     * @param inputPath the path to the input file (used to derive the output filename)
     */
    static void writeOutput(String result, String inputPath) throws IOException {
        System.out.println(result);
        // Derive output filename from input filename (strip path and .txt extension)
        String name = new File(inputPath).getName();
        if (name.endsWith(".txt")) name = name.substring(0, name.length() - 4);
        new File("outputs").mkdirs(); // create outputs directory if it does not exist
        try (PrintWriter pw = new PrintWriter("outputs/output_" + name + ".txt")) {
            pw.println(result);
        }
    }
}
