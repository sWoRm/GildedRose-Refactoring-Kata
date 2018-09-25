package com.gildedrose;

import static org.junit.Assert.*;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.util.function.Function;

public class GildedRoseTest {


    /**
     * Helper method in order to build an expected array of <Items>
     *
     * @return items array
     */
    private Item[] generateBasicItems() {
        return new Item[]{
                new Item("+5 Dexterity Vest", 10, 20), //
                new Item("Aged Brie", 2, 0), //
                new Item("Elixir of the Mongoose", 5, 7), //
                new Item("Sulfuras, Hand of Ragnaros", 0, 80), //
                new Item("Sulfuras, Hand of Ragnaros", -1, 80),
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
                new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),

                // Coverage purpose only special cases
                new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20),
                new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20),

                // this conjured item does not work properly yet
                new Item("Conjured Mana Cake", 3, 6)};
    }

    /**
     * Helper Interface created to execute the chosen update quality method
     */
    private interface UpdateQualityExecutor {
        void execute(GildedRose gildedRose);
    }

    /**
     * Original Test flow
     */
    @Test
    public void updateQuality_BasicMonthFlowValidation_A() {
        updateQuality_BasicMonthFlowValidation(30, new Function<GildedRose, Void>() {
            @Override
            public Void apply(GildedRose gildedRose) {
                gildedRose.updateQuality_A();
                return null;
            }
        });
    }


    @Test
    public void updateQuality_BasicMonthFlowValidation_B() {
        updateQuality_BasicMonthFlowValidation(30, new Function<GildedRose, Void>() {
            @Override
            public Void apply(GildedRose gildedRose) {
                gildedRose.updateQuality_B();
                return null;
            }
        });
    }


    /**
     * Validate the correct Flow execution
     * @param days numbers of day to execute
     * @param updateQualityExecutor {@link Function} external helper executor strategy
     */
    private void updateQuality_BasicMonthFlowValidation(int days, Function<GildedRose, Void> updateQualityExecutor) {
        Item[] actual_items = generateBasicItems();

        GildedRose app = new GildedRose(SerializationUtils.clone(actual_items));

        // Simulate 1 Month (~30 days)
        for (int day = 1; day <= days; day++) {

            // Launch Daily items review
            updateQualityExecutor.apply(app);

            // Item arrays size must match!
            assertEquals(actual_items.length, app.items.length);

            // Compare Items separately
            for (int i = 0; i < actual_items.length; i++) {

                final Item actual_item = actual_items[i];
                final Item expected_item = app.items[i];
                final String actual_item_name = actual_item.name;

                // Coverage Force check
                final String testText = actual_item.toString();

                //  - Once the sell by date has passed, Quality degrades twice as fast
                if (!ItemConstantsName.SULFURAS.equals(actual_item_name)
                        && actual_item.sellIn <= 0) {
                    if (ItemConstantsName.AGED_BRIE.equals(actual_item_name)) {
                        assertEquals(expected_item.quality, Math.min(actual_item.quality + 2, 50));

                    } else if (ItemConstantsName.BACKSTAGE_PASSES.equals(actual_item_name)
                            && actual_item.sellIn <= 0) {
                        // Quality drops to 0 after the concert
                        assertEquals(expected_item.quality, 0);

                    } else {
                        assertEquals(expected_item.quality, Math.max(0, actual_item.quality - 2));
                    }

                }

                //  - The Quality of an item is never negative
                assertTrue(actual_item.quality >= 0);
                assertTrue(expected_item.quality >= 0);


                //  - "Aged Brie" actually increases in Quality the older it gets
                if (ItemConstantsName.AGED_BRIE.equals(actual_item_name)) {

                    if (actual_item.sellIn > expected_item.sellIn) {

                        // - The Quality of an item is never more than 50
                        if (actual_item.quality >= 50) {
                            assertEquals(expected_item.quality, 50);

                        } else {
                            assertTrue(actual_item.quality < expected_item.quality);
                        }
                    }
                }

                //  - "Sulfuras", being a legendary item, never has to be sold or decreases in Quality
                if (ItemConstantsName.SULFURAS.equals(actual_item_name)) {
                    // however "Sulfuras" is a legendary item and as such its Quality is 80 and it never alters
                    assertEquals(actual_item.quality, 80);
                    assertEquals(expected_item.quality, 80);

                } else {
                    //  - The Quality of an item is never more than 50
                    //    Just for clarification, an item can never have its Quality increase above 50, however "Sulfuras" is a
                    //    legendary item and as such its Quality is 80 and it never alters
                    assertTrue(actual_item.quality <= 50);
                    assertTrue(expected_item.quality <= 50);
                }

                //  - "Backstage passes", like aged brie, increases in Quality as its SellIn value approaches;
                if (ItemConstantsName.BACKSTAGE_PASSES.equals(actual_item_name)) {
                    //  Quality increases by 2 when there are 10 days or less and by 3 when there are 5 days or less but
                    //  Quality drops to 0 after the concert
                    if (actual_item.sellIn <= 0) {
                        assertEquals(expected_item.quality, 0);

                    } else if (actual_item.sellIn <= 5) {
                        assertEquals(expected_item.quality, Math.min(actual_item.quality + 3, 50));

                    } else if (actual_item.sellIn <= 10) {
                        assertEquals(expected_item.quality, Math.min(actual_item.quality + 2, 50));
                    }
                }

                //  We have recently signed a supplier of conjured items. This requires an update to our system:
                //  - "Conjured" items degrade in Quality twice as fast as normal items
                if (ItemConstantsName.CONJURED.equals(actual_item_name)) {
                    assertEquals(expected_item.quality, actual_item.quality - 2);
                }
            }
            // replace old value with the new one
            actual_items = SerializationUtils.clone(app.items);
        }
    }
}
