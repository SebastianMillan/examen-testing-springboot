package com.salesianostriana.dam.testing.examen;

import com.salesianostriana.dam.testing.examen.repo.DatoMeteorologicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Sql(value = "classpath:insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class RepositoryTestTemplate {

	@Autowired
	private DatoMeteorologicoRepository datoMeteorologicoRepository;

	@Container
	@ServiceConnection
	static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"))
			.withUsername("testUser")
			.withPassword("testSecret")
			.withDatabaseName("testDatabase");

	@Test
	void whenDateAndCityIsOk_thenReturnTrue() {
		LocalDate fecha = LocalDate.of(2020,10,10);
		String ciudad = "Sevilla";
		boolean result= datoMeteorologicoRepository.existePorFechaPoblacion(fecha, ciudad);
        assertTrue(result);
	}
	@Test
	void whenDateNotExists_thenReturnTrue() {
		LocalDate fecha = LocalDate.of(2022,10,10);
		String ciudad = "Sevilla";
		boolean result= datoMeteorologicoRepository.existePorFechaPoblacion(fecha, ciudad);
        assertFalse(result);
	}
	@Test
	void whenCityNotExists_thenReturnTrue() {
		LocalDate fecha = LocalDate.of(2020,10,10);
		String ciudad = "Cuenca";
		boolean result= datoMeteorologicoRepository.existePorFechaPoblacion(fecha, ciudad);
        assertFalse(result);
	}

}
