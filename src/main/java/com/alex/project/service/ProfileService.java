package com.alex.project.service;

import com.alex.project.entity.OperationType;
import com.alex.project.controller.ModerationServiceClient;
import com.alex.project.dto.ModerationRequestDto;
import com.alex.project.dto.ProfileDto;
import com.alex.project.dto.ProfileRegistrationDto;
import com.alex.project.entity.Alumni;
import com.alex.project.repository.ProfileRepository;
import com.alex.project.utils.ProfileMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.postgresql.shaded.com.ongres.stringprep.Profile;

import java.util.Optional;

@ApplicationScoped
public class ProfileService {

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
    }

    @Transactional
    public Alumni getAlumni(String email) {
        return profileRepository.findByEmail(email.trim()).orElseThrow(() -> new RuntimeException("No profile found with email " + email));
    }

    public void updateProfile(ProfileDto profileDto, String email) {
        ModerationRequestDto dto = new ModerationRequestDto();

        Alumni alumni = profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No profile found with email " + email));

        System.out.println(alumni.getEmail());

        changeVerificationState(alumni);

        ProfileDto oldState = new ProfileDto();

        profileMapper.updateProfileDtoFromAlunmi(alumni, oldState);

        System.out.println("Old profile state "  + oldState.toString());

        dto.setTargetType("PROFILE");
        dto.setChanges(profileDto);
        dto.setOldState(oldState);
        dto.setOperationType(OperationType.UPDATING);

        moderationServiceClient.createProfileModerationRequest(dto);
    }

    @Transactional
    public void changeVerificationState(Alumni alumni) {
        alumni.setVerified(!alumni.isVerified());
        profileRepository.persist(alumni);
    }

    public void acceptChanges(ProfileDto profileDto) {
        ProfileDto dto = new ProfileDto();
        Optional<Alumni> alumni = profileRepository.findByName(profileDto.getName(), profileDto.getSurname());
        profileMapper.updateProfileFromDto(dto, alumni.orElse(null));
        System.out.println("New profile state " + alumni.toString());

        alumni.ifPresent(this::changeVerificationState);

        profileRepository.persist(alumni.orElse(null));
    }
}
