package l33tD33r.app.database.query;

import java.text.MessageFormat;

/**
 * Created by Simon on 8/21/2014.
 */
public class JoinRules {
    private String[] leftSideColumnNames;
    private String[] rightSideColumnNames;

    private int matchingColumnCount;

    private JoinType type;

    public JoinRules(String[] leftSideColumnNames, String[] rightSideColumnNames, JoinType type) {
        this.leftSideColumnNames = leftSideColumnNames;
        this.rightSideColumnNames = rightSideColumnNames;
        this.type = type;

        if (leftSideColumnNames.length != rightSideColumnNames.length) {
            throw new RuntimeException(MessageFormat.format("Number of left and ride side columns for matching must be the same - Left:{0} Right:{1}", leftSideColumnNames.length, rightSideColumnNames.length));
        }

        matchingColumnCount = leftSideColumnNames.length;
    }

    public int getMatchingColumnCount() {
        return matchingColumnCount;
    }

    public String[] getLeftSideColumnNames() {
        return leftSideColumnNames;
    }

    public String[] getRightSideColumnNames() {
        return rightSideColumnNames;
    }

    public String getLeftSideColumnName(int index) {
        checkIndex(index);
        return leftSideColumnNames[index];
    }

    public String getRightSideColumnName(int index) {
        checkIndex(index);
        return rightSideColumnNames[index];
    }

    public JoinType getType() {
        return type;
    }

    private void checkIndex(int index) {
        if (index < 0) {
            throw new RuntimeException(MessageFormat.format("Index cannot be below zero: {0}", index));
        }
        if (index >= getMatchingColumnCount()) {
            throw new RuntimeException(MessageFormat.format("Index is out of bounds. Size:{0} Index:{1}", getMatchingColumnCount(), index));
        }
    }
}
