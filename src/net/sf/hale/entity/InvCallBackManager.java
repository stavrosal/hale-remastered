package net.sf.hale.entity;

import de.matthiasmann.twl.Button;
import net.sf.hale.Game;
import net.sf.hale.entity.Inventory.Slot;
import net.sf.hale.rules.Currency;
import net.sf.hale.rules.Merchant;
import net.sf.hale.widgets.MultipleItemPopup;
import net.sf.hale.widgets.RightClickMenu;

public class InvCallBackManager{
	private Inventory inventory;
	
	public InvCallBackManager(Inventory inv) {
		this.inventory = inv;
		
	}
	
	
	/**
	 * Returns a callback which, when run, will sell the item in the specified inventory slot
	 * to the specified merchant
	 * @param slot
	 * @param merchant
	 * @return a callback for selling an equipped item
	 */
	public Runnable getSellEquippedCallback(Inventory.Slot slot, Merchant merchant) {
		return new SellEquippedCallback(slot, merchant);
	}
	
	/**
	 * Returns a callback which, when run, will take up to the specified quantity of the item
	 * from the container and put it in this inventory.  If maxQuantity is one, then a quantity
	 * of one is taken.  If maxQuantity is greater than one, then the player selects a quantity up
	 * to maxQuantity
	 * @param item
	 * @param maxQuantity
	 * @param container
	 * @return a callback for taking an item from a container
	 */
	
	public Runnable getTakeCallback(Item item, int maxQuantity, Container container) {
		return new TakeCallback(item, maxQuantity, container);
	}
	
	/**
	 * A callback which, when run, takes all the items from the specified container and
	 * puts them in this inventory
	 * @param container the container to take the items from
	 * @return a callback for taking all items from the container
	 */
	public Runnable getTakeAllCallback(Container container) {
		return new TakeAllCallback(container);
	}
	/**
	 * Returns a callback which, when run, will buy up to the specified quantity of the item from
	 * the merchant and put the item in this inventory.  If maxQuantity is one, then a quantity
	 * of one is bought.  If maxQuantity is greater than one, then the player selects a quantity up
	 * to maxQuantity
	 * @param item
	 * @param maxQuantity
	 * @param merchant
	 * @return a callback for buying an item from a merchant
	 */
	public Runnable getBuyCallback(Item item, int maxQuantity, Merchant merchant) {
		return new BuyCallback(item, maxQuantity, merchant);
	}
	
	/**
	 * Returns a callback which, when run, will sell up to the specified quantity of the item to
	 * the merchant and take the item from this inventory.  If maxQuantity is one, then a quantity
	 * of one is sold.  If maxQuantity is greater than one, then the player selects a quantity up
	 * to maxQuantity
	 * @param item
	 * @param maxQuantity
	 * @param merchant
	 * @return a callback for selling an item to a merchant
	 */
	public Runnable getSellCallback(Item item, int maxQuantity, Merchant merchant) {
		return new SellCallback(item, maxQuantity, merchant);
	}
	
	/**
	 * Returns a callback which, when run, will drop the item in the specified inventory slot
	 * to the currently open container, or if no container is open, the ground beneath
	 * the parent creature's feet
	 * @param slot
	 * @return a drop equipped item callback
	 */
	
	public Runnable getDropEquippedCallback(Inventory.Slot slot) {
		return new DropEquippedCallback(slot);
	}
	
	/**
	 * Returns a callback which, when run, will drop up to the specified quantity of the
	 * item to the currently open container, or if no container is open, the ground beneath
	 * the parent creature's feet
	 * @param item
	 * @param maxQuantity
	 * @return a drop item callback
	 */
	
	public Runnable getDropCallback(Item item, int maxQuantity) {
		return new DropCallback(item, maxQuantity);
	}
	
	/**
	 * Returns a callback which, when run, will give up to the specified quantity of the
	 * item to the target.  If maxQuantity is one, then gives a quantity of one to the
	 * target.  If maxQuantity is greater than one, then the player selects the quantity
	 * (up to maxQuantity)
	 * @param item
	 * @param maxQuantity
	 * @param target
	 * @return a callback for giving an item
	 */
	
	public Runnable getGiveCallback(Item item, int maxQuantity, Creature target) {
		return new GiveCallback(item, maxQuantity, target);
	}
	
	/**
	 * Returns a callback that, when run, will show the menu of possible give targets.
	 * If the user selects a give target, then up to the maxQuantity of the item will
	 * move from this inventory to the target's inventory.
	 * See {@link #getGiveCallback(Item, int, Creature)}
	 * @param item
	 * @param maxQuantity
	 * @return a callback that will give up to maxQuantity of the specified Item
	 */
	
	public Runnable getGiveCallback(Item item, int maxQuantity) {
		return new ShowGiveMenuCallback(item, maxQuantity);
	}
	
	/**
	 * Returns a callback which, when run, will equip the item to the specified slot
	 * @param item
	 * @param slot the specified slot, or null for the default slot for this item
	 * @return a callback for equipping the item
	 */
	
