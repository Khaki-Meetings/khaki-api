package com.getkhaki.api.bff.web.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class UserProfileResponseDto extends PersonDto {
    private String companyName;
}
