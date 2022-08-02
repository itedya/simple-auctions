package com.itedya.simpleauctions.dtos;

import java.sql.Date;

public class AuctionDto implements Dto {
    public String uuid;
    public String sellerUUID;
    public String buyerUUID;
    public Date createdAt;
    public Date updatedAt;
    public Date deletedAt;
}
