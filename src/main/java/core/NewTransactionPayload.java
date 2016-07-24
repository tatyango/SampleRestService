package core;

import lombok.Data;

/**
 * Created by Tetiana on 7/23/2016.
 */
@Data
public class NewTransactionPayload {

    private Long parent_id;
    private String type;
    private Double amount;

    //parent_id could be null
    public boolean isValid() {
        return type != null && !type.isEmpty()&& amount != null;
    }
}
