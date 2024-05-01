package project.module.example.data.dto.user;

import lombok.Getter;
import lombok.Setter;
import project.module.example.config.validator.ValidDateRange;
import project.module.example.data.object.DateRange;

@Getter
@Setter
public class SearchUserDTO {

    @ValidDateRange
    private DateRange birthDateRange;

}
