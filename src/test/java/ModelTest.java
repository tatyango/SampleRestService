import core.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tetiana on 7/24/2016.
 */
public class ModelTest {

    /* predefined types */
    private static final String CARS = "cars";
    private static final String SHOPPING = "shopping";
    /* test model */
    private Model testModel = new Model();


    @Before
    public void setUp() {
        testModel.createRecord(1L, CARS, 1000.0, null);
        testModel.createRecord(2L, CARS, 1000.0, 1L);
        testModel.createRecord(3L, CARS, 2000.0, 2L);
        testModel.createRecord(4L, CARS, 1000.0, 2L);
        testModel.createRecord(5L, SHOPPING, 1000.0, 2L);
        testModel.createRecord(6L, SHOPPING, 1000.0, 2L);
    }

    @Test
    public void testGetSumById() {
        Long parentId = 1L;
        Double expectedSum = 7000.0;
        Double result;
        //when
        result = testModel.getSumById(1L);
        //then
        assertEquals(expectedSum,result);
    }

    @Test
    public void testCreateRecord() {
        Long id = 777L;
        Long parentId = 777L;
        String type =SHOPPING;
        Double amount = 7000.0;
        Transaction result;
        //when
        testModel.createRecord(id,type , amount, parentId);
        result=testModel.getTransaction(id);
        //then
        assertNotNull(result);
        assertEquals(id,result.getId());
        assertEquals(type,result.getType());
        assertEquals(amount,result.getAmount());
        assertEquals(parentId,result.getParentId());
    }


    @Test
    public void testGetTransactionsIdsByType() {

        String type = CARS;
        int count = 4;
        List<Long> expected = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));
        List<Long> result;
        //when
        result=testModel.getTransactionsIdsByType(type);
        //then
        assertNotNull(result);
        assertEquals(count,result.size());
        assertTrue(expected.equals(result));
    }


}
