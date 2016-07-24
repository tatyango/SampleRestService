package core;

/**
 * Created by Tetiana on 7/23/2016.
 */

public class ServiceResponse {

    private ResponseStatus status;
    private String description;

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
