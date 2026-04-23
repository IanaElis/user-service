package com.alex.project.controller;

import com.alex.project.dto.ModerationRequestDto;
import com.alex.project.dto.ProfileDto;
import com.alex.project.dto.ProfileRegistrationDto;
import com.alex.project.dto.SearchUser;
import com.alex.project.service.ProfileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("internal/profiles")
@ApplicationScoped
public class ProfileController {

    @Inject
    ProfileService profileService;

    @POST
    public Response createProfile(@Valid ProfileRegistrationDto request) {

        profileService.createProfile(request);

        return Response.ok().build();
    }

    @POST
    @Path("/get")
    public Response getProfile(@Valid SearchUser email) {
        return Response.ok(profileService.getAlumni(email.email())).build();
    }

    @POST
    @Path("/update")
    public Response updateProfile(@Valid ProfileDto request, @HeaderParam("X-USER-EMAIL") @NotBlank String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            throw new BadRequestException("Missing or empty X-USER-EMAIL header");
        }

        profileService.updateProfile(request, userEmail);

        return Response.ok().build();
    }

    @POST
    @Path("/accept")
    public Response acceptProfileChanges(@Valid ProfileDto request) {
        profileService.acceptChanges(request);

        return Response.ok().build();
    }

    @POST
    @Path("/reject")
    public Response rejectProfileChanges(@Valid SearchUser dto){
        profileService.rejectChanges(dto);

        return Response.ok().build();
    }

}
