package l33tD33r.app.ui.workspace.visualization.graph;

/**
 * Created by Simon on 8/22/2014.
 */
public class Edge {
    private Vertex a;
    private Vertex b;

    private String label;

    public Edge(Vertex a, Vertex b, String label) {
        this.a = a;
        this.b = b;
        this.label = label;
    }

    public Vertex getA() {
        return a;
    }

    public Vertex getB() {
        return b;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            return equals((Edge)obj);
        }
        return false;
    }

    public boolean equals(Edge other) {
        if (!getLabel().equals(other.getLabel())) {
            return false;
        }
        if (!getA().equals(other.getA())) {
            return false;
        }
        if (!getB().equals(other.getB())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (getLabel() + getA().getLabel() + getB().getLabel()).hashCode();
    }
}
