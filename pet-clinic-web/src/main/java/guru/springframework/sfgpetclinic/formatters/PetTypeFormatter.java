package guru.springframework.sfgpetclinic.formatters;

import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Locale;

@Service
public class PetTypeFormatter implements Formatter<PetType> {
    private final PetTypeService petTypeService;

    public PetTypeFormatter(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    @Override
    public PetType parse(String id, Locale locale) throws ParseException {
        return petTypeService.findById(Long.valueOf(id));
    }

    @Override
    public String print(PetType object, Locale locale) {
        return object.getId() != null ? object.getId().toString() : "";
    }
}
