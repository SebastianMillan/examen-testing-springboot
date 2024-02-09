package com.salesianostriana.dam.testing.examen;

import com.salesianostriana.dam.testing.examen.dto.EditDatoMeteorologicoDto;
import com.salesianostriana.dam.testing.examen.dto.GetDatoMeteoDto;
import com.salesianostriana.dam.testing.examen.security.jwt.JwtProvider;
import com.salesianostriana.dam.testing.examen.security.user.model.User;
import com.salesianostriana.dam.testing.examen.security.user.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
//@Sql(value = "classpath:data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class IntegrationTestTemplate {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;


	private HttpHeaders adminhttpHeaders;
	private HttpHeaders userhttpHeaders;
	private EditDatoMeteorologicoDto editDatoMeteorologicoDto;

	@BeforeEach
	void setUp() {
		restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		editDatoMeteorologicoDto= new EditDatoMeteorologicoDto("Sevilla", "10/10/2022",20.3);
		User admin = User.builder()
				.id(UUID.fromString("627b819a-4cfc-4e82-b019-d28b7f04df22"))
				.username("admin")
				.password(passwordEncoder.encode("admin"))
				.roles(Set.of(UserRole.ADMIN))
				.build();
		User user = User.builder()
				.id(UUID.fromString("dd51f931-9820-41de-9ef6-0c0dcc7004fe"))
				.username("user")
				.password(passwordEncoder.encode("1234"))
				.roles(Set.of(UserRole.USER))
				.build();
		String adminToken= jwtProvider.generateToken(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword())));
		String userToken= jwtProvider.generateToken(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())));

		adminhttpHeaders= new HttpHeaders();
		adminhttpHeaders.setContentType(MediaType.APPLICATION_JSON);
		adminhttpHeaders.setBearerAuth(adminToken);
		userhttpHeaders= new HttpHeaders();
		userhttpHeaders.setContentType(MediaType.APPLICATION_JSON);
		userhttpHeaders.setBearerAuth(userToken);
	}

	@Test
	void test() {
		ResponseEntity<GetDatoMeteoDto> response = restTemplate.exchange("http://localhost:"+port+"/meteo/add", HttpMethod.POST,
				new HttpEntity<>(editDatoMeteorologicoDto,adminhttpHeaders), GetDatoMeteoDto.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
