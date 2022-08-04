package com.itedya.simpleauctions.dtos;

import org.bukkit.Material;

import java.sql.Date;
import java.util.List;

public class AuctionDto implements Dto {
    public String uuid;
    public String sellerUUID;
    public Material material;
    public int startingPrice;
    public List<BidDto> bids;
    public int quantity;
    public Date createdAt;
    public Date updatedAt;
    public Date deletedAt;

    public BidDto getHighestBid() {
        int highestBid = -1;
        int highestBidIndex = -1;

        for (int i = 0; i < bids.size(); i++) {
            BidDto bid = bids.get(i);

            if (bid.price > highestBid) {
                highestBid = bid.price;
                highestBidIndex = i;
            }
        }

        if (highestBid == -1) {
            return null;
        }

        return bids.get(highestBidIndex);
    }
}
