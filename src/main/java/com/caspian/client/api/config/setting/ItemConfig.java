package com.caspian.client.api.config.setting;

import com.caspian.client.api.config.Config;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class ItemConfig extends Config<Item>
{
    //
    private final Item[] values;

    /**
     * Initializes the config with a default value and allowed values array.
     *
     * @param name  The unique config identifier
     * @param desc  The config description
     * @param value The default item
     * @param values The allowed items
     * @throws NullPointerException if value is <tt>null</tt>
     */
    public ItemConfig(String name, String desc, Item value, Item[] values)
    {
        super(name, desc, value);
        this.values = values;
    }

    /**
     * Initializes the config with a default value and allows all items to be
     * values.
     *
     * @param name  The unique config identifier
     * @param desc  The config description
     * @param value The default item
     * @throws NullPointerException if value is <tt>null</tt>
     */
    public ItemConfig(String name, String desc, Item value)
    {
        this(name, desc, value, (Item[]) Registries.ITEM.stream().toArray());
    }

    /**
     * Converts all data in the object to a {@link JsonObject}.
     *
     * @return The data as a json object
     */
    @Override
    public JsonObject toJson()
    {
        return null;
    }

    /**
     * Reads all data from a {@link JsonObject} and updates the values of the
     * data in the object.
     *
     * @param jsonObj The data as a json object
     * @see #toJson()
     */
    @Override
    public void fromJson(JsonObject jsonObj)
    {

    }
}