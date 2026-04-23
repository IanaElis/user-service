package com.alex.project.controller;

import com.alex.project.dto.FieldDto;
import com.alex.project.service.api.FieldService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/internal/field")
public class FieldController {

    @Inject
    FieldService fieldService;

    @GET
    @Path("/all")
    public Response getAllFields(){
        return Response.ok(fieldService.getAllFields()).build();
    }

    @POST
    @Path("/new")
    public Response addNewField(@Valid FieldDto fieldDto){
        fieldService.addNewField(fieldDto);

        return Response.ok().build();
    }
}
