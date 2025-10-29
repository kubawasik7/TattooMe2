package TattooMe.TattooMe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "work_hour_studio")
@Getter
@Setter
@NoArgsConstructor
public class WorkHourStudio {
    @EmbeddedId
    private WorkHourStudioId id;

    @MapsId("tattooStudioId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tattoo_studio_id", insertable = false, updatable = false)
    private TattooStudio studio;

    @MapsId("workHourId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_hour_id", insertable = false, updatable = false)
    private WorkHour workHour;

}

