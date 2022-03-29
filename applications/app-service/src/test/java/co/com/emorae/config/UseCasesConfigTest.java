package co.com.emorae.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UseCasesConfigTest {

    @InjectMocks
    private UseCasesConfig config;

    @Test
    public void useCaseConfigTest() {
        assertNotNull(config);
    }

}