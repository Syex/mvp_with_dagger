package de.memorian.mvpdagger.data.model;

/**
 * @author Tom Seifert
 */
public final class ExampleModel {

    private String value = "";

    public ExampleModel() {
    }

    public String getValue() {
        return value;
    }

    public void append(String appendedValue) {
        value += appendedValue;
    }
}
