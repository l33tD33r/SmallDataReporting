package l33tD33r.app.database.query;

/**
 * Created by Simon on 12/22/2014.
 */
public class ColumnSummarization {

    private SummarizationRule rule;

    private String resetColumnName;

    public ColumnSummarization() {
    }

    public SummarizationRule getRule() {
        return rule;
    }
    public void setRule(SummarizationRule rule) {
        this.rule = rule;
    }

    public String getResetColumnName() {
        return resetColumnName;
    }
    public void setResetColumnName(String resetColumnName) {
        this.resetColumnName = resetColumnName;
    }
    public boolean hasResetColumnName() {
        return getResetColumnName() != null && !getResetColumnName().isEmpty();
    }
}
