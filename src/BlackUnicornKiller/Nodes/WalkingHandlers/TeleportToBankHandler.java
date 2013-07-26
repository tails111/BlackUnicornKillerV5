package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import BlackUnicornKiller.Nodes.Area;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Actor;
import org.powerbot.script.wrappers.Tile;

public class TeleportToBankHandler extends Job {

    public TeleportToBankHandler (MethodContext ctx){
        super(ctx);
    }


    final Area edgeville = new Area(new Tile(3063,3509,0), new Tile(3100,3486,0));

    Actor me;

    public boolean activate(){
        me = ctx.players.local();

        Globals.emergencyTeleport();
        return !edgeville.contains(Globals.me.getLocation()) &&
                me.getAnimation() == -1 && ctx.backpack.select().id(Globals.ID_ITEMS_HORN).count()>=27;
    }

    public void execute(){
        BlackUnicornKiller.status = "Homeporting to Edgeville.";
        Timer timeCheck = new Timer(5000);
        do{
            ctx.movement.findPath(Globals.TILE_LOAD_WILDERNESS).traverse();
            Timer timeCheck2 = new Timer(2000);
            do{
                Delay.sleep(150, 275);
            }while(me.isInMotion() && timeCheck2.isRunning());
        }while(timeCheck.isRunning() && me.getLocation().distanceTo(Globals.TILE_LOAD_WILDERNESS)>=2);

        ctx.movement.findPath(Globals.TILE_LOAD_WILDERNESS).traverse();

        if(me.getLocation().distanceTo(Globals.TILE_LOAD_WILDERNESS)<=14 && !Globals.me.isInCombat()){
            if(!ctx.widgets.get(1092,0).isVisible()){
                ctx.widgets.get(640,113).click(true);
                Delay.sleep(750,1250);
            }
            if(ctx.widgets.get(1092,0).isVisible()){
                ctx.widgets.get(1092,45).click(true);
                Timer timeCheck3 = new Timer(15000);
                do{
                    Delay.sleep(1000);
                }while(me.getLocation().distanceTo(Globals.TILE_LOAD_EDGEVILLE)>=5 && timeCheck3.isRunning());
            }
        }
    }
}