package com.getkhaki.api.bff.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrailingStatisticsResponseDto {
    List<TimeBlockSummaryResponseDto> timeBlockSummaries;
}
