package com.alex.project.utils;

import com.alex.project.dto.ProfileDto;
import com.alex.project.entity.Alumni;
import com.alex.project.entity.Field;
import org.hibernate.dialect.unique.AlterTableUniqueDelegate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "JAKARTA", uses = {SpecialtyMapperHelper.class,
        FiledMapperHelper.class})
public interface ProfileMapper {
        ProfileDto toDto(Alumni profile);
        @BeanMapping(nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE)
        void updateProfileFromDto(ProfileDto profileChangesDto, @MappingTarget Alumni profileDto);
        @BeanMapping(nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE)
        void updateProfileDtoFromAlunmi(Alumni profileDto, @MappingTarget ProfileDto profileDto1);
}
