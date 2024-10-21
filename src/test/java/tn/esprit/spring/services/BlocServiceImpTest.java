package tn.esprit.spring.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.services.entity.Bloc;
import tn.esprit.spring.services.repository.BlocRepository;
import tn.esprit.spring.services.service.BlocServiceImpl;
import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class BlocServiceImpTest {

    @Mock
    BlocRepository blocRepository;
    @InjectMocks
    BlocServiceImpl blocService;
    Bloc bloc1 = new Bloc(1L, "Bloc A", 100L, null, null);

    List<Bloc> blocList = new ArrayList<Bloc>()
    {
        {
            add(new Bloc(3L, "Bloc C", 500L, null, null));
            add(new Bloc(4L, "Bloc D", 200L, null, null));
        }
    };

    @Test
    @Order(1)
    public void retriveBlocTest (){
        Mockito.when(blocRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bloc1));
        Bloc result = blocService.retrieveBloc(1L);
        Assertions.assertNotNull(result);
    }

    @Test
    @Order(2)
    public void retrieveAllBlocsTest() {
        Mockito.when(blocRepository.findAll()).thenReturn(blocList);

        List<Bloc> result = blocService.retrieveAllBlocs();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

    }
    @Test
    @Order(3)
    public void removeBlocTest() {
        Mockito.doNothing().when(blocRepository).deleteById(1L);

        blocService.removeBloc(1L);
    }
    @Test
    @Order(4)
    public void testRetrieveBlocsSelonCapacite() {
        Mockito.when(blocRepository.findAll()).thenReturn(blocList);

        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(300L);


        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Bloc C", result.get(0).getNomBloc());
    }
}
