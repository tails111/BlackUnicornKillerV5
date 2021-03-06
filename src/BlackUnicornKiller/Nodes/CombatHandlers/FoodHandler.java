package BlackUnicornKiller.Nodes.CombatHandlers;
import BlackUnicornKiller.BlackUnicornKiller;

import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.script.methods.Hud;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Actor;
import org.powerbot.script.wrappers.Item;

public class FoodHandler extends Job {

    public FoodHandler (MethodContext ctx){
        super(ctx);
    }

    Actor me;
    Item food;

    public Item nilItem = ctx.backpack.getNil();

    public int getHpPercent() {
        return Math.abs(100 - 100 * ctx.widgets.get(748, 5).getHeight() / 28);
    }

    private Item checkItem(Item tItem){
        if(tItem==null){
            return ctx.backpack.getNil();
        }
        return tItem;
    }

    public boolean emergencyTeleport(){
        Actor me = ctx.players.local();
        if(me!=null){
            if(me.getHealthPercent()<=70){
                Timer timeCheck = new Timer(6000);
                do{
                    for(Item tab : ctx.backpack.select().id(Globals.ID_ITEMS_FALLYTAB).first()){
                        if(tab.isValid()){
                            tab.interact("Break");
                        }else
                            break;
                    }
                }while(me.getHealthPercent()<=70 && timeCheck.isRunning());


                Timer timeCheck4 = new Timer(60000);
                do{
                    if(!me.isInCombat()){
                        if(!ctx.widgets.get(1092,0).isVisible()){
                            ctx.widgets.get(640, 113).click(true);
                            Delay.sleep(750, 1250);
                        }
                        if(ctx.widgets.get(1092,0).isVisible()){
                            ctx.widgets.get(1092,45).click(true);
                            Timer timeCheck3 = new Timer(15000);
                            do{
                                Delay.sleep(1000);
                            }while(me.getLocation().distanceTo(Globals.TILE_LOAD_EDGEVILLE)>=5 && timeCheck3.isRunning());
                        }
                    }
                }while(me.getLocation().distanceTo(Globals.TILE_LOAD_EDGEVILLE)>=5 && timeCheck4.isRunning());

                Timer timeCheck5 = new Timer(Random.nextInt(6000, 8500));
                do{
                    ctx.movement.newTilePath(Globals.PATH_BANK_TO).traverse();
                }while(me.getLocation().distanceTo((Globals.TILE_BANK))>=5 && timeCheck5.isRunning());

                return true;
            }
        }
        return false;
    }

    public boolean activate(){
        me= ctx.players.local();
        if(!ctx.hud.isOpen(Hud.Window.BACKPACK)){
            ctx.hud.open(Hud.Window.BACKPACK);
        }
        for (Item tempFood : ctx.backpack.id(Globals.ID_ITEMS_LOBSTER)){food=checkItem(tempFood);}
        emergencyTeleport();
        System.out.println("---Food handler Activate----");
        System.out.println("getHpPercent()<=50: " + (getHpPercent()<=50));
        System.out.println("Loot==nilGround : " + (food==nilItem));
        System.out.println("--------------------------------------");
        return (getHpPercent()<=50 && food != nilItem);
    }

    public void execute(){
        BlackUnicornKiller.status = "Eating food.";
        System.out.println("Food ACTIVATED.");
        food.interact("Eat");
    }
}
