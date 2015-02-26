package edu.washington.norimori.quizdroidv5;

import java.util.List;

/**
 * Created by midori on 2/17/15.
 * Interface simply stores elements in memory from a hard-coded list initialized on startup.
 */
public interface TopicRepository {
    public List<Topic> getEverything();
}
