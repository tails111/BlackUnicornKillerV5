package BlackUnicornKiller.Nodes.CombatHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;

import org.powerbot.script.methods.Hud;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GroundItem;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Item;

import java.awt.*;

public class LootHandler extends Job {

    public LootHandler (MethodContext ctx){
        super(ctx);
    }

    Rectangle screen = new Rectangle(1,55,518,258);
    GroundItem Loot;

  //  public void altCameraTurnTo(GroundItem e){
  //      Timer timeCheck = new Timer(Random.nextInt(2800, 3400));
  //      do{
  //          ctx.camera.setAngle(ctx.camera.getYaw() + Random.nextInt(35, 55));
  //          if(e == null){
  //              break;
  //          }
  //     }while(!altIsOnScreen(e) && timeCheck.isRunning());
  //  }

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
        for(final Polygon p : e.getModel().getTriangles()){
            if(screen.contains(p.getBounds())){
                return true;
            }
        }
        return false;
    }

    public void boneAndCharmClearer(){
        for (Item bones : ctx.backpack.id(Globals.ID_ITMES_BONES).first()){
            bones.interact("Drop");
        }
        for(int i=0; i<=Globals.ID_CHARMS.length-1; i++){
            for (Item charms : ctx.backpack.id(Globals.ID_CHARMS[i]).first()){
                charms.interact("Drop");
            }
        }
    }

    public boolean activate(){
        Globals.me = ctx.players.local();

        if(!ctx.hud.isOpen(Hud.Window.BACKPACK)){
            ctx.hud.open(Hud.Window.BACKPACK);
        }
        boneAndCharmClearer();
        Globals.emergencyTeleport();

        for (GroundItem loot : ctx.groundItems.select().id(Globals.ID_ITEMS_HORN).nearest()){

            if(ctx.backpack.select().count() >= 28){
                for (Item extraFood : ctx.backpack.select().id(Globals.ID_ITEMS_LOBSTER)){
                    extraFood.interact("Eat");
                }
            }
            return(loot != null && ctx.backpack.select().count()>=28);
        }
        return false;
    }

    public void execute(){
        while(activate()){
            System.out.println("Loot handler");
            for (GroundItem loot : ctx.groundItems.select().id(Globals.ID_ITEMS_HORN).nearest()){
                Globals.emergencyTeleport();

                if(loot != null){
                    if(!altIsOnScreen(loot)){
                        BlackUnicornKiller.status = "Walking towards Loot";
                        ctx.movement.findPath(loot).traverse();
                        BlackUnicornKiller.status = "Turning Camera to Loot";
                       // altCameraTurnTo(Loot);
                        ctx.movement.findPath(Loot).traverse();
                        if(loot.interact("Take")){
                            invChangeSleep();
                        }
                        while(Globals.me.isInMotion()){
                            Globals.me = ctx.players.local();
                            Delay.sleep(50,100);
                        }
                    } else {
                        BlackUnicornKiller.status = "Grabbing Loot.";
                        if(loot.interact("Take")){
                            invChangeSleep();
                        }
                        while(Globals.me.isInMotion()){
                            Delay.sleep(50,100);
                        }
                    }
                }
                BlackUnicornKiller.actualProfit= BlackUnicornKiller.actualProfit + (Globals.HornPrice);
                BlackUnicornKiller.postedHorns= BlackUnicornKiller.postedHorns + 1;
            }
        }
    }
}