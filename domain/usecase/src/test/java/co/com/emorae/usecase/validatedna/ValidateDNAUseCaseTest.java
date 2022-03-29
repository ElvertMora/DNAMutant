package co.com.emorae.usecase.validatedna;

import co.com.emorae.model.sequence.Sequence;

import co.com.emorae.model.sequence.gateways.SequenceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.RuntimeErrorException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ValidateDNAUseCaseTest {

    @InjectMocks
    private ValidateDNAUseCase useCase;

    @Mock
    private SequenceRepository dnaDB;

    @Test
    public void isMutant2DiagonalSequences() {
        when(dnaDB.findSequence(anyString())).thenReturn(null);
        boolean isMutant = useCase.isMutant(Sequence.builder().dna(Arrays.asList(
                "ATGCGA",
                "CAGTAC",
                "TTATGT",
                "AGTAGG",
                "CTACTA",
                "TCACTG")).build());
        assertTrue(isMutant);
    }

    @Test
    public void isMutant2HorizontalSequences() {
        when(dnaDB.findSequence(anyString())).thenReturn(null);
        boolean isMutant = useCase.isMutant(Sequence.builder().dna(Arrays.asList(
                "CTGCGA",
                "CAGTCC",
                "TTATGT",
                "AAAAGG",
                "CCCCTA",
                "TCACTG")).build());
        assertTrue(isMutant);
    }

    @Test
    public void isMutant2VerticalSequences() {
        when(dnaDB.findSequence(anyString())).thenReturn(null);
        boolean isMutant = useCase.isMutant(Sequence.builder().dna(Arrays.asList(
                "CTGCGA",
                "CAGTGC",
                "ATATGT",
                "AGAAGG",
                "ACCCTA",
                "ACACTG")).build());
        assertTrue(isMutant);
    }

    @Test
    public void notIsMutant() {
        when(dnaDB.findSequence(anyString())).thenReturn(null);
        boolean isMutant = useCase.isMutant(Sequence.builder().dna(Arrays.asList(
                "CTGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "ACCCTA",
                "TCACTG")).build());
        Assertions.assertFalse(isMutant);
    }
    @Test
    public void isMutant() {
        when(dnaDB.findSequence(anyString())).thenReturn(null);
        boolean isMutant = useCase.isMutant(Sequence.builder().dna(Arrays.asList(
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG")).build());
        assertTrue(isMutant);
        verify(dnaDB).findSequence(anyString());
    }

    @Test
    public void validateNxNSequence() {
        RuntimeErrorException exception = assertThrows(RuntimeErrorException.class, () -> {
            useCase.isMutant(Sequence.builder().dna(Arrays.asList(
                            "ATGCGAA",
                            "CAGTGC",
                            "TTATGT",
                            "AGAAGG",
                            "CCCCTA",
                            "TCACTG")).build());
                });
        String expectedMessage = "Bad Request";
        String actualMessage = exception.getTargetError().getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void validateCorrectCharacterInSequence() {
        RuntimeErrorException exception = assertThrows(RuntimeErrorException.class, () -> {
            useCase.isMutant(Sequence.builder().dna(Arrays.asList(
                    "ATGCGR",
                    "CAGTGC",
                    "TTATGT",
                    "AGAAGG",
                    "CCCCTA",
                    "TCACTG")).build());
        });
        String expectedMessage = "Bad Request";
        String actualMessage = exception.getTargetError().getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getResponseIsMutantFromDB() {
        when(dnaDB.findSequence(anyString())).thenReturn(true);
        boolean isMutant = useCase.isMutant(Sequence.builder().dna(Arrays.asList(
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG")).build());
        assertTrue(isMutant);
        verify(dnaDB).findSequence(anyString());
    }

    @Test
    public void getStats() {
        Map<String,Boolean> counts = new HashMap<>();
        counts.put("test1",false);
        counts.put("test2",true);
        when(dnaDB.getAll()).thenReturn(counts);
        Map<String,Object> stats = useCase.getStats();
        assertTrue(stats.containsKey( "count_human_dna"));
        assertTrue(stats.containsKey( "count_mutant_dna"));
        assertTrue(stats.containsKey( "ratio"));
        assertEquals(1L,stats.get("count_human_dna"));
        assertEquals(1L,stats.get("count_mutant_dna"));
        assertEquals(0.5,stats.get("ratio"));
    }
}