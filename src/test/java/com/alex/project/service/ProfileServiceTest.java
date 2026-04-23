package com.alex.project.service;

import com.alex.project.controller.ModerationServiceClient;
import com.alex.project.dto.ModerationRequestDto;
import com.alex.project.dto.ProfileDto;
import com.alex.project.dto.ProfileRegistrationDto;
import com.alex.project.dto.SearchUser;
import com.alex.project.entity.Alumni;
import com.alex.project.entity.enums.OperationType;
import com.alex.project.exceptions.service.ModerationRequestException;
import com.alex.project.exceptions.service.ProfileNotFoundException;
import com.alex.project.exceptions.service.VerificationException;
import com.alex.project.repository.ProfileRepository;
import com.alex.project.utils.ProfileMapper;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.wildfly.common.Assert.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    ProfileRepository profileRepository;

    @Mock
    ModerationServiceClient moderationServiceClient;

    @Mock
    ProfileMapper profileMapper;

    @InjectMocks
    ProfileService profileService;

    private ProfileRegistrationDto registrationDto;
    private ProfileDto profileDto;
    private Alumni alumni;
    private SearchUser searchUser;

    @BeforeEach
    void setUp() {
        registrationDto = new ProfileRegistrationDto();
        registrationDto.setEmail("test@example.com");
        registrationDto.setId(1L);

        profileDto = new ProfileDto();
        profileDto.setEmail("test@example.com");

        alumni = new Alumni();
        alumni.setId(1L);
        alumni.setEmail("test@example.com");
        alumni.setVerified(true);

        searchUser = new SearchUser("test@example.com");
    }

    @Test
    void createProfile_shouldPersistNewAlumni() {
        profileService.createProfile(registrationDto);

        ArgumentCaptor<Alumni> captor = ArgumentCaptor.forClass(Alumni.class);
        verify(profileRepository).persist(captor.capture());

        Alumni savedAlumni = captor.getValue();
        assertNotNull(savedAlumni);
        assertEquals(registrationDto.getEmail(), savedAlumni.getEmail());
        assertEquals(registrationDto.getId(), savedAlumni.getId());
    }

    @Test
    void getAlumni_shouldReturnAlumni_whenFound() {
        when(profileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(alumni));

        Alumni result = profileService.getAlumni("test@example.com");

        assertEquals(alumni, result);
        verify(profileRepository).findByEmail("test@example.com");
    }

    @Test
    void getAlumni_shouldTrimEmailBeforeSearch() {
        when(profileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(alumni));

        Alumni result = profileService.getAlumni("  test@example.com  ");


        assertEquals(alumni, result);
        verify(profileRepository).findByEmail("test@example.com");
    }

    @Test
    void getAlumni_shouldThrowProfileNotFoundException_whenNotFound() {
        when(profileRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        ProfileNotFoundException exception = assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.getAlumni("missing@example.com")
        );

        assertEquals("No profile found with email missing@example.com", exception.getMessage());
        verify(profileRepository).findByEmail("missing@example.com");
    }

    @Test
    void updateProfile_shouldThrowProfileNotFoundException_whenAlumniNotFound() {
        when(profileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        ProfileNotFoundException exception = assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.updateProfile(profileDto, "test@example.com")
        );

        assertEquals("No profile found with email: test@example.com", exception.getMessage());

        verify(profileRepository).findByEmail("test@example.com");
        verifyNoInteractions(moderationServiceClient);
        verify(profileMapper, never()).updateProfileDtoFromAlunmi(any(), any());
    }

    @Test
    void updateProfile_shouldThrowVerificationException_whenAlumniIsNotVerified() {
        alumni.setVerified(false);

        when(profileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(alumni));

        VerificationException exception = assertThrows(
                VerificationException.class,
                () -> profileService.updateProfile(profileDto, "test@example.com")
        );

        assertEquals(
                "Your account is under verification process. Please try again later",
                exception.getMessage()
        );

        verify(profileRepository).findByEmail("test@example.com");
        verifyNoInteractions(moderationServiceClient);
        verify(profileMapper, never()).updateProfileDtoFromAlunmi(any(), any());
        assertFalse(alumni.isVerified());
    }

    @Test
    void updateProfile_shouldSendModerationRequest_whenInputIsValid() {
        when(profileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(alumni));
        when(moderationServiceClient.createProfileModerationRequest(any(ModerationRequestDto.class)))
                .thenReturn(Response.status(200).build());

        profileService.updateProfile(profileDto, "test@example.com");

        verify(profileRepository).findByEmail("test@example.com");
        verify(profileMapper).updateProfileDtoFromAlunmi(eq(alumni), any(ProfileDto.class));
        verify(moderationServiceClient).createProfileModerationRequest(any(ModerationRequestDto.class));

        assertEquals("test@example.com", profileDto.getEmail());
        assertFalse(alumni.isVerified());
        verify(profileRepository, never()).persist(Collections.singleton(any()));
    }

    @Test
    void updateProfile_shouldBuildCorrectModerationRequest() {
        when(profileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(alumni));
        when(moderationServiceClient.createProfileModerationRequest(any(ModerationRequestDto.class)))
                .thenReturn(Response.status(200).build());

        profileService.updateProfile(profileDto, "test@example.com");

        ArgumentCaptor<ModerationRequestDto> captor = ArgumentCaptor.forClass(ModerationRequestDto.class);
        verify(moderationServiceClient).createProfileModerationRequest(captor.capture());

        ModerationRequestDto request = captor.getValue();

        assertNotNull(request);
        assertEquals("PROFILE", request.getTargetType());
        assertEquals(OperationType.UPDATING, request.getOperationType());
        assertEquals(profileDto, request.getChanges());
        assertNotNull(request.getOldState());
    }

    @Test
    void updateProfile_shouldThrowModerationRequestException_whenModerationServiceReturnsErrorStatus() {
        when(profileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(alumni));
        when(moderationServiceClient.createProfileModerationRequest(any(ModerationRequestDto.class)))
                .thenReturn(Response.status(500).build());

        ModerationRequestException exception = assertThrows(
                ModerationRequestException.class,
                () -> profileService.updateProfile(profileDto, "test@example.com")
        );

        assertEquals("Moderation request creating error!", exception.getMessage());

        verify(profileRepository).findByEmail("test@example.com");
        verify(profileMapper).updateProfileDtoFromAlunmi(eq(alumni), any(ProfileDto.class));
        verify(moderationServiceClient).createProfileModerationRequest(any(ModerationRequestDto.class));
        verify(profileRepository, never()).persist(Collections.singleton(any()));
        assertFalse(alumni.isVerified());
    }

    @Test
    void changeVerificationState_shouldChangeVerifiedFromTrueToFalse() {
        alumni.setVerified(true);

        profileService.changeVerificationState(alumni);

        assertFalse(alumni.isVerified());
        verify(profileRepository, never()).persist(Collections.singleton(any()));
    }

    @Test
    void changeVerificationState_shouldChangeVerifiedFromFalseToTrue() {
        alumni.setVerified(false);

        profileService.changeVerificationState(alumni);

        assertTrue(alumni.isVerified());
        verify(profileRepository, never()).persist(Collections.singleton(any()));
    }

    @Test
    void acceptChanges_shouldThrowProfileNotFoundException_whenAlumniNotFound() {
        when(profileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        ProfileNotFoundException exception = assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.acceptChanges(profileDto)
        );

        assertEquals("No profile found with email: test@example.com", exception.getMessage());

        verify(profileRepository).findByEmail("test@example.com");
        verify(profileMapper, never()).updateProfileFromDto(any(), any());
        verify(profileRepository, never()).persist(Collections.singleton(any()));
    }

    @Test
    void acceptChanges_shouldUpdateAlumniToggleVerificationAndPersist_whenAlumniExists() {
        when(profileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(alumni));

        profileService.acceptChanges(profileDto);

        verify(profileRepository).findByEmail("test@example.com");
        verify(profileMapper).updateProfileFromDto(profileDto, alumni);
        verify(profileRepository).persist(alumni);

        assertFalse(alumni.isVerified());
    }

    @Test
    void rejectChanges_shouldThrowProfileNotFoundException_whenAlumniNotFound() {
        when(profileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        ProfileNotFoundException exception = assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.rejectChanges(searchUser)
        );

        assertEquals("No profile found with email: test@example.com", exception.getMessage());

        verify(profileRepository).findByEmail("test@example.com");
        verify(profileRepository, never()).persist(Collections.singleton(any()));
    }

    @Test
    void rejectChanges_shouldToggleVerificationAndPersist_whenAlumniExists() {
        when(profileRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(alumni));

        profileService.rejectChanges(searchUser);

        verify(profileRepository).findByEmail("test@example.com");
        verify(profileRepository).persist(alumni);
        verifyNoInteractions(profileMapper);

        assertFalse(alumni.isVerified());
    }
}
