package org.chou.project.fuegobase.utils;

import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HashIdUtil {

    @Value("${salt}")
    private String salt;

    public Hashids getHashids() {
        return new Hashids(salt, 8);
    }

    public String encoded(Long id) {
        Hashids hashids = getHashids();
        return hashids.encode(id);
    }

    public long decoded(String hId) {
        Hashids hashids = getHashids();
        if (hashids.decode(hId).length > 0) {
            return hashids.decode(hId)[0];
        } else {
            throw new IllegalArgumentException("ID not exist.");
        }
    }
}


