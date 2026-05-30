package net.nodus.core.site.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSiteRequest {

    private String name;
    private String domain;
    private String url;

}
