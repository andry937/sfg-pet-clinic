package guru.springframework.sfgpetclinic.bootstrap;

import guru.springframework.sfgpetclinic.model.*;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import guru.springframework.sfgpetclinic.services.SpecialityService;
import guru.springframework.sfgpetclinic.services.VetService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {
    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeService petTypeService;
    private final SpecialityService specialityService;

    public DataLoader(OwnerService ownerService, VetService vetService, PetTypeService petTypeService, SpecialityService specialityService) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeService = petTypeService;
        this.specialityService = specialityService;
    }

    @Override
    public void run(String... args) throws Exception {
        int count = vetService.findAll().size();
        if(count == 0){
            loadData();
        }
    }

    private void loadData() {
        PetType dog = new PetType();
        dog.setName("Dog");
        petTypeService.save(dog);
        PetType cat = new PetType();
        cat.setName("Cat");
        petTypeService.save(dog);

        Owner owner1 = new Owner();
        owner1.setLastName("Michael");
        owner1.setFirstName("Weston");
        owner1.setAddress("123 Brickerel");
        owner1.setCity("Miami");
        owner1.setTelephone("123456789");
        ownerService.save(owner1);

        Pet mikesPet = new Pet();
        mikesPet.setName("Rosco");
        mikesPet.setOwner(owner1);
        mikesPet.setBirthDate(LocalDate.now());
        mikesPet.setPetType(dog);
        owner1.getPets().add(mikesPet);

        Owner owner2 = new Owner();
        owner2.setLastName("Fionna");
        owner2.setFirstName("Glenanne");
        owner2.setAddress("123 Brickerel");
        owner2.setCity("Miami");
        owner2.setTelephone("123456789");
        ownerService.save(owner2);

        Pet fionnaCat = new Pet();
        fionnaCat.setName("Bianca");
        fionnaCat.setOwner(owner2);
        fionnaCat.setBirthDate(LocalDate.now());
        fionnaCat.setPetType(cat);
        owner2.getPets().add(fionnaCat);

        System.out.println("Loaded Owners...");

        Speciality radiology = new Speciality();
        radiology.setName("radiology");
        specialityService.save(radiology);
        Speciality surgery = new Speciality();
        surgery.setName("surgery");
        specialityService.save(surgery);
        Speciality dentistry = new Speciality();
        dentistry.setName("dentistry");
        specialityService.save(dentistry);

        Vet vet1 = new Vet();
        vet1.setLastName("Sam");
        vet1.setFirstName("Axe");
        vet1.getSpecialities().add(radiology);
        vetService.save(vet1);

        Vet vet2 = new Vet();
        vet2.setLastName("Jessica");
        vet2.setFirstName("Potter");
        vet2.getSpecialities().add(dentistry);
        vet2.getSpecialities().add(surgery);
        vetService.save(vet2);

        System.out.println("Loaded vets...");
    }
}
