package me.weckle.owncloud.scheduler;

import me.weckle.owncloud.scheduler.task.Task;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class CloudScheduler {

    protected final ConcurrentHashMap<Integer, Task> tasks;
    protected final Random random;

    public CloudScheduler() {
        this.random = new Random();
        this.tasks = new ConcurrentHashMap<>();
    }

    public int scheduleAsync(Runnable run) {
        int taskid = this.random.nextInt(Integer.MAX_VALUE);
        this.tasks.put(Integer.valueOf(taskid), new Task(run, taskid, 0L, -1L, this));
        return taskid;
    }

    public int scheduleAsyncDelay(Runnable run, long delay) {
        int taskid = this.random.nextInt(Integer.MAX_VALUE);
        this.tasks.put(Integer.valueOf(taskid), new Task(run, taskid, delay, -1L, this));
        return taskid;
    }

    public int scheduleAsyncWhile(Runnable run, long delay, long repeat) {
        int taskid = this.random.nextInt(Integer.MAX_VALUE);
        this.tasks.put(Integer.valueOf(taskid), new Task(run, taskid, delay, repeat, this));
        return taskid;
    }

    public int getCount() {
        return this.tasks.size();
    }

    public void cancelTask(int id) {
        if (this.tasks.containsKey(Integer.valueOf(id)))
        {
            ((Task)this.tasks.get(Integer.valueOf(id))).stop();
            this.tasks.remove(Integer.valueOf(id));
        }
    }

    public void cancelAllTasks() {
        for(int i : tasks.keySet()) {
            cancelTask(i);
        }
    }

    public ConcurrentHashMap<Integer, Task> getTasks() {
        return tasks;
    }


}
