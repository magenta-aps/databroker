package dk.magenta.databroker.service.rest;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by lars on 17-12-14.
 */

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {

        Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
        if (exception instanceof NotFoundException) {
            httpStatus = Response.Status.NOT_FOUND;
        }

        return Response.status(httpStatus).entity(exception.getMessage()).build();
    }
}

