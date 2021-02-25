package me.espryth.easyjoin;

import me.espryth.easyjoin.file.FilesManager;
import me.espryth.easyjoin.format.Format;
import me.espryth.easyjoin.service.MainService;
import me.espryth.easyjoin.service.Service;
import me.espryth.easyjoin.util.nms.NMSSetup;
import me.espryth.easyjoin.util.nms.SimpleNMSSetup;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Core extends JavaPlugin {

    private Map<String, Format> formatMap;

    private NMSSetup nmsSetup;
    private FilesManager filesManager;

    private Service mainService;

    @Override
    public void onLoad() {
        this.formatMap = new HashMap<>();
        this.nmsSetup = new SimpleNMSSetup();
        this.filesManager = new FilesManager(this);
        this.mainService = new MainService(this);
    }

    @Override
    public void onEnable() {
        this.filesManager.setup();
        this.nmsSetup.enableNMS();
        this.mainService.start();
    }

    public NMSSetup getNmsSetup() {
        return nmsSetup;
    }

    public FilesManager getFilesManager() {
        return filesManager;
    }

    public Map<String, Format> getFormatMap() {
        return formatMap;
    }
}
