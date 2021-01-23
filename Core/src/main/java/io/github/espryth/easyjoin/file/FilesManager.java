package io.github.espryth.easyjoin.file;

import io.github.espryth.easyjoin.Core;

public class FilesManager {

    private final Core plugin;

    private Files config;
    private Files book;

    public FilesManager (Core plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        config = new Files(plugin, "config");
        book = new Files(plugin, "book");
    }

    public Files getConfig() {
        return config;
    }
    public Files getBook() {
        return book;
    }
}
