package ru.ifmo.web.service.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IdNotFoundExceptionMapper implements ExceptionMapper<IdNotFoundException> {
    @Override
    public Response toResponse(IdNotFoundException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getReason()).build();
    }
}
