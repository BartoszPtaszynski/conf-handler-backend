package ConfHandler.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MenuDto {
    private String header;
    private List<String> name;
}
