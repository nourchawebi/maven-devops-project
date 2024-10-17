package tn.esprit.tpfoyer;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;
import tn.esprit.tpfoyer.service.EtudiantServiceImpl;

import java.util.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)

public class EtudiantServiceImplTest {
    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;


    private Etudiant etudiant;
    private List<Etudiant> listEtudiants;


    @BeforeEach
    public void setUp() {
        etudiant = new Etudiant(1L, "Nourch", "Chaouebich", 112345678L, new Date(), null);
       listEtudiants = new ArrayList<>(
                Arrays.asList(
                        new Etudiant(1L, "Nour", "Chaouebi", 12345678L, new Date(), null),
                        new Etudiant(2L, "asma", "Cchaouebi", 22334455L, new Date(), null),
                        new Etudiant(3L, "Amina", "Ben Ali", 99887766L, new Date(), null),
                        new Etudiant(4L, "Omar", "Jlassi", 55443322L, new Date(), null)
                )
        );



    }
    @Test
    public void testRetrieveAllEtudiants() {
        when(etudiantRepository.findAll()).thenReturn(listEtudiants);
        List<Etudiant> result = etudiantService.retrieveAllEtudiants();
        assertEquals(4, result.size());
        assertEquals("Nour", result.get(0).getNomEtudiant());
    }

    @Test
    public void testRetrieveEtudiant() {
        when(etudiantRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(etudiant));
        Etudiant result = etudiantService.retrieveEtudiant(1L);
        Assertions.assertNotNull(result);

    }

    @Test
    public void testAddEtudiant() {
        when(etudiantRepository.save(etudiant)).thenAnswer(invocation -> {
            Etudiant savedEtudiant = invocation.getArgument(0);
            listEtudiants.add(etudiant);  // Add to the list when saved
            return savedEtudiant;
        });

        // Call the service to add the student
        Etudiant result = etudiantService.addEtudiant(etudiant);

        // Verify that the student was added to the list
        assertNotNull(result);

        assertEquals(5, listEtudiants.size());  // Ensure list size is 1
    }


    @Test
    public void testModifyEtudiant() {
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);
        Etudiant result = etudiantService.modifyEtudiant(etudiant);
        assertEquals(112345678L, result.getCinEtudiant());
    }

    @Test
    public void testRemoveEtudiant() {
        etudiantService.removeEtudiant(1L);
        Mockito.verify(etudiantRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testRecupererEtudiantParCin() {
        when(etudiantRepository.findEtudiantByCinEtudiant(12345678L))
                .thenReturn(etudiant);
        Etudiant result = etudiantService.recupererEtudiantParCin(12345678L);
        assertNotNull(result);
        assertEquals("Nour", result.getNomEtudiant());
    }
}


