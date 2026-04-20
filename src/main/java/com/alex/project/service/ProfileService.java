package com.alex.project.service;

import com.alex.project.entity.enums.OperationType;
import com.alex.project.controller.ModerationServiceClient;
import com.alex.project.dto.ModerationRequestDto;
import com.alex.project.dto.ProfileDto;
import com.alex.project.dto.ProfileRegistrationDto;
import com.alex.project.entity.Alumni;
import com.alex.project.exceptions.service.ModerationRequestException;
import com.alex.project.exceptions.service.ProfileNotFoundException;
import com.alex.project.exceptions.service.VerificationException;
import com.alex.project.repository.ProfileRepository;
import com.alex.project.utils.ProfileMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ProfileService {

    private static final Logger log = LoggerFactory.getLogger(ProfileService.class);

    @Inject
    ProfileRepository profileRepository;

    @RestClient
    ModerationServiceClient moderationServiceClient;

    @Inject
    ProfileMapper profileMapper;

    @Transactional
    public void createProfile(ProfileRegistrationDto profileRegistrationDto) {
        Alumni alumni = new Alumni();

        alumni.setEmail(profileRegistrationDto.getEmail());
        alumni.setId(profileRegistrationDto.getId());

        profileRepository.persist(alumni);

        log.info("Alumni with {} created", profileRegistrationDto.getEmail());
    }

    @Transactional
    public Alumni getAlumni(String email) {
        return profileRepository.findByEmail(email.trim()).orElseThrow(() -> new ProfileNotFoundException("No profile found with email " + email));
    }

    @Transactional
    public void updateProfile(ProfileDto profileDto, String email) {
        ModerationRequestDto dto = new ModerationRequestDto();

        Alumni alumni = profileRepository.findByEmail(email)
                .orElseThrow(() -> new ProfileNotFoundException("No profile found with email: " + email));

        if(!alumni.isVerified()){
            throw new VerificationException("Your account is under verification process. Please" +
                    "try again later");
        }

        changeVerificationState(alumni);

        ProfileDto oldState = new ProfileDto();

        profileMapper.updateProfileDtoFromAlunmi(alumni, oldState);

        profileDto.setEmail(email);

        dto.setTargetType("PROFILE");
        dto.setChanges(profileDto);
        dto.setOldState(oldState);
        dto.setOperationType(OperationType.UPDATING);

        Response resp = moderationServiceClient.createProfileModerationRequest(dto);

        if(resp.getStatus() > 300){
            throw new ModerationRequestException("Moderation request creating error!");
        }
    }

    @Transactional
    public void changeVerificationState(Alumni alumni) {
        alumni.setVerified(!alumni.isVerified());
        profileRepository.persist(alumni);
    }

    @Transactional
    public void acceptChanges(ProfileDto profileDto) {
        Alumni alumni = profileRepository.findByEmail(profileDto.getEmail())
                .orElseThrow(() -> new ProfileNotFoundException("No profile found with email: " + profileDto.getEmail()));
        profileMapper.updateProfileFromDto(profileDto, alumni);

        changeVerificationState(alumni);

        profileRepository.persist(alumni);
    }

    public void rejectChanges(ProfileDto dto){
        Alumni alumni = profileRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ProfileNotFoundException("No profile found with email: " + dto.getEmail()));

        changeVerificationState(alumni);

        profileRepository.persist(alumni);
    }
}
