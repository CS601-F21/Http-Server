/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class Purpose : store the path-mapping
 */
package ServerPackage.ServerUtils.Mapping;

import ServerPackage.Handlers.Handler;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class contains the HashMap which contains the path to the handler mapping
 **/
public class PathHandlerMap {
    private HashMap<String, Handler> map;

    /**
     * Using locks to ensure that later on if the user wants to add some more mapping, they will be able to do
     * so without getting some weird error
     */
    private ReentrantReadWriteLock lock;
    private Lock readLock;
    private Lock writeLock;

    public PathHandlerMap() {
        /**
         * Constructing the HashMap and the locks
         */
        this.map = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    public void addMapping (String path, Handler object){
        /**
         * When the user wants to add a mapping we use the write lock
         */
        writeLock.lock();
        try {
            map.put(path, object);
        }finally {
            //unlocking the lock
            writeLock.unlock();
        }
    }

    public boolean contains (String key){
        /**
         * When the user wants to check if a mapping exists we use the read lock
         */
        readLock.lock();
        try {
            return map.containsKey(key);
        }finally {
            //unlocking the lock
            readLock.unlock();
        }
    }

    public Handler getObject (String path){
        /**
         * When the user wants to get the Handler for the particular path we use the read lock
         */
        readLock.lock();
        try {
            return map.get(path);
        }finally {
            //unlocking the lock
            readLock.unlock();
        }
    }
}
