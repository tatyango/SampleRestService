package core;

import lombok.Data;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Tetiana on 7/22/2016.
 */
@Data
public class Transaction {

    private Long id;
    private Long parentId;
    private String type;
    private Double amount;

    private transient Set<Long> childIds = new TreeSet<>();

}
