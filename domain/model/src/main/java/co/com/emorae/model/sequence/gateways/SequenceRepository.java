package co.com.emorae.model.sequence.gateways;

import java.util.Map;

public interface SequenceRepository {

    void addSequence(String key, Boolean value);

    Boolean findSequence(String key);

    Map<String,Boolean> getAll();
}
