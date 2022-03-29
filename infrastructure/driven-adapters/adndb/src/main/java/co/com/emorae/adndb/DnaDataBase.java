package co.com.emorae.adndb;

import co.com.emorae.model.sequence.gateways.SequenceRepository;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class DnaDataBase implements SequenceRepository {

    private Map<String,Boolean> db;

    @Override
    public void addSequence(String key, Boolean value){
        db.put(key,value);
    }

    @Override
    public Boolean findSequence(String key){
        return db.get(key);
    }

    @Override
    public Map<String, Boolean> getAll() {
        return db;
    }

}
