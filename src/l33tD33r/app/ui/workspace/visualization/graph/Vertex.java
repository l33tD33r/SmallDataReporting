package l33tD33r.app.ui.workspace.visualization.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 8/22/2014.
 */
public class Vertex {

    private String label;

    private String color;

    private List<Edge> parentEdges;

    private List<Edge> childEdges;

    public Vertex(String label, String color) {
        this.label = label;
        this.color = color;

        parentEdges = new ArrayList<>();
        childEdges = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public String getColor() { return color; }

    public void addParent(Edge parent) {
        parentEdges.add(parent);
    }

    public List<Edge> getParentEdges() {
        return parentEdges;
    }

    public void addChild(Edge child) {
        childEdges.add(child);
    }

    public List<Edge> getChildEdges() {
        return childEdges;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertex) {
            return equals((Vertex)obj);
        }
        return false;
    }

    public boolean equals(Vertex other) {
        return getLabel().equals(other.getLabel());
    }

    @Override
    public int hashCode() {
        return getLabel().hashCode();
    }
}
