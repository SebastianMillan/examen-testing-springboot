package com.salesianostriana.dam.testing.examen.service;

import com.salesianostriana.dam.testing.examen.model.DatoMeteorologico;
import com.salesianostriana.dam.testing.examen.model.DatoMeterologicoPK;
import com.salesianostriana.dam.testing.examen.repo.DatoMeteorologicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.salesianostriana.dam.testing.examen.Utils.fechaAMes;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class ServicioMeteorologicoTest {

    @InjectMocks
    private ServicioMeteorologico servicioMeteorologico;

    @Mock
    private DatoMeteorologicoRepository datoMeteorologicoRepository;

    private String poblacion;
    private List<DatoMeteorologico> filtradosPorPoblacion;

    @BeforeEach
    void setUp(){
        poblacion="Bollullos de la mitacion";
        filtradosPorPoblacion= List.of(
                DatoMeteorologico.builder()
                        .id(new DatoMeterologicoPK(poblacion, LocalDate.of(2022,10,10)))
                        .precipitacion(20.5)
                        .build(),
                DatoMeteorologico.builder()
                        .id(new DatoMeterologicoPK(poblacion, LocalDate.of(2021,5,21)))
                        .precipitacion(10)
                        .build(),
                DatoMeteorologico.builder()
                        .id(new DatoMeterologicoPK(poblacion, LocalDate.of(2021,5,21)))
                        .precipitacion(5)
                        .build());
    }
    @Test
    void whenPoblacionExistAndPrecipitacionExists_thenReturnOk() {
        Mockito.when(datoMeteorologicoRepository.buscarPorPoblacion(poblacion)).thenReturn(filtradosPorPoblacion);
        Map<String, Double> result= servicioMeteorologico.mediaMensual(poblacion);
        Map<String, Double> expectedResult= Map.of(fechaAMes(LocalDate.of(2022,10,10)),20.5,"MAYO",7.5);
        assertNotNull(result);
        assertEquals(result, expectedResult);
        assertTrue(result.containsKey("OCTUBRE"));
        assertEquals(result.get("OCTUBRE"), expectedResult.get("OCTUBRE"));
        Mockito.verify(datoMeteorologicoRepository, times(1)).buscarPorPoblacion(poblacion);
    }
    @Disabled
    @Test
    void whenPoblacionIsEmpty_thenReturnException() {
        Mockito.when(datoMeteorologicoRepository.buscarPorPoblacion(poblacion)).thenReturn(List.of());
        //Devuelve exepcion
    }

}