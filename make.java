package org.ncsecu.aem.site.core.models;

public class TrueCarMake {

    private final String title;
    private final String value;

    public TrueCarMake(final String title, final String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}