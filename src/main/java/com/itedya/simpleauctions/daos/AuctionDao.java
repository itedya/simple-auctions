package com.itedya.simpleauctions.daos;

import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.dtos.BidDto;
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

    /**
     * Gets size of data array
     *
     * @return size
     */
    public static int getSize() {
        return data.size();
    }

    /**
     * Creates AuctionDTO from provided data
     *
     * @param seller        Player that is selling an item
     * @param material      Material of item
     * @param quantity      Quantity of item
     * @param startingPrice Auction starting price
     * @return AuctionDTO
     */
    public static AuctionDto create(Player seller, Material material, int quantity, int startingPrice) {
        AuctionDto auctionDto = new AuctionDto();
        auctionDto.uuid = UUID.randomUUID().toString();
        auctionDto.sellerUUID = seller.getUniqueId().toString();
        auctionDto.material = material;
        auctionDto.startingPrice = startingPrice;
        auctionDto.bids = new ArrayList<>();
        auctionDto.quantity = quantity;
        auctionDto.createdAt = new Date(Calendar.getInstance().getTime().getTime());
        auctionDto.updatedAt = auctionDto.createdAt;
        auctionDto.deletedAt = null;
        data.add(auctionDto);

        return auctionDto;
    }

    /**
     * Gets first item from data array
     *
     * @return AuctionDTO
     */
    public static @Nullable AuctionDto first() {
        try {
            return data.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Removes first item from data array
     */
    public static void removeFirst() {
        data.remove(0);
    }

    public static void addBid(BidDto bidDto) {
        AuctionDto auctionDto = first();
        auctionDto.bids.add(bidDto);
    }

    public static List<AuctionDto> all() {
        return data;
    }
}
