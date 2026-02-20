package net.alex9849.arm.minifeatures.selloffer;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OfferListener implements Listener {

    private Player seller;
    private Player buyer;
    private Offer offer;
    private WrappedTask timertask;

    protected OfferListener(Player seller, Player buyer, Offer offer) {
        this.seller = seller;
        this.buyer = buyer;
        this.offer = offer;
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        if (event.getPlayer().getUniqueId() == buyer.getUniqueId() || event.getPlayer().getUniqueId() == seller.getUniqueId()) {
            buyer.sendMessage(offer.replaceVariables(Messages.OFFER_HAS_BEEN_CANCELLED));
            offer.reject();
        }
    }

    protected void unregister() {
        PlayerQuitEvent.getHandlerList().unregister(this);
        if (this.timertask != null && !this.timertask.isCancelled()) {
            this.timertask.cancel();
        }
    }

    protected void activateCancelTimer(int ticks) {
        this.timertask = AdvancedRegionMarket.getInstance().getScheduler().runLater(new Runnable() {
            @Override
            public void run() {
                seller.sendMessage(Messages.PREFIX + offer.replaceVariables(Messages.OFFER_TIMED_OUT));
                buyer.sendMessage(Messages.PREFIX + offer.replaceVariables(Messages.OFFER_TIMED_OUT));
                offer.reject();
            }
        }, ticks);
    }
}
