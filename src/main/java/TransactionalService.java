import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import core.NewTransactionPayload;
import core.ResponseStatus;
import core.ServiceResponse;
import core.Transaction;
import exception.IdAlreadyExistsException;
import spark.Response;

import java.io.IOException;
import java.io.StringWriter;

import static spark.Spark.get;
import static spark.Spark.put;

/**
 * Created by Tetiana on 7/22/2016.
 */
public class TransactionalService {
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int INTERNAL_SERVER_ERROR = 500;



    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();

        } catch (IOException e) {
            throw new RuntimeException("[TransactionalService] IOException has been throwned in StringWriter");
        }
    }

    public static void main(String[] args) {
        //model
        Model model = new Model();

        put("/transactionservice/transaction/:transaction_id", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                NewTransactionPayload creation = mapper.readValue(request.body(), NewTransactionPayload.class);
                if (!creation.isValid()) {
                    response.status(HTTP_BAD_REQUEST);
                    return "";
                }
                Long id = Long.parseLong(request.params("transaction_id"));

                model.createRecord(id, creation.getType(), creation.getAmount(), creation.getParent_id());
                response.status(200);
                response.type("application/json");
                ServiceResponse answer = new ServiceResponse();
                answer.setStatus(ResponseStatus.OK);

                return new Gson().toJson(answer);
            } catch (JsonParseException jpe) {
                return prepareErrorResponse(response, HTTP_BAD_REQUEST, jpe.getMessage());
            } catch (IdAlreadyExistsException ex) {
                return prepareErrorResponse(response, HTTP_BAD_REQUEST, ex.getMessage());
            } catch (Exception ex) {
                return prepareErrorResponse(response, INTERNAL_SERVER_ERROR, "Internal server error");
            }
        });
        // get all transactions data by type
        get("/transactionservice/types/:type", (request, response) -> {
            String type = request.params(":type");
            response.status(200);
            response.type("application/json");
            return dataToJson(model.getTransactionsIdsByType(type));
        });
        // get sum of all child transactions by parent_id
        get("/transactionservice/sum/:transaction_id", (request, response) -> {
            Long transaction_id = Long.parseLong(request.params("transaction_id"));
            response.status(200);
            response.type("application/json");
            Double result = model.getSumById(transaction_id);
            if (result == null)
                return prepareErrorResponse(response, HTTP_BAD_REQUEST, "Transaction with id " + transaction_id + " has not been found");
            else
                return dataToJson(model.getSumById(transaction_id));
        });

        get("/transactionservice/transaction/:transaction_id", (request, response) -> {
            Long id;
            try {
                id = Long.parseLong(request.params("transaction_id"));
            } catch (NumberFormatException ex) {
                return prepareErrorResponse(response, HTTP_BAD_REQUEST, ex.getMessage());
            }
            Transaction transaction = model.getTransaction(id);
            if (transaction == null) {
                return prepareErrorResponse(response, HTTP_BAD_REQUEST, "core.Transaction with " + id + " not found");
            } else {
                response.status(200);
                response.type("application/json");
                return new Gson().toJson(transaction);
            }
        });
    }

    private static String prepareErrorResponse(Response response, Integer statusCode, String desciption) {
        response.status(statusCode);
        response.type("application/json");
        ServiceResponse answer = new ServiceResponse();
        answer.setStatus(ResponseStatus.ERROR);
        answer.setDescription(desciption);
        return new Gson().toJson(answer);
    }
}