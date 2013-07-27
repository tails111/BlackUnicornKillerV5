package BlackUnicornKiller.Nodes.CombatHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;

import org.powerbot.script.methods.Hud;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Actor;
import org.powerbot.script.wrappers.GroundItem;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Item;

public class LootHandler extends Job {

    public LootHandler (MethodContext ctx){
        super(ctx);
    }

    GroundItem loot;
    Item extraFood;
    Actor me;

    public void altCameraTurnTo(GroundItem e){
        Timer timeCheck = new Timer(Random.nextInt(2800, 3400));
        do{
            ctx.camera.setYaw(ctx.camera.getYaw() + Random.nextInt(35, 55));
            if(e == null){
                break;
            }
       }while(!altIsOnScreen(e) && timeCheck.isRunning());
    }

    public boolean invChangeSleep(){
        Timer timeCheck = new Timer(Random.nextInt(1200,1600));
        int tempInvCount = ctx.backpack.select().count();
        int newInvCount;
        do {
            BlackUnicornKiller.status="Waiting for Inv Change: " +timeCheck.getRemaining();
            Delay.sleep(20, 50);
            newInvCount = ctx.backpack.select().count();
            if(timeCheck.getRemaining()<=100){return false;}
        }while(tempInvCount==newInvCount && timeCheck.isRunning());
        return true;
    }

    public boolean altIsOnScreen(GroundItem e){
        return(e.isOnScreen());
    }


    public void boneAndCharmClearer(){
        Item bones;
        Item charms;
        for (Item tempBones : ctx.backpack.id(Globals.ID_ITMES_BONES).first()){
            bones=tempBones;
            bones.interact("Drop");
        }
        for(int i=0; i<=Globals.ID_CHARMS.length-1; i++){
            for (Item tempCharms : ctx.backpack.id(Globals.ID_CHARMS[i]).first()){
                charms=tempCharms;
                charms.interact("Drop");
            }
        }
    }


    public boolean activate(){
        me = ctx.players.local();

        if(!ctx.hud.isOpen(Hud.Window.BACKPACK)){
            ctx.hud.open(Hud.Window.BACKPACK);
        }
        boneAndCharmClearer();
        Globals.emergencyTeleport();

        for (GroundItem tempLoot : ctx.groundItems.select().id(Globals.ID_ITEMS_HORN).nearest()){
            loot=tempLoot;
        }

            if(ctx.backpack.select().count() >= 28){
                for (Item tempExtraFood : ctx.backpack.select().id(Globals.ID_ITEMS_LOBSTER)){
                    extraFood=tempExtraFood;
                    extraFood.interact("Eat");
                }
            }
            return(loot != null && ctx.backpack.select().count()>=28);
    }

    public void execute(){
        while(activate()){
            System.out.println("Loot handler");
                Globals.emergencyTeleport();

                if(loot != null){
                    if(!altIsOnScreen(loot)){
                        BlackUnicornKiller.status = "Walking towards Loot";
                        ctx.movement.findPath(loot).traverse();
                        BlackUnicornKiller.status = "Turning Camera to Loot";
                        altCameraTurnTo(loot);
                        if(loot.interact("Take")){
                            invChangeSleep();
                        }
                        while(me.isInMotion()){
                            me = ctx.players.local();
                            Delay.sleep(50,100);
                        }
                    } else {
                        BlackUnicornKiller.status = "Grabbing Loot.";
                        if(loot.interact("Take")){
                            invChangeSleep();
                        }
                        while(me.isInMotion()){
                            Delay.sleep(50,100);
                        }
                    }
                }
                BlackUnicornKiller.actualProfit= BlackUnicornKiller.actualProfit + (Globals.HornPrice);
                BlackUnicornKiller.postedHorns= BlackUnicornKiller.postedHorns + 1;
        }
    }
}