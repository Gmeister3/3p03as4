import java.util.*;
import java.io.*;

public class BellmanFordDP {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File(args[0]));
        int n = sc.nextInt();
        int[][] w = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                w[i][j] = sc.nextInt();
        sc.close();

        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (i != j && w[i][j] != 0)
                    edges.add(new int[]{i, j, w[i][j]});

        final int INF = Integer.MAX_VALUE / 2;
        int[] dist = new int[n];
        int[] pred = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(pred, -1);
        dist[0] = 0;

        for (int i = 0; i < n - 1; i++)
            for (int[] e : edges)
                if (dist[e[0]] < INF && (long) dist[e[0]] + e[2] < dist[e[1]]) {
                    dist[e[1]] = dist[e[0]] + e[2];
                    pred[e[1]] = e[0];
                }

        for (int[] e : edges)
            if (dist[e[0]] < INF && (long) dist[e[0]] + e[2] < dist[e[1]]) {
                writeOutput("There is a negative cycle", args[0]);
                return;
            }

        StringBuilder sb = new StringBuilder();
        for (int v = 0; v < n; v++) {
            List<Integer> path = new ArrayList<>();
            for (int cur = v; cur != -1; cur = pred[cur])
                path.add(0, cur);
            if (v > 0) sb.append('\n');
            sb.append(v).append(", ").append(dist[v]).append(", ").append(path);
        }
        writeOutput(sb.toString(), args[0]);
    }

    static void writeOutput(String result, String inputPath) throws IOException {
        System.out.println(result);
        String name = new File(inputPath).getName();
        if (name.endsWith(".txt")) name = name.substring(0, name.length() - 4);
        new File("outputs").mkdirs();
        try (PrintWriter pw = new PrintWriter("outputs/output_" + name + ".txt")) {
            pw.println(result);
        }
    }
}
