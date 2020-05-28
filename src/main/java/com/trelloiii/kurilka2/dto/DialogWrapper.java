package com.trelloiii.kurilka2.dto;

import com.fasterxml.jackson.annotation.*;
import com.trelloiii.kurilka2.model.Dialog;
import com.trelloiii.kurilka2.views.View;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonView(View.MainInfo.class)
public class DialogWrapper {
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    Dialog dialog;
}
