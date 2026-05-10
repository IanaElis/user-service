package com.alex.project.controller.client;

import com.alex.project.dto.ProfileDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "friend-service")
public interface FriendServiceApiClient {

    @POST
    @Path("/save-or-update")
    boolean saveOrUpdateUser(ProfileDto request);

}