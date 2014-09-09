package l33tD33r.app.ui.workspace.visualization;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import l33tD33r.app.database.report.Report;
import l33tD33r.app.ui.workspace.visualization.graph.Edge;
import l33tD33r.app.ui.workspace.visualization.graph.Graph;
import l33tD33r.app.ui.workspace.visualization.graph.Vertex;

import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Simon on 8/22/2014.
 */
public class GraphView {

    private Graph graph;

    public GraphView(Report report, String vertexAColumn, String vertexBColumn, String edgeLabelColumn, String vertexColorColumn) {
        graph = new Graph();
        graph.buildGraph(report.getQuery(), vertexAColumn, vertexBColumn, edgeLabelColumn, vertexColorColumn);
    }

    public Node getGraphView() {
//        VBox graphPane = new VBox();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefWidth(1920);
        scrollPane.setPrefHeight(1080);

        Pane graphPane = new Pane();

        List<Integer> depths = new ArrayList<>(graph.getDepthVerticiesMap().keySet());

        depths.sort( (a,b) -> a.compareTo(b) );

        Map<String,StackPane> vertexPaneMap = new HashMap<>();

        for (Integer depth : depths) {
//            HBox currentDepthVerticesPane = new HBox();
//
//            graphPane.getChildren().add(currentDepthVerticesPane);

            int vertexIndex = 0;

            for (Vertex vertex : graph.getDepthVerticiesMap().get(depth)) {

                StackPane vertexPane = createVertexPane(vertex);

                vertexPaneMap.put(vertex.getLabel(), vertexPane);

//                currentDepthVerticesPane.setMargin(vertexPane, new Insets(10, 0, 0, 10));

//                currentDepthVerticesPane.getChildren().add(vertexPane);
                vertexPane.relocate(vertexIndex * 250 + 50, depth * 100 + 50);
                graphPane.getChildren().add(vertexPane);

                for (Edge edge : vertex.getParentEdges()) {
                    Line edgeLine = createEdgeLine(edge, vertexPaneMap);

                    graphPane.getChildren().add(edgeLine);
                }
                vertexIndex++;
            }
        }

//        return graphPane;
        scrollPane.setContent(graphPane);
        return scrollPane;
    }

    private static double VERTEX_WIDTH = 200;
    private static double VERTEX_HEIGHT = 50;

    private StackPane createVertexPane(Vertex vertex) {
        StackPane pane = new StackPane();

        Rectangle box = new Rectangle();
        switch (vertex.getColor()) {
            case "Red":
                box.setFill(Color.RED);
                break;
            case "Blue":
                box.setFill(Color.BLUE);
                break;
            case "Green":
                box.setFill(Color.GREEN);
                break;
            case "Yellow":
                box.setFill(Color.YELLOW);
                break;
        }
        box.setHeight(VERTEX_HEIGHT);
        box.setWidth(VERTEX_WIDTH);

        Text label = new Text(vertex.getLabel());
        label.setFill(Color.BLACK);

        pane.getChildren().addAll(box, label);

        pane.setAlignment(Pos.CENTER);
//        StackPane.setMargin(label, new Insets(5, 5, 5, 5));

        return pane;
    }

    private Line createEdgeLine(Edge edge, Map<String,StackPane> vertexPaneMap) {
        Line edgeLine = new Line();
        edgeLine.setStroke(Color.BLACK);
        StackPane vertexAPane = vertexPaneMap.get(edge.getA().getLabel());
        StackPane vertexBPane = vertexPaneMap.get(edge.getB().getLabel());

        edgeLine.setStartX(getStartX(vertexAPane));
        edgeLine.setStartY(getStartY(vertexAPane));

        edgeLine.setEndX(getEndX(vertexBPane));
        edgeLine.setEndY(getEndY(vertexBPane));

        return edgeLine;
    }

    private double getStartX(StackPane vertexPane) {
        return vertexPane.getLayoutX() + VERTEX_WIDTH / 2;
    }

    private double getStartY(StackPane vertexPane) {
        return vertexPane.getLayoutY() + VERTEX_HEIGHT;
    }

    private double getEndX(StackPane vertexPane) {
        return vertexPane.getLayoutX()  +VERTEX_WIDTH / 2;
    }

    private double getEndY(StackPane vertexPane) {
        return vertexPane.getLayoutY();
    }
}
