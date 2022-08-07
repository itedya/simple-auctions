package com.itedya.simpleauctions.daos;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.itedya.simpleauctions.SimpleAuctions;
import com.itedya.simpleauctions.dtos.ItemPersistenceDto;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPersistenceDao {
    private static Map<String, List<ItemPersistenceDto>> data = new HashMap<>();

    public static void removeByUUID(String uuid) {
        data.remove(uuid);
    }

    public static List<ItemPersistenceDto> getByUUID(String uuid) {
        var result = data.get(uuid);

        if (result == null) {
            return new ArrayList<>();
        }

        return result;
    }

    public static void addItem(String uuid, ItemPersistenceDto dto) {
        List<ItemPersistenceDto> dtos = getByUUID(uuid);

        if (dtos == null) {
            dtos = new ArrayList<>();
        }

        dtos.add(dto);

        data.put(uuid, dtos);
    }

    public static void read() {
        try {
            Gson gson = new Gson();
            SimpleAuctions plugin = SimpleAuctions.getInstance();

            File readFrom = new File(plugin.getDataFolder(), "item-persistence.json");
            if (!readFrom.exists()) {
                return;
            }

            FileReader fr = new FileReader(readFrom);
            Type type = new TypeToken<Map<String, List<ItemPersistenceDto>>>() {
            }.getType();
            data = gson.fromJson(fr, type);
            fr.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Gson gson = new Gson();
            SimpleAuctions plugin = SimpleAuctions.getInstance();

            File saveTo = new File(plugin.getDataFolder(), "item-persistence.json");
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }

            FileWriter fw = new FileWriter(saveTo, false);
            Type type = new TypeToken<Map<String, List<ItemPersistenceDto>>>() {
            }.getType();
            gson.toJson(data, type, fw);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