	public Runnable getEquipCallback(EquippableItem item, Slot slot) {
		return new EquipCallback(item, slot);
	}
	
	/**
	 * Returns a callback, which, when run, will give the item in the slot specified
	 * of this inventory to the specified target creature
	 * @param slot the slot to give.  There must be an item in this slot when the callback
	 * is run
	 * @param target
	 * @return a callback for giving an equipped item
	 */
	
	public Runnable getGiveEquippedCallback(Slot slot, Creature target) {
		return new GiveEquippedCallback(slot, target);
	}
	
	/**
	 * Returns a callback which, when run, will take the specified item from the container
	 * and then equip it
	 * @param item
	 * @param container
	 * @return a callback for taking and then equipping an item
	 */
	
	public Runnable getTakeAndWieldCallback(EquippableItem item, Container container) {
		return new TakeAndWieldCallback(item, container);
	}
	
	/**
	 * Returns a callback which, when run, will unequip the item in the specified slot
	 * @param slot
	 * @return a callback for unequipping the specified item
	 */
	
	public Runnable getUnequipCallback(Slot slot) {
		return new UnequipCallback(slot);
	}
	
	private class TakeCallback extends MultipleCallback {
		private Container container;
		
		private TakeCallback(Item item, int maxQuantity, Container container) {
			super(item, maxQuantity, "Take");
			
			this.container = container;
		}
		
		@Override public void performItemAction(int quantity) {
			if (!inventory.getParent().timer.performAction(Game.ruleset.getValue("PickUpItemCost")))
				return;
			
			inventory.getUnequippedItems().add(item);
			
			
			container.getCurrentItems().remove(item, quantity);
    		if (container.getCurrentItems().size() == 0 && container.getTemplate().isTemporary()) {
    			Game.curCampaign.curArea.removeEntity(container);
    		}
    		
			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}
	
	private class TakeAllCallback implements Runnable {
		private Container container;
		
		private TakeAllCallback(Container container) {
			this.container = container;
		}
		
		@Override
		public void run() {
			ItemList list = container.getCurrentItems();
			
			for (ItemList.Entry entry : list) {
				if (!inventory.getParent().timer.performAction(Game.ruleset.getValue("PickUpItemCost")))
					return;
				inventory.getUnequippedItems().add(entry.getID(), entry.getQuality(), entry.getQuantity());
			}
			
			container.getCurrentItems().clear();
    		if (container.getTemplate().isTemporary()) {
    			Game.curCampaign.curArea.removeEntity(container);
    		}
    		
			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}
	
	private class BuyCallback extends MultipleCallback {
		private Merchant merchant;
		
		private BuyCallback(Item item, int maxQuantity, Merchant merchant) {
			super(item, maxQuantity, "Buy");
			this.merchant = merchant;
		}

		@Override public String getValueText(int quantity) {
			int percent = merchant.getCurrentSellPercentage();
			return "Price: " + Currency.getPlayerBuyCost(item, quantity, percent).shortString();
		}

		@Override public void performItemAction(int quantity) {
			merchant.sellItem(item, inventory.getParent(), quantity);
			
			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}
	
	private class SellCallback extends MultipleCallback {
		private Merchant merchant;
		
		private SellCallback(Item item, int maxQuantity, Merchant merchant) {
			super(item, maxQuantity, "Sell");
			this.merchant = merchant;
		}
		
		@Override public String getValueText(int quantity) {
			int percent = merchant.getCurrentBuyPercentage();
			return "Price: " + Currency.getPlayerSellCost(item, quantity, percent).shortString();
		}
		
		@Override public void performItemAction(int quantity) {
			merchant.buyItem(item, inventory.getParent(), quantity);
			
			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}
	
	private class SellEquippedCallback implements Runnable {
		private Slot slot;
		private Merchant merchant;
		
		private SellEquippedCallback(Slot slot, Merchant merchant) {
			this.slot = slot;
			this.merchant = merchant;
		}
		
		@Override public void run() {
			Item item = inventory.getEquippedItem(slot);
			if (item.getTemplate().isQuest()) return;
			
			inventory.unequip(slot, false);
			
			merchant.buyItem(item, inventory.getParent());
			
			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}
	
	private class DropEquippedCallback implements Runnable {
		private Slot slot;
		
		private DropEquippedCallback(Slot slot) {
			this.slot = slot;
		}
		
		@Override public void run() {
			Item item = inventory.getEquippedItems().get(slot);
			
			if (item.getTemplate().isQuest()) return;
			
			if (!inventory.getParent().timer.performAction(Game.ruleset.getValue("DropItemCost")))
				return;
			
			// if the container window is open, drop it in the container,
			// otherwise drop it at the creature's feet
			Container container = Game.mainViewer.containerWindow.getContainer();
			if (container != null)
				item.setLocation(container.getLocation());
			else
				item.setLocation(inventory.getParent().getLocation());
			
			inventory.unequip(slot, false);
			
			Game.curCampaign.curArea.addItem(item, 1);
			
			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}	
	
