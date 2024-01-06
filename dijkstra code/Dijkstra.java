import java.util.*;

public class Dijkstra {
    static class Edge {
        int destination;
        int weight;

        public Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input: Number of vertices and edges
        System.out.println("Enter the number of vertices and edges (n m):");
        int n = scanner.nextInt();  // Number of vertices
        int m = scanner.nextInt();  // Number of edges

        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // Input: Edge descriptions
        System.out.println("Enter the edge descriptions (v1 v2 w):");
        for (int i = 0; i < m; i++) {
            int v1 = scanner.nextInt();
            int v2 = scanner.nextInt();
            int w = scanner.nextInt();

            // Adding edges for an undirected graph
            graph.get(v1 - 1).add(new Edge(v2 - 1, w));
            graph.get(v2 - 1).add(new Edge(v1 - 1, w));
        }

        // Input: Starting and ending vertices
        System.out.println("Enter the starting and ending vertices (s e):");
        int startVertex = scanner.nextInt() - 1;
        int endVertex = scanner.nextInt() - 1;

        // Compute shortest path in terms of edge count
        List<Integer> shortestPathEdges = dijkstra(graph, startVertex, endVertex, n, false);

        // Compute shortest path in terms of weight sum
        List<Integer> shortestPathWeight = dijkstra(graph, startVertex, endVertex, n, true);

        // Output results
        System.out.println("Shortest path in terms of edge count: " + (shortestPathEdges.size() - 1));
        System.out.println("Vertices on the path: " + shortestPathEdges);

        System.out.println("Shortest path in terms of weight sum: " + getWeightSum(graph, shortestPathWeight));
        System.out.println("Vertices on the path: " + shortestPathWeight);
    }

    private static List<Integer> dijkstra(List<List<Edge>> graph, int start, int end, int n, boolean useWeights) {
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        priorityQueue.add(new int[]{start, 0});

        int[] distances = new int[n];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[start] = 0;

        int[] parents = new int[n];
        Arrays.fill(parents, -1);

        while (!priorityQueue.isEmpty()) {
            int[] current = priorityQueue.poll();
            int currentNode = current[0];
            int currentDistance = current[1];

            if (currentDistance > distances[currentNode]) {
                continue;
            }

            for (Edge neighbor : graph.get(currentNode)) {
                int newDistance = distances[currentNode] + (useWeights ? neighbor.weight : 1);

                if (newDistance < distances[neighbor.destination]) {
                    distances[neighbor.destination] = newDistance;
                    parents[neighbor.destination] = currentNode;
                    priorityQueue.add(new int[]{neighbor.destination, newDistance});
                }
            }
        }

        // Reconstruct the path
        List<Integer> path = new ArrayList<>();
        int current = end;
        while (current != -1) {
            path.add(current + 1);
            current = parents[current];
        }
        Collections.reverse(path);

        return path;
    }

    private static int getWeightSum(List<List<Edge>> graph, List<Integer> path) {
        int sum = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int current = path.get(i) - 1;
            int next = path.get(i + 1) - 1;

            for (Edge edge : graph.get(current)) {
                if (edge.destination == next) {
                    sum += edge.weight;
                    break;
                }
            }
        }
        return sum;
    }
}
