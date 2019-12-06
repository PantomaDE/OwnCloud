package me.weckle.owncloud.scheduler.task;

import me.weckle.owncloud.scheduler.CloudScheduler;

public class Task extends Thread {

    private Runnable task;
    private int taskId;
    private long delay;
    private long repeat;
    private CloudScheduler scheduler;

    public Runnable getTask() {
        return task;
    }

    public int getTaskId() {
        return taskId;
    }

    public long getDelay() {
        return delay;
    }

    public long getRepeat() {
        return repeat;
    }

    public Task(Runnable run, int id, long delay, long repeat, CloudScheduler scheduler) {
        this.task = run;
        this.taskId = id;
        this.delay = delay;
        this.repeat = repeat;
        this.scheduler = scheduler;
        start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.delay);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (this.repeat != -1L) {
            this.task.run();
            try {
                Thread.sleep(this.repeat);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.repeat == -1L) {
            this.task.run();
        }
        this.scheduler.getTasks().remove(Integer.valueOf(this.taskId));
        this.scheduler.cancelTask(this.taskId);
    }

}
