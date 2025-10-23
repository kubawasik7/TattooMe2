package TattooMe.TattooMe.dto.Featured;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FeaturedDTO {
    private UUID id;
    private byte[] image;
    private boolean isFlash;
}
