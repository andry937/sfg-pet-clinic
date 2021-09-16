package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VisitControllerTest {
    @Mock
    VisitService visitService;
    @Mock
    PetService petService;

    MockMvc mockMvc;
    VisitController visitController;
    private static final Long OWNER_ID = 1L;
    private static final Long PET_ID = 1L;
    private static final Long VISIT_ID = 1L;

    private static final String OWNER_URL = String.format("/owners/%d",OWNER_ID, PET_ID);
    private static final String PET_URL = OWNER_URL+"/pets/" + PET_ID;
    private static final String VISIT_URL = PET_URL +"/visits";
    private static final String VISIT_NEW_URL = VISIT_URL+"/new";
    private static final String VISIT_EDIT_URL = VISIT_URL+"/"+VISIT_ID+"/edit";

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
        visitController = new VisitController(visitService, petService);
        mockMvc = MockMvcBuilders.standaloneSetup(visitController).build();
    }

    @Test
    void testInitCreateVisitForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VISIT_NEW_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("visit"))
                .andExpect(view().name(VisitController.VIEW_VISIT_FORM));
    }

    @Test
    void testInitUpdateVisitForm() throws Exception {
        Mockito.when(visitService.findById(Mockito.anyLong())).thenReturn(Visit.builder().id(VISIT_ID).build());
        mockMvc.perform(MockMvcRequestBuilders.get(VISIT_EDIT_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute("visit", hasProperty("id",is(VISIT_ID))))
                .andExpect(view().name(VisitController.VIEW_VISIT_FORM));
    }

    @Test
    void testPerformCreateVisit() throws Exception {
        Mockito.when(visitService.save(Mockito.any(Visit.class)))
                .thenReturn(Visit.builder()
                        .pet(Pet.builder().id(PET_ID).owner(Owner.builder().id(OWNER_ID).build()).build())
                        .build());
        mockMvc.perform(MockMvcRequestBuilders.post(VISIT_NEW_URL)
                .param("date","2021-09-09")
                .param("description", "description"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"+PET_URL));
    }



    @Test
    void testPerformUpdateVisit() throws Exception {
        Mockito.when(visitService.save(Mockito.any(Visit.class)))
                .thenReturn(Visit.builder()
                        .pet(Pet.builder().id(PET_ID).owner(Owner.builder().id(OWNER_ID).build()).build())
                        .build());
        mockMvc.perform(MockMvcRequestBuilders.post(VISIT_EDIT_URL)
                        .param("date","2021-09-09")
                        .param("description", "description"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"+PET_URL));
    }
}