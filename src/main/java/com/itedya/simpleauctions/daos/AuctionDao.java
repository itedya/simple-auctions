package com.itedya.simpleauctions.daos;

import com.itedya.simpleauctions.dtos.AuctionDto;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AuctionDao {
    private final static List<AuctionDto> data = new ArrayList();

    public static int getSize() {
        return data.size();
    }

    public static AuctionDto create(Player seller, Material material, int quantity) {
        AuctionDto auctionDto = new AuctionDto();
        auctionDto.uuid = UUID.randomUUID().toString();
        auctionDto.buyerUUID = null;
        auctionDto.sellerUUID = seller.getUniqueId().toString();
        auctionDto.material = material;
        auctionDto.quantity = quantity;
        auctionDto.createdAt = new Date(Calendar.getInstance().getTime().getTime());
        auctionDto.updatedAt = auctionDto.createdAt;
        auctionDto.deletedAt = null;
        data.add(auctionDto);

        return auctionDto;
    }

    public static @Nullable AuctionDto shift() {
        try {
            var item = data.get(0);
            data.remove(0);
            return item;
        } catch (Exception e) {
            return null;
        }
    }
}
