package com.datainfosys.myshops.data;

/**
 * Created by developer on 7/1/16.
 */
public class NavigationItem {
    int id=0;
    String title= "";
    int icon= -1;
    Runnable task=null;

    public NavigationItem() {
    }

    public NavigationItem(int id, String title, int icon, Runnable task) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }
}
