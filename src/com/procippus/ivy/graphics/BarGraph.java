package com.procippus.ivy.graphics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BarGraph {
    List<Bar> bars = new ArrayList<Bar>();
    int width = 200;
    int height = 150;
    int[] scale;
    Color backgroundColor = Color.WHITE;
    String title;

    public void addBar(Bar bar) {
        if (!bars.contains(bar)) {
            bars.add(bar);
        }
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int[] getScale() {
        return scale;
    }
    public void setScale(int[] scale) {
        this.scale = scale;
    }
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BarGraph other = (BarGraph) obj;
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }
}
