package l33tD33r.app.ui.workspace.visualization.graph;

import l33tD33r.app.database.query.Query;
import l33tD33r.app.database.query.ResultRow;

import java.util.*;

/**
 * Created by Simon on 8/22/2014.
 */
public class Graph {

    private Map<String,Vertex> vertexes;
    private Set<Edge> edges;
    private Map<Integer,Set<Vertex>> depthVerticiesMap;

    public Graph() {
        vertexes = new HashMap<>();
        edges = new HashSet<>();
        depthVerticiesMap = new HashMap<>();
    }

    public void buildGraph(Query query, String vertexAColumn, String vertexBColumn, String edgeLabelColumn, String vertexColorColumn) {

        for (int rowIndex=0; rowIndex < query.getRowCount(); rowIndex++) {
            query.setPosition(rowIndex);

            ResultRow currentRow = query.getCurrentRow();

            String vertexALabel = currentRow.getStringValue(vertexAColumn);

            if (vertexALabel == null || vertexALabel.isEmpty()) {
                throw new RuntimeException("Vertex must have a label");
            }

            String vertexColor = currentRow.getStringValue(vertexColorColumn);

            Vertex vertexA = new Vertex(vertexALabel, vertexColor);

            if (!vertexes.containsKey(vertexALabel)) {
                vertexes.put(vertexALabel, vertexA);
            }
        }

        for (int rowIndex=0; rowIndex < query.getRowCount(); rowIndex++) {
            query.setPosition(rowIndex);

            ResultRow currentRow = query.getCurrentRow();

            String vertexALabel = currentRow.getStringValue(vertexAColumn);

            String vertexBLabel = currentRow.getStringValue(vertexBColumn);

            if (vertexBLabel == null || vertexBLabel.isEmpty()) {
                continue;
            }

            Vertex vertexA = vertexes.get(vertexALabel);
            Vertex vertexB = vertexes.get(vertexBLabel);

            Edge edge = new Edge(vertexB, vertexA, currentRow.getStringValue(edgeLabelColumn));
            edges.add(edge);

            vertexB.addChild(edge);
            vertexA.addParent(edge);
        }

        for (Vertex vertex : vertexes.values()) {
            int depth = findRequirementsDepth(vertex);

            Set<Vertex> depthVertices = depthVerticiesMap.get(depth);
            if (depthVertices == null) {
                depthVertices = new HashSet<>();
                depthVerticiesMap.put(depth, depthVertices);
            }

            depthVertices.add(vertex);
        }
    }

    private Integer findRequirementsDepth(Vertex currentVertex) {
        int depth = 0;
        for (Edge edge : currentVertex.getParentEdges()) {
            depth = Math.max(depth, findRequirementsDepth(edge.getA()) + 1);
        }
        return depth;
    }

    public Map<Integer,Set<Vertex>> getDepthVerticiesMap() {
        return depthVerticiesMap;
    }
}
