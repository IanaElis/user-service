package com.alex.project.controller;

import com.alex.project.dto.FieldDto;
import com.alex.project.dto.SpecialtyDto;
import com.alex.project.service.api.FieldService;
import com.alex.project.service.api.SpecialityService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("internal/specialty")
public class SpecialityController {

    @Inject
    SpecialityService specialityService;

    @GET
    @Path("/all")
    public Response getAllSpecialty(){
        return Response.ok(specialityService.getAllSpecialty()).build();
    }

    @POST
    @Path("/new")
    public Response addNewSpecialty(SpecialtyDto specialtyDto){
        specialityService.addNewSpeciality(specialtyDto);

        return Response.ok().build();
    }
}
