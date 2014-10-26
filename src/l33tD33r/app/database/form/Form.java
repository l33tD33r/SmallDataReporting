package l33tD33r.app.database.form;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Simon on 10/24/2014.
 */
public class Form {

    private String name;
    private String title;

    private LinkedHashMap<String,Item> items;
    private ArrayList<View> views;
    private ArrayList<Output> outputs;

    public Form() {
        items = new LinkedHashMap<>();
        views = new ArrayList<>();
        outputs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Item getItem(String itemId) {
        return items.get(itemId);
    }

    public ArrayList<String> getItemIds() {
        return new ArrayList<>(items.keySet());
    }

    public void addItem(Item item) {
        items.put(item.getId(), item);
    }

    public ArrayList<View> getViews() {
        return new ArrayList<>(views);
    }
    public void addView(View view) {
        views.add(view);
    }

    public ArrayList<Output> getOutputs() {
        return new ArrayList<>(outputs);
    }
    public void addOutput(Output output) {
        outputs.add(output);
    }
}
