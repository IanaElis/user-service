package com.alex.project.controller.client;

import com.alex.project.dto.ModerationRequestDto;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("internal/moderationProfiles")
@RegisterRestClient(configKey = "moderation-service")
public interface ModerationServiceClient {

    @POST
    Response createProfileModerationRequest(ModerationRequestDto moderationRequestDto);
}
