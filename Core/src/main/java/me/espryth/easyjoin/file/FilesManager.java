package me.espryth.easyjoin.file;

import me.espryth.easyjoin.Core;

public class FilesManager {

    private final Core core;

    private Files config;
    private Files book;

    public FilesManager (Core core) {
        this.core = core;
    }

    public void setup() {
        config = new Files(core, "config");
        book = new Files(core, "book");
    }

    public Files getConfig() {
        return config;
    }
    public Files getBook() {
        return book;
    }
}
