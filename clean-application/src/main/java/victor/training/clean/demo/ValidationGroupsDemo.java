package victor.training.clean.demo;

import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("applications")
public class ValidationGroupsDemo {
  interface Draft {}                      // relaxat: minimul ca să poți salva
  interface Submitted extends Draft {}    // strict: moștenește regulile Draft + altele

  record ApplicationDto(
      @NotBlank(groups = Draft.class) @Size(max = 100, groups = Draft.class)
      String name,                        // obligatoriu chiar și în draft

      @Email(groups = Draft.class)        // formatul se verifică dacă e completat...
      @NotNull(groups = Submitted.class)  // ...dar e obligatoriu doar la submit
      String email,

      @Size(max = 500, groups = Draft.class)
      @NotBlank(groups = Submitted.class)
      String description,

      @Past(groups = Draft.class)
      @NotNull(groups = Submitted.class)
      LocalDate birthDate,

      @AssertTrue(groups = Submitted.class) // nu trimiți fără să accepți termenii
      boolean termsAccepted) {}

  @PutMapping("{id}/draft")   // salvezi progresul: tolerant
  void saveDraft(@PathVariable long id,
                 @RequestBody @Validated(Draft.class) ApplicationDto dto) {
    // repo.save(dto.toEntity(id, Status.DRAFT))
  }

  @PostMapping("{id}/submit") // îngheți: strict
  void submit(@PathVariable long id,
              @RequestBody @Validated(Submitted.class) ApplicationDto dto) {
    // repo.save(dto.toEntity(id, Status.SUBMITTED)); notify(...)
  }
}
