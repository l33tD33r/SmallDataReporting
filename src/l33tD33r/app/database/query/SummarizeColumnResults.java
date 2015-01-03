package l33tD33r.app.database.query;

import com.sun.javafx.scene.control.behavior.TableViewBehavior;
import sun.swing.BakedArrayList;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simon on 12/23/2014.
 */
public class SummarizeColumnResults {

    private Query query;

    private Map<Column,ColumnSummarizationValues> columnSummarizationValues;

    private ArrayList<ResultRow> sortedRows;

    public SummarizeColumnResults(Query query) {
        this.query = query;

        columnSummarizationValues = new HashMap<>();

        for (int i=0; i < query.getColumnCount(); i++) {
            Column column = query.getColumn(i);
            if (!column.hasSummarization()) {
                continue;
            }

            ColumnSummarizationValues summarizationValues;

            switch (column.getDataType()) {
                case Integer:
                    summarizationValues = new IntegerColumnSummarizationValues(column);
                    break;
                case Number:
                    summarizationValues = new NumberColumnSummarizationValues(column);
                    break;
                default:
                    throw new RuntimeException(MessageFormat.format("Data type {0} is not supported for Column Summarization", column.getDataType().name()));
            }

            columnSummarizationValues.put(column, summarizationValues);
        }

        sortedRows = new ArrayList<>();
    }

    public void addRow(ResultRow row) {
        sortedRows.add(row);

        for (Column column : columnSummarizationValues.keySet()) {
            ColumnSummarizationValues summarizationValues = columnSummarizationValues.get(column);

            Object resetColumnValue = null;
            if (column.getSummarization().hasResetColumnName()) {
                resetColumnValue = row.getValue(column.getSummarization().getResetColumnName());
            }
            switch (column.getDataType()) {
                case Integer: {
                    IntegerColumnSummarizationValues integerSummarizationValues = (IntegerColumnSummarizationValues)summarizationValues;
                    integerSummarizationValues.addValue(row.getIntegerValue(column.getName()), resetColumnValue);
                    break;
                }
                case Number:
                    NumberColumnSummarizationValues numberSummarizationValues = (NumberColumnSummarizationValues)summarizationValues;
                    numberSummarizationValues.addValue(row.getNumberValue(column.getName()), resetColumnValue);
                    break;
                default:
                    throw new RuntimeException(MessageFormat.format("", column.getDataType().name()));
            }
        }
    }

    public ArrayList<ResultRow> getSummarizedRows() {
        int rowIndex = 0;
        for (ResultRow sortedRow : sortedRows) {
            for (Column column : columnSummarizationValues.keySet()) {
                ColumnSummarizationValues summarizedValues = columnSummarizationValues.get(column);

                Object summarizedValue = summarizedValues.getSummarizedValue(rowIndex);

                sortedRow.setValue(column.getName(), summarizedValue);
            }
            rowIndex++;
        }
        return sortedRows;
    }

    private abstract class ColumnSummarizationValues<TValue extends Number> {

        private Column column;

        private ArrayList<ArrayList<TValue>> groupedValues;

        private Object lastResetColumnValue;

        private ArrayList<TValue> summarizedValues;

        public ColumnSummarizationValues(Column column) {
            if (!column.hasSummarization()) {
                throw new IllegalArgumentException("");
            }
            this.column = column;
            groupedValues = new ArrayList<>();
        }

        public void addValue(TValue value, Object resetColumnValue) {
            ArrayList<TValue> values = getValues(resetColumnValue);
            values.add(value);
        }

        private ArrayList<TValue> getValues(Object currentResetColumnValue) {
            if (groupedValues.size() == 0 || (lastResetColumnValue != null && !lastResetColumnValue.equals(currentResetColumnValue))) {
                lastResetColumnValue = currentResetColumnValue;
                ArrayList<TValue> values = new ArrayList<>();
                groupedValues.add(values);
                return values;
            } else {
                return groupedValues.get(groupedValues.size()-1);
            }
        }

        public TValue getSummarizedValue(int rowIndex) {
            return getSummarizedValues().get(rowIndex);
        }

        private ArrayList<TValue> getSummarizedValues() {
            if (summarizedValues == null) {
                summarizedValues = createSummarizedValues();
            }
            return summarizedValues;
        }

        private ArrayList<TValue> createSummarizedValues() {
            ArrayList<TValue> summarizedValues = new ArrayList<>();

            for (ArrayList<TValue> values : groupedValues) {
                summarizedValues.addAll(applySummarizationToGroup(values));
            }

            return summarizedValues;
        }

        private ArrayList<TValue> applySummarizationToGroup(ArrayList<TValue> groupedValues) {
            switch (column.getSummarization().getRule()) {
                case RunningBalance:
                    return applyRunningBalanceToGroup(groupedValues);
                default:
                    throw new RuntimeException(MessageFormat.format("Column summarization rule {0} not supported", column.getSummarization().getRule().name()));
            }
        }

        private ArrayList<TValue> applyRunningBalanceToGroup(ArrayList<TValue> groupedValues) {
            TValue runningBalanceValue = null;

            ArrayList<TValue> summarizecValues = new ArrayList<>();

            for (TValue groupedValue : groupedValues) {
                TValue summarizedValue = null;

                if (runningBalanceValue == null) {
                    summarizedValue = groupedValue;
                } else {
                    summarizedValue = add(groupedValue, runningBalanceValue);
                }

                summarizecValues.add(summarizedValue);
                runningBalanceValue = summarizedValue;
            }

            return summarizecValues;
        }

        protected abstract TValue add(TValue a, TValue b);
    }

    private class IntegerColumnSummarizationValues extends ColumnSummarizationValues<Integer> {

        public IntegerColumnSummarizationValues(Column column) {
            super(column);
        }

        @Override
        protected Integer add(Integer a, Integer b) {
            return a + b;
        }
    }

    private class NumberColumnSummarizationValues extends ColumnSummarizationValues<Double> {

        public NumberColumnSummarizationValues(Column column) {
            super(column);
        }

        @Override
        protected Double add(Double a, Double b) {
            return a + b;
        }
    }
}
