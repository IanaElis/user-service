package com.alex.project.controller;

import com.alex.project.dto.ModerationRequestDto;
import com.alex.project.dto.ProfileDto;
import com.alex.project.dto.ProfileRegistrationDto;
import com.alex.project.dto.SearchUser;
import com.alex.project.service.ProfileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("internal/profiles")
@ApplicationScoped
public class ProfileController {

    @Inject
    ProfileService profileService;

    @POST
    public Response createProfile(ProfileRegistrationDto request) {

        profileService.createProfile(request);

        return Response.ok().build();
    }

    @POST
    @Path("/get")
    public Response getProfile(SearchUser email) {
        return Response.ok(profileService.getAlumni(email.getEmail())).build();
    }

    @POST
    @Path("/update")
    public Response updateProfile(ProfileDto request, @HeaderParam("X-USER-EMAIL") String userEmail) {
        profileService.updateProfile(request, userEmail);

        return Response.ok().build();
    }

    @POST
    @Path("/accept")
    public Response acceptProfileChanges(ProfileDto request) {
        profileService.acceptChanges(request);

        return Response.ok().build();
    }

}
