package me.ogali.jetpacks.fuels.domain;

public abstract class AbstractFuel {

    private final String id;

    protected AbstractFuel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
