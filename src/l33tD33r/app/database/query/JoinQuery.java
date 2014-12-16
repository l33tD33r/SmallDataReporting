package l33tD33r.app.database.query;

import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Simon on 8/21/2014.
 */
public class JoinQuery extends Query {

    private Query leftSide;
    private Query rightSide;

    private JoinRules rules;

    public JoinQuery(Query leftSide, Query rightSide, JoinRules rules, String name, ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
        super(name, sourceFilterExpression, group, columns, resultFilterExpression);

        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.rules = rules;
    }

    @Override
    public IDataSource getDataSource() {
        return new JoinDataSource();
    }

    private class JoinDataSource implements IDataSource {

        private Map<KeySet,List<ResultRow>> leftSideKeyRowsMap;
        private Map<KeySet,List<ResultRow>> rightSideKeyRowsMap;
        private Set<KeySet> combinedKeys;

        private List<JoinDataRow> joinRows;
        private int currentJoinRowIndex;

        public JoinDataSource() {

            leftSideKeyRowsMap = findKeyRows(leftSide, rules.getLeftSideColumnNames());
            rightSideKeyRowsMap = findKeyRows(rightSide, rules.getRightSideColumnNames());

            combinedKeys = findCombinedKeys(leftSideKeyRowsMap.keySet(), rightSideKeyRowsMap.keySet());

            joinRows = createJoinRows();
            currentJoinRowIndex = 0;
        }

        @Override
        public boolean hasMoreElements() {
            return currentJoinRowIndex < joinRows.size();
        }

        @Override
        public IDataRow nextElement() {
            if (!hasMoreElements()) {
                throw new RuntimeException("no more elements");
            }
            JoinDataRow joinDataRow = joinRows.get(currentJoinRowIndex);
            joinDataRow.setRowIndex(currentJoinRowIndex);
            currentJoinRowIndex++;
            return joinDataRow;
        }

        private Map<KeySet,List<ResultRow>> findKeyRows(Query source, String[] keyNames) {
            Map<KeySet,List<ResultRow>> keyRowsMap = new HashMap<>();

            for (int rowIndex=0; rowIndex < source.getRowCount(); rowIndex++) {
                source.setPosition(rowIndex);

                ResultRow currentRow = source.getCurrentRow();

                Object[] keyValues = new Object[keyNames.length];

                for (int i=0; i < keyNames.length; i++) {
                    String keyName = keyNames[i];

                    keyValues[i] = currentRow.getValue(keyName);
                }

                KeySet keySet = new KeySet(keyValues);

                List<ResultRow> rows = keyRowsMap.get(keySet);

                if (rows == null) {
                    rows = new ArrayList<>();
                    keyRowsMap.put(keySet, rows);
                }

                rows.add(currentRow);
            }

            return keyRowsMap;
        }

        private Set<KeySet> findCombinedKeys(Set<KeySet> leftSideKeys, Set<KeySet> rightSideKeys) {
            Set<KeySet> combinedKeys = new HashSet<>();

            switch (rules.getType()) {
                case Inner:
                    combinedKeys.addAll(leftSideKeys);
                    combinedKeys.retainAll(rightSideKeys);
                    break;
                case Outer:
                    combinedKeys.addAll(leftSideKeys);
                    combinedKeys.addAll(rightSideKeys);
                    break;
                case Left:
                    combinedKeys.addAll(leftSideKeys);
                    break;
                case Right:
                    combinedKeys.addAll(rightSideKeys);
                    break;
            }

            return combinedKeys;
        }

        private List<JoinDataRow> createJoinRows() {
            List<JoinDataRow> joinDataRows = new ArrayList<>();

            String leftSideName = leftSide.getName();
            String rightSideName = rightSide.getName();

            for(KeySet keySet : combinedKeys) {
                List<ResultRow> leftSideRows = leftSideKeyRowsMap.get(keySet);
                List<ResultRow> rightSideRows = rightSideKeyRowsMap.get(keySet);

                if (leftSideRows != null && rightSideRows != null) {
                    for (ResultRow leftSideRow : leftSideRows) {
                        for (ResultRow rightSideRow : rightSideRows) {
                            joinDataRows.add(new JoinDataRow(JoinQuery.this, leftSideName, leftSideRow, rightSideName, rightSideRow));
                        }
                    }
                } else if (leftSideRows != null) {
                    for (ResultRow leftSideRow : leftSideRows) {
                        joinDataRows.add(new JoinDataRow(JoinQuery.this, leftSideName, leftSideRow, rightSideName, null));
                    }
                } else if (rightSideRows != null) {
                    for (ResultRow rightSideRow : rightSideRows) {
                        joinDataRows.add(new JoinDataRow(JoinQuery.this, leftSideName, null, rightSideName, rightSideRow));
                    }
                } else {
                    throw new RuntimeException("Both sides cannot have zero rows");
                }
            }

            return joinDataRows;
        }
    }

    private static class JoinDataRow implements IDataRow {

        private Query query;
        private String leftSideName;
        private ResultRow leftSideRow;

        private String rightSideName;
        private ResultRow rightSideRow;

        private int rowIndex;

        public JoinDataRow(Query query, String leftSideName, ResultRow leftSideRow, String rightSideName, ResultRow rightSideRow) {
            this.query = query;
            this.leftSideName = leftSideName;
            this.leftSideRow = leftSideRow;
            this.rightSideName = rightSideName;
            this.rightSideRow = rightSideRow;
        }

        public int getRowIndex() {
            return rowIndex;
        }
        public void setRowIndex(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        public IContext getContext() {
            return query;
        }

//        @Override
//        public DataType getType(String name) {
//            return null;
//        }

        @Override
        public Object getValue(String name) {
            if ("RowIndex".equalsIgnoreCase(name)) {
                return getRowIndex();
            }
            ResultRow sourceRow = getRow(name);
            if (sourceRow == null) {
                return "";
            }
            String rowColumnName = getRowColumnName(name);
            return sourceRow.getValue(rowColumnName);
        }

        private ResultRow getRow(String name) {
            String sourceName = getSourceColumnNames(name)[0];
            if (leftSideName.equalsIgnoreCase(sourceName)) {
                return leftSideRow;
            } else if (rightSideName.equalsIgnoreCase(sourceName)) {
                return rightSideRow;
            }
            throw new RuntimeException(MessageFormat.format("Unknown source ''{0}''. Must be either ''{1}'' or ''{2}''", sourceName, leftSideName, rightSideName));
        }

        private String getRowColumnName(String name) {
            return getSourceColumnNames(name)[1];
        }

        private String[] getSourceColumnNames(String name) {
            String[] sourceColumnNames = name.split("\\.", 2);

            if (sourceColumnNames.length != 2) {
                throw new RuntimeException(MessageFormat.format("", name));
            }

            return sourceColumnNames;
        }
    }
}
