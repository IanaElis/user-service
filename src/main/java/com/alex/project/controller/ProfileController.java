package com.alex.project.controller;

import com.alex.project.controller.client.FriendServiceApiClient;
import com.alex.project.dto.ProfileDto;
import com.alex.project.dto.ProfileRegistrationDto;
import com.alex.project.dto.SearchUser;
import com.alex.project.service.ProfileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("internal/profiles")
@ApplicationScoped
public class ProfileController {

    @Inject
    ProfileService profileService;

    @POST
    @Path("/create")
    public Response createProfile(ProfileRegistrationDto request) {

        profileService.createProfile(request);
        return Response.ok().build();
    }

    @GET
    @Path("/get") // TODO: maybe change to a similar structure like below
    public Response getProfile(SearchUser email) {
        return Response.ok(profileService.getAlumniByEmail(email.getEmail())).build();
    }

    @GET
    @Path("/get/id")
    public Response getProfile(Long id) {
        return Response.ok(profileService.getAlumniById(id)).build();
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
