package TattooMe.TattooMe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkHourStudioId implements Serializable {
    @Column(name = "tattoo_studio_id")
    private UUID tattooStudioId;

    @Column(name = "work_hour_id")
    private UUID workHourId;
}
