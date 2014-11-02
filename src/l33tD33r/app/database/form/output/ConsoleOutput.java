package l33tD33r.app.database.form.output;

import java.util.ArrayList;

/**
 * Created by Simon on 10/25/2014.
 */
public class ConsoleOutput extends Output {

    private ArrayList<WriteLine> writeLines;

    public ConsoleOutput() {
        writeLines = new ArrayList<>();
    }

    @Override
    public OutputType getType() {
        return OutputType.Console;
    }

    public ArrayList<WriteLine> getWriteLines() {
        return new ArrayList<>(writeLines);
    }
    public void addWriteLine(WriteLine writeLine) {
        writeLines.add(writeLine);
    }

    @Override
    public String generateOutput() {
        StringBuffer outputBuffer = new StringBuffer();

        for (WriteLine writeLine : getWriteLines()) {
            outputBuffer.append(writeLine.getValueSource().getValue().toString());
            outputBuffer.append("\n");
        }

        return outputBuffer.toString();
    }
}
