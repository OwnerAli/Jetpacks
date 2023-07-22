package me.ogali.jetpacks.registries;

import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JetpackRegistry {

    private final Map<String, AbstractJetpack> abstractJetpackMap = new HashMap<>();

    public void registerJetpack(AbstractJetpack abstractJetpack) {
        abstractJetpackMap.put(abstractJetpack.getId(), abstractJetpack);
    }

    public void unregisterJetpackById(String id) {
        abstractJetpackMap.remove(id);
    }

    public Optional<AbstractJetpack> getJetpackById(String id) {
        return Optional.ofNullable(abstractJetpackMap.get(id));
    }

    public boolean isJetpackId(String id) {
        return abstractJetpackMap.containsKey(id);
    }

}
