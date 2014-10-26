package l33tD33r.app.database.form;

import l33tD33r.app.database.form.storage.FormProvider;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Simon on 10/24/2014.
 */
public class FormManager {

    // Singleton
    private static FormManager singleton = null;

    public static boolean singletonCreated() { return singleton != null; }

    public static void createSingleton(FormProvider formProvider) {
        if (singletonCreated()) {
            throw new RuntimeException("FormManager can only be created once");
        }
        singleton = new FormManager(formProvider);
    }

    public static FormManager getSingleton() {
        return singleton;
    }

    private FormProvider formProvider;

    private LinkedHashMap<String,Form> formsMap;

    public FormManager(FormProvider formProvider) {
        this.formProvider = formProvider;
        formsMap = new LinkedHashMap<>();
    }

    private void loadForms() {
        Document formDocument = formProvider.getFormsDocument();
        if (formDocument == null) {
            return;
        }
        for (Form form : FormSerialization.deserializeForms(formDocument)) {
            loadForm(form);
        }
    }

    private void loadForm(Form report) {
        formsMap.put(report.getName(), report);
    }

    public void reloadForms() {
        formsMap.clear();
        loadForms();
    }

    public Form getForm(String reportName) {
        return formsMap.get(reportName);
    }

    public ArrayList<String> getReportNames() {
        return new ArrayList<>(formsMap.keySet());
    }
}
