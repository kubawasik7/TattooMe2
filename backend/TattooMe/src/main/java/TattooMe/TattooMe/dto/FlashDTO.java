package TattooMe.TattooMe.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashDTO {
    @NotNull(message = "Minimalny rozmiar jest wymagany")
    @Min(value = 1, message = "Minimalny rozmiar musi być co najmniej 1")
    @Max(value = 200, message = "Maksymalny rozmiar to 200")
    private Integer sizeMin;

    @NotNull(message = "Maksymalny rozmiar jest wymagany")
    @Min(value = 1, message = "Maksymalny rozmiar musi być co najmniej 1")
    @Max(value = 200, message = "Maksymalny rozmiar to 200")
    private Integer sizeMax;

    @NotNull(message = "Minimalna cena jest wymagana")
    @Min(value = 1, message = "Minimalna cena musi być co najmniej 1")
    @Max(value = 100000, message = "Maksymalna cena to 100000")
    private Integer priceMin;

    @NotNull(message = "Maksymalna cena jest wymagana")
    @Min(value = 1, message = "Maksymalna cena musi być co najmniej 1")
    @Max(value = 100000, message = "Maksymalna cena to 100000")
    private Integer priceMax;

    @Size(max = 60, message = "Pole  może mieć maksymalnie 60 znaków")
    private String reccomendedPlace;

    @Size(max = 100, message = "Opis może mieć maksymalnie 100 znaków")
    private String description;

    private String picture;

}