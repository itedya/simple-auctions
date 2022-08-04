package com.itedya.simpleauctions.dtos;

import org.bukkit.Material;

import java.sql.Date;

public class AuctionDto implements Dto {
    public String uuid;
    public String sellerUUID;
    public String buyerUUID;
    public Material material;
    public int startingPrice;
    public int quantity;
    public Date createdAt;
    public Date updatedAt;
    public Date deletedAt;
}
