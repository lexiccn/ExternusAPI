package me.deltaorion.bukkit.item.custom;

import me.deltaorion.bukkit.item.predicate.EventCondition;
import me.deltaorion.bukkit.item.wrapper.CIEventWrapper;
import me.deltaorion.bukkit.item.wrapper.CustomEventWrapper;
import me.deltaorion.bukkit.item.predicate.EventPredicate;
import org.bukkit.event.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ItemEventHandler {

    /**
     * Define the priority of the event.
     * <p>
     * First priority to the last priority executed:
     * <ol>
     * <li>LOWEST
     * <li>LOW
     * <li>NORMAL
     * <li>HIGH
     * <li>HIGHEST
     * <li>MONITOR
     * </ol>
     *
     * @return the priority
     */
    EventPriority priority() default EventPriority.NORMAL;

    /**
     * Define if the handler ignores a cancelled event.
     * <p>
     * If ignoreCancelled is true and the event is cancelled, the method is
     * not called. Otherwise, the method is always called.
     *
     * @return whether cancelled events should be ignored
     */
    boolean ignoreCancelled() default false;

    /**
     * Define under what state the item should be in for the custom item for the event to be called
     * <ol>
     * <li>MAIN_HAND The item must be in the players main hand</li>
     * <li>INVENTORY The item is present anywhere in the players inventory including their armor slots and off hand. This does not
     * include the players crafting table slots</li>
     * <li>ARMOR The item is present in any of the players armor slots<li/>
     * <li>HOTBAR The item is present anywhere in the players hotbar including their main hand and off hand</li>
     * <li>OFF_HAND  The item must be present in the players off hand</li>
     * <li>ANY_HAND The item may be present in any hand</li>
     * </ol>
     *
     * The event predicate may be ignored with some CustomEventWrappers. This however will be clearly documented under
     * the CustomEventWrapper.
     *
     * See more at {@link EventPredicate}
     *
     * @return the predicate in which the item should activate
     */
    EventCondition condition() default EventCondition.MAIN_HAND;

    /**
     * How the Custom Event should be wrapped. This parameter essentially defines which entity should be extracted from an
     * event. Once the entity is extracted their inventory will be checked for the custom item.
     *
     * For example the {@link CustomEventWrapper#DAMAGE_ANOTHER_ENTITY} will get the entity who caused the damage in the
     * {@link org.bukkit.event.entity.EntityDamageByEntityEvent} whereas {@link CustomEventWrapper#GET_DAMAGED_BY_ENTITY} will
     * get the entity which was damaged.
     *
     * The default {@link CustomEventWrapper#ROOT} will attempt to grab the first event wrapper it can find. However using this
     * might cause the event to extract the wrong entity.
     *
     * See more at {@link CIEventWrapper}
     *
     * @return
     */
    CustomEventWrapper wrapper() default CustomEventWrapper.ROOT;

    /**
     * @return Whether the item activates with players only. If false then any livingentity can use the item. If true only players can
     * use this item.
     *
     */
    boolean playerOnly() default false;
}
