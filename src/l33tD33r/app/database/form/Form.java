package l33tD33r.app.database.form;

import l33tD33r.app.database.form.action.Action;
import l33tD33r.app.database.form.data.Collection;
import l33tD33r.app.database.form.data.PropertyCollection;
import l33tD33r.app.database.form.data.ItemSource;
import l33tD33r.app.database.form.output.Output;
import l33tD33r.app.database.form.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simon on 10/24/2014.
 */
public class Form {

    private String name;
    private String title;

    private Map<String,ItemSource> items;
    private Map<String,Collection> collections;
    private ArrayList<View> views;
    private ArrayList<Action> actions;
    private ArrayList<Output> outputs;

    public Form() {
        items = new HashMap<>();
        collections = new HashMap<>();
        views = new ArrayList<>();
        actions = new ArrayList<>();
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

    public ItemSource getItem(String itemId) {
        return items.get(itemId);
    }

    public ArrayList<String> getItemIds() {
        return new ArrayList<>(items.keySet());
    }

    public void addItem(ItemSource itemSource) {
        items.put(itemSource.getId(), itemSource);
    }

    public Collection getCollection(String collectionId) { return collections.get(collectionId); }

    public ArrayList<String> getCollectionIds() { return new ArrayList<>(collections.keySet()); }

    public void addCollection(Collection collection) { collections.put(collection.getId(), collection); }

    public ArrayList<View> getViews() {
        return new ArrayList<>(views);
    }
    public void addView(View view) {
        views.add(view);
    }

    public ArrayList<Action> getActions() { return new ArrayList<>(actions); }
    public void addAction(Action action) { actions.add(action); }

    public ArrayList<Output> getOutputs() {
        return new ArrayList<>(outputs);
    }
    public void addOutput(Output output) {
        outputs.add(output);
    }
}
