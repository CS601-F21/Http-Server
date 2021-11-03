package ServerPackage.ServerUtils.Mapping;

import ServerPackage.Handlers.Handler;

import java.util.HashMap;

//class will contain the HashMap which contains the path to the handler mapping
public class PathHandlerMap {
    private HashMap<String, Handler> map;

    public PathHandlerMap() {
        this.map = new HashMap<>();
    }

    public void addMapping (String path, Handler object){
        map.put(path, object);
    }

    public boolean contains (String key){
        return map.containsKey(key);
    }

    public Handler getObject (String path){
        return map.get(path);
    }
}
