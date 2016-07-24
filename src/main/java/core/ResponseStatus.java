package core;

/**
 * Created by Tetiana on 7/23/2016.
 */
public enum ResponseStatus {
    OK("ok"),
    ERROR("error");
    private final String text;

    /**
     * @param text status text description
     */
    ResponseStatus(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
