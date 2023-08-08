package me.ogali.jetpacks.files.domain;

import de.leonhard.storage.Json;

public class JsonFile extends Json {

    public JsonFile(String fileName) {
        super(fileName, "plugins/Jetpacks");
    }

}