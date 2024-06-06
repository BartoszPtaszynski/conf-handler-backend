package ConfHandler.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MetadataDto {
    private String contactEmail;
    private String contactLandlineNumber;
    private String contactCellNumber;
    private String contactWebsite;
}
