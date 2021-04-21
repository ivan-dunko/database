package ru.nsu;

public class IdName {
    private final String name;
    private final int id;

    public IdName(int id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString(){
        return name;
    }
}
