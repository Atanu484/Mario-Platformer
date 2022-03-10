package com.nortal.platformer;
public class Platform {

    private Integer index;
    private Integer cost;

    public Platform(Integer index, Integer cost) {
        this.index = index;
        this.cost = cost;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Platform{" +
                "index=" + index +
                ", cost=" + cost +
                '}';
    }
}
