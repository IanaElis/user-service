package com.alex.project.utils.advice;

import com.alex.project.exceptions.service.ProfileNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionAdvice implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        if(exception instanceof ProfileNotFoundException ex){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Profile not found!")
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
