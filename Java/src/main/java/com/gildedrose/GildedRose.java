package com.gildedrose;

class GildedRose {

    protected Item[] items;

    protected GildedRose(Item[] items) {
        this.items = items;
    }


    /**
     * Original Flow Strategy
     */
    public void updateQuality_A() {
        for (Item item : items) {
            if (!ItemConstantsName.AGED_BRIE.equals(item.name)
                    && !ItemConstantsName.BACKSTAGE_PASSES.equals(item.name)) {
                if (item.quality > 0) {
                    if (!ItemConstantsName.SULFURAS.equals(item.name)) {
                        // "Conjured" items degrade in Quality twice as fast as normal items
                        if (item.name.contains(ItemConstantsName.CONJURED)) {
                            item.quality = item.quality - 2;
                        } else {
                            item.quality = item.quality - 1;
                        }
                    }
                }
            } else {
                if (item.quality < 50) {
                    item.quality = item.quality + 1;

                    if (ItemConstantsName.BACKSTAGE_PASSES.equals(item.name)) {
                        if (item.sellIn < 11) {
                            if (item.quality < 50) {
                                item.quality = item.quality + 1;
                            }
                        }

                        if (item.sellIn < 6) {
                            if (item.quality < 50) {
                                item.quality = item.quality + 1;
                            }
                        }
                    }
                }
            }

            if (!ItemConstantsName.SULFURAS.equals(item.name)) {
                item.sellIn = item.sellIn - 1;
            }

            if (item.sellIn < 0) {
                if (!ItemConstantsName.AGED_BRIE.equals(item.name)) {
                    if (!ItemConstantsName.BACKSTAGE_PASSES.equals(item.name)) {
                        if (item.quality > 0) {
                            if (!item.name.equals(ItemConstantsName.SULFURAS)) {
                                // "Conjured" items degrade in Quality twice as fast as normal items
                                if (item.name.contains(ItemConstantsName.CONJURED)) {
                                    item.quality = item.quality - 2;
                                } else {
                                    item.quality = item.quality - 1;
                                }
                            }
                        }
                    } else {
                        item.quality = 0;
                    }
                } else {
                    if (item.quality < 50) {
                        item.quality = item.quality + 1;
                    }
                }
            }
        }
    }

    /**
     * TODO: finish him!
     * Refactoring Quest
     */
    public void updateQuality_B() {

        for (Item item : items) {
            if (!ItemConstantsName.AGED_BRIE.equals(item.name)
                    && !ItemConstantsName.BACKSTAGE_PASSES.equals(item.name)) {
                if (item.quality > 0) {
                    if (!ItemConstantsName.SULFURAS.equals(item.name)) {
                        // "Conjured" items degrade in Quality twice as fast as normal items
                        if (item.name.contains(ItemConstantsName.CONJURED)) {
                            item.quality -= 2;
                        } else {
                            item.quality--;
                        }
                    }
                }
            } else {
                if (item.quality < 50) {
                    item.quality += 1;

                    if (ItemConstantsName.BACKSTAGE_PASSES.equals(item.name)) {
                        if (item.quality < 50) {

                            if (item.sellIn < 11) {
                                item.quality++;
                            }

                            if (item.sellIn < 6) {
                                item.quality++;
                            }
                        }
                    }
                }
            }

            if (!ItemConstantsName.SULFURAS.equals(item.name)) {
                item.sellIn--;
            }

            if (item.sellIn < 0) {
                if (!ItemConstantsName.AGED_BRIE.equals(item.name)) {
                    if (!ItemConstantsName.BACKSTAGE_PASSES.equals(item.name)) {
                        if (item.quality > 0) {
                            if (!item.name.equals(ItemConstantsName.SULFURAS)) {
                                // "Conjured" items degrade in Quality twice as fast as normal items
                                if (item.name.contains(ItemConstantsName.CONJURED)) {
                                    item.quality -= 2;
                                } else {
                                    item.quality--;
                                }
                            }
                        }
                    } else {
                        item.quality = 0;
                    }
                } else {
                    if (item.quality < 50) {
                        item.quality++;
                    }
                }
            }
        }
    }
}


