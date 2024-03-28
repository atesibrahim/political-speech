package com.fashion.digital.politicalspeech;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest
@ActiveProfiles("test")
class SpeechApplicationTests {
	@Mock
	private SpringApplication springApplication;

	@Test
	void testMain() throws Exception {
		String[] args = new String[]{"arg1", "arg2"}; // Sample command line arguments
		SpeechApplication.main(args);

		verifyNoInteractions(springApplication);
	}
}
