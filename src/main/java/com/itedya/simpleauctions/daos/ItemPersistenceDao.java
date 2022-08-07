package com.itedya.simpleauctions.daos;

import com.itedya.simpleauctions.dtos.ItemPersistenceDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPersistenceDao {
    private static Map<String, List<ItemPersistenceDto>> data = new HashMap<>();

    public static List<ItemPersistenceDto> getByUUID(String uuid) {
        return data.get(uuid);
    }

    public static void addItem(String uuid, ItemPersistenceDto dto) {
        List<ItemPersistenceDto> dtos = getByUUID(uuid);

        if (dtos == null) {
            dtos = new ArrayList<>();
        }

        dtos.add(dto);

        data.put(uuid, dtos);
    }
}
