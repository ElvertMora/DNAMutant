package co.com.emorae.usecase.validatedna;

import co.com.emorae.model.sequence.Sequence;
import co.com.emorae.model.sequence.gateways.SequenceRepository;
import lombok.AllArgsConstructor;

import javax.management.RuntimeErrorException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@AllArgsConstructor
public class ValidateDNAUseCase {

    private static final String[] MUTANT_SEQUENCES = {"TTTT", "GGGG", "AAAA", "CCCC"};
    private static final String REGEX_INVALID_CHARS = "^[TGAC]+$";
    private SequenceRepository dnaDB;

    public Boolean isMutant(Sequence sequence) {
        if (!validate(sequence.getDna())) {
            throw new RuntimeErrorException(new Error("Bad Request"));
        }
        String key = getMD5(sequence.getDna());
        Boolean value = dnaDB.findSequence(key);
        if (value != null) {
            return value;
        }
        Boolean isMutant = verticalValidation(sequence.getDna()) + horizontalValidation(sequence.getDna()) + diagonalValidation(sequence.getDna()) > 1;
        dnaDB.addSequence(key, isMutant);
        return isMutant;
    }

    private Integer diagonalValidation(final List<String> dna) {
        int size = dna.size();
        int minDiag = -size + 1;
        int diagSize = 2 * (size * 2 - 1);
        int halfDiag = size * 2 - 1;
        List<StringBuilder> diag = Stream.generate(StringBuilder::new)
                .limit(diagSize)
                .collect(Collectors.toList());

        IntStream.range(0, size).forEach(x ->
                IntStream.range(0, size).forEach(y -> {
                    char base = dna.get(y).charAt(x);
                    diag.get(x + y).append(base);
                    diag.get(halfDiag + x - y - minDiag).append(base);
                }));

        return diag.parallelStream().filter(s -> s.length() > 3).map(StringBuilder::toString)
                .map(this::totalMutantGenInSequence).reduce(0, Integer::sum);
    }

    private Integer horizontalValidation(List<String> dna) {
        return dna.parallelStream().map(this::totalMutantGenInSequence).reduce(0, Integer::sum);
    }

    private Integer verticalValidation(List<String> dna) {
        return IntStream.range(0, dna.size()).mapToObj(
                        idx -> dna.stream().reduce("", (p, s) -> p + s.charAt(idx)))
                .parallel().map(this::totalMutantGenInSequence).reduce(0, Integer::sum);
    }

    private Integer totalMutantGenInSequence(final String sequence) {
        return Stream.of(MUTANT_SEQUENCES).parallel()
                .map(mutantString -> sequence.contains(mutantString) ? 1 : 0)
                .reduce(0, Integer::sum);
    }

    private String getMD5(List<String> dna) {
        String completeSequence = String.join("", dna);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(completeSequence.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            return completeSequence;
        }
    }

    private boolean validate(List<String> dna) {
        return dna.parallelStream().noneMatch(sequence -> !sequence.matches(REGEX_INVALID_CHARS) || dna.size() != sequence.length());
    }

    public Map<String ,Object> getStats(){
       Map<Boolean,Long> count = dnaDB.getAll().values().stream()
               .collect(Collectors.groupingBy(Boolean::booleanValue,Collectors.counting()));
       Long totalMutant = count.get(true)==null?0:count.get(true);
       Long totalHuman = count.get(false)==null?0:count.get(false);
       Double ratio = Double.valueOf(totalMutant) / Double.valueOf(totalMutant + totalHuman);
       Map<String,Object> result = new HashMap<>();
       result.put("count_mutant_dna",totalMutant);
       result.put("count_human_dna",totalHuman);
       result.put("ratio",ratio);
       return result;
    }
}
