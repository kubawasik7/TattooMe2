package TattooMe.TattooMe.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class TattooStudioArtistId implements Serializable {
    private UUID tattooStudio;
    private UUID user;
}
