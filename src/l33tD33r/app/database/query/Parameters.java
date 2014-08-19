package l33tD33r.app.database.query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simon on 2/17/14.
 */
public class Parameters {

    private Map<String,Object> nameValueMap;

    public Parameters() {
        nameValueMap = new HashMap<>();
    }

    public void setParameter(String name, Object value) {
        nameValueMap.put(name, value);
    }

    public Object getParameter(String name) {
        return nameValueMap.get(name);
    }
}