	private class DropCallback extends MultipleCallback {
		private DropCallback(Item item, int maxQuantity) {
			super(item, maxQuantity, "Drop");
		}
		
		@Override public void performItemAction(int quantity) {
			//quest items cannot be dropped
			if (item.getTemplate().isQuest()) return;
			
			if (!inventory.getParent().timer.performAction(Game.ruleset.getValue("DropItemCost")))
				return;
			
			// if the container window is open, drop it in the container,
			// otherwise drop it at the creature's feet
			Container container = Game.mainViewer.containerWindow.getContainer();
			if (container != null)
				item.setLocation(container.getLocation());
			else
				item.setLocation(inventory.getParent().getLocation());
			
			Game.curCampaign.curArea.addItem(item, quantity);
			
			inventory.getUnequippedItems().remove(item, quantity);
			
			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}
	
	private class GiveCallback extends MultipleCallback {
		private Creature target;
		
		private GiveCallback(Item item, int maxQuantity, Creature target) {
			super(item, maxQuantity, "Give");
			this.target = target;
		}

		@Override public void performItemAction(int quantity) {
			if (!inventory.getParent().timer.performAction(Game.ruleset.getValue("GiveItemCost")))
				return;
			
			inventory.getUnequippedItems().remove(item, quantity);
			
			target.inventory.getUnequippedItems().add(item, quantity);

			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}
	/*
	 * Shows a menu of player character give targets
	 */
	
	private class ShowGiveMenuCallback implements Runnable {
		private Item item;
		private int maxQuantity;
		
		private ShowGiveMenuCallback(Item item, int maxQuantity) {
			this.item = item;
			this.maxQuantity = maxQuantity;
		}
		
		@Override public void run() {
			RightClickMenu menu = Game.mainViewer.getMenu();
			menu.removeMenuLevelsAbove(1);
			menu.addMenuLevel("Give");
			for (PC pc : Game.curCampaign.party) {
				if (pc == inventory.getParent() || pc.isSummoned()) continue;
				
				Button button = new Button();
				button.setText(pc.getTemplate().getName());
				button.addCallback(new GiveCallback(item, maxQuantity, pc));
				
				menu.addButton(button);
			}
			
			menu.show();
		}
	}
	
	private class EquipCallback implements Runnable {
		private Slot slot;
		private EquippableItem item;
		
		private EquipCallback(EquippableItem item, Slot slot) {
			this.slot = slot;
			this.item = item;
		}
		
		@Override public void run() {
			inventory.equipItem(item, slot);
			
			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}
	
	private class GiveEquippedCallback implements Runnable {
		private Creature target;
		private Slot slot;
		
		private GiveEquippedCallback(Slot slot, Creature target) {
			this.slot = slot;
			this.target = target;
		}
		
		@Override public void run() {
			if (!inventory.getParent().timer.performAction(Game.ruleset.getValue("GiveItemCost")))
				return;
			
			EquippableItem item = inventory.getEquippedItems().get(slot);
			
			inventory.unequip(slot, false);
			
			target.inventory.getUnequippedItems().add(item);
			
			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}
	
	private class TakeAndWieldCallback implements Runnable {
		private EquippableItem item;
		private Container container;
		
		private TakeAndWieldCallback(EquippableItem item, Container container) {
			this.item = item;
			this.container = container;
		}
		
		@Override public void run() {
			if (!inventory.getParent().timer.performAction(Game.ruleset.getValue("PickUpAndWieldItemCost")))
				return;
			
			inventory.getUnequippedItems().add(item);
			
			inventory.equipItem(item, null);
			
			container.getCurrentItems().remove(item);
    		if (container.getCurrentItems().size() == 0 && container.getTemplate().isTemporary()) {
    			Game.curCampaign.curArea.removeEntity(container);
    		}
    		
			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}
	
	private class UnequipCallback implements Runnable {
		private Slot slot;
		
		private UnequipCallback(Slot slot) {
			this.slot = slot;
		}
		
		@Override public void run() {
			if ( !inventory.getParent().timer.performEquipAction(inventory.getEquippedItem(slot)) )
				return;
			
			inventory.unequip(slot, true);
			
			Game.mainViewer.getMenu().hide();
			Game.mainViewer.updateInterface();
		}
	}
	
	private abstract class MultipleCallback implements MultipleItemPopup.Callback {
			protected final Item item;
			private final int maxQuantity;
			private final String labelText;
			
			private MultipleCallback(Item item, int maxQuantity, String labelText) {
				this.item = item;
				this.maxQuantity = maxQuantity;
				this.labelText = labelText;
			}
			
			@Override public void run() {
				if (maxQuantity == 1) {
					performItemAction(1);
				} else {
					MultipleItemPopup popup = new MultipleItemPopup(Game.mainViewer);
					popup.openPopupCentered(this);
				}
			}
			
			@Override public String getLabelText() { return labelText; }
			
			@Override public int getMaximumQuantity() { return maxQuantity; }
			
			@Override public String getValueText(int quantity) { return ""; }
		}
}
