package com.getkhaki.api.bff.web.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class OrganizersStatisticsResponseDto {
    int page;
    List<OrganizerStatisticsResponseDto> organizersStatistics;
}
