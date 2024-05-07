package org.chou.project.fuegobase.utils;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
        return hashids.decode(hId)[0];
    }
}


