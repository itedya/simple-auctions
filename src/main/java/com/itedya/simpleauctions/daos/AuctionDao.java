package com.itedya.simpleauctions.daos;

import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.dtos.BidDto;
import com.itedya.simpleauctions.runnables.*;
import com.itedya.simpleauctions.utils.ThreadUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AuctionDao {
    private static AuctionDao instance;

    public static AuctionDao getInstance() {
        if (instance == null) instance = new AuctionDao();
        return instance;
    }

    private final int taskId;

    private AuctionDao() {
        taskId = ThreadUtil.syncRepeat(this::removeOneSecond, 20);
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public void removeOneSecond() {
        if (this.active == null) {
            if (queue.size() == 0) {
                return;
            } else {
                active = this.firstInQueue();
                this.removeFirstInQueue();
            }
        }

        if (active.ttl == 90 || active.ttl == 60 || active.ttl == 30) {
            ThreadUtil.sync(new AnnounceItemRunnable(active));
        } else if (active.ttl == 15 || active.ttl == 10 || (active.ttl >= 1 && active.ttl <= 5)) {
            ThreadUtil.sync(new AnnounceEndSecondsOfAuctionRunnable(active.ttl));
        } else if (active.ttl == 0) {
            if (active.bids.size() == 0) {
                ThreadUtil.sync(new EndOfNotSoldAuctionRunnable(active));
            } else {
                ThreadUtil.sync(new EndOfSoldAuctionRunnable(active));
            }

            active = null;
            return;
        }

        active.ttl -= 1;
    }

    private final List<AuctionDto> queue = new ArrayList();
    private AuctionDto active = null;

    /**
     * Creates AuctionDTO from provided data
     *
     * @param seller        Player that is selling an item
     * @param material      Material of item
     * @param quantity      Quantity of item
     * @param startingPrice Auction starting price
     * @return AuctionDTO
     */
    public AuctionDto create(Player seller, Material material, int quantity, int startingPrice) {
        AuctionDto auctionDto = new AuctionDto();
        auctionDto.uuid = UUID.randomUUID().toString();
        auctionDto.sellerUUID = seller.getUniqueId().toString();
        auctionDto.material = material;
        auctionDto.startingPrice = startingPrice;
        auctionDto.bids = new ArrayList<>();
        auctionDto.quantity = quantity;
        auctionDto.ttl = 90;
        auctionDto.createdAt = new Date(Calendar.getInstance().getTime().getTime());
        auctionDto.updatedAt = auctionDto.createdAt;
        auctionDto.deletedAt = null;

        if (active == null) active = auctionDto;
        else queue.add(auctionDto);

        return auctionDto;
    }

    public int getQueueSize() {
        return queue.size();
    }

    public void removeFirstInQueue() {
        queue.remove(0);
    }

    public AuctionDto firstInQueue() {
        return queue.get(0);
    }

    public AuctionDto getActive() {
        return active;
    }

    public void addBid(BidDto bidDto) {
        active.bids.add(bidDto);
    }

    public void addTTL(int sec) {
        active.ttl += sec;
    }

    public List<AuctionDto> allInQueue() {
        return queue;
    }
}
