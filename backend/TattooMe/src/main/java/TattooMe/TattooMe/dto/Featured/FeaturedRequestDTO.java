package TattooMe.TattooMe.dto.Featured;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeaturedRequestDTO {
    private boolean featured;

    public boolean isFeatured() {
        return featured;
    }
}
