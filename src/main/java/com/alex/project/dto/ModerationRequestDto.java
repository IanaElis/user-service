package com.alex.project.dto;

import com.alex.project.entity.enums.OperationType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

public class ModerationRequestDto {
    @NotBlank
    private String targetType;

    @NotBlank
    private ProfileDto changes;
    private OperationType operationType;
    private ProfileDto oldState;

    public ModerationRequestDto(String targetType, ProfileDto changes, OperationType operationType, ProfileDto oldState) {
        this.targetType = targetType;
        this.changes = changes;
        this.operationType = operationType;
        this.oldState = oldState;
    }

    public ModerationRequestDto() {
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public ProfileDto getChanges() {
        return changes;
    }

    public void setChanges(ProfileDto changes) {
        this.changes = changes;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public ProfileDto getOldState() {
        return oldState;
    }

    public void setOldState(ProfileDto oldState) {
        this.oldState = oldState;
    }
}
