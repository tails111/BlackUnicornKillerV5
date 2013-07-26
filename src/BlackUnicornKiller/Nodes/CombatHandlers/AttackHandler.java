package BlackUnicornKiller.Nodes.CombatHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import BlackUnicornKiller.Nodes.WalkingHandlers.PaceUnicornsHandler;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Actor;
import org.powerbot.script.wrappers.Npc;

import java.awt.*;

public class AttackHandler extends Job {

    public AttackHandler (MethodContext ctx){
        super(ctx);
    }

    FoodHandler eating = new FoodHandler(ctx);
    PaceUnicornsHandler paceUnicorns = new PaceUnicornsHandler(ctx);

    Rectangle screen = new Rectangle(1,55,518,258);

    Actor interacting;
    Actor me;

    public void altCameraTurnTo(Actor e){
        Timer timeCheck = new Timer(Random.nextInt(2800, 3200));
        do{
            ctx.camera.setYaw(ctx.camera.getYaw() + Random.nextInt(35, 55));
            if(e == null){
                break;
            }
        }while(!altIsOnScreen(e) && timeCheck.isRunning());
    }

    public boolean altIsOnScreen(Actor e){
        Integer numOfPoints;
        for(final Polygon p : e.getModel().getTriangles()){
            numOfPoints = p.xpoints.length + p.ypoints.length;
            if(screen.contains(p.getBounds()) && numOfPoints>=4){
                return true;
            }
        }
        return false;
    }

    public boolean activate(){
        Globals.emergencyTeleport();

        for (Npc theUnicorn : ctx.npcs.id(Globals.ID_NPCS_UNICORNS).nearest()){
            interacting = ctx.players.local().getInteracting();
            me = ctx.players.local();
            if(theUnicorn == null){
                if(paceUnicorns.activate()){
                    paceUnicorns.execute();
                }
            }
            return(theUnicorn != null && interacting==null && theUnicorn.getHealthPercent()!=0
                    && me.getLocation().distanceTo(theUnicorn)<=15 && ctx.backpack.select().count()>=28);
        }
        return false;
    }

    public void execute(){
        System.out.println("Attack handler");
        BlackUnicornKiller.status="Attacking Unicorn.";

        ActionBarHandler.momentumCheck();
        Globals.emergencyTeleport();
        for (Npc theUnicorn : ctx.npcs.id(Globals.ID_NPCS_UNICORNS).nearest()){
            if(theUnicorn != null){
                if(me.getLocation().distanceTo(theUnicorn)>=4){
                    if(!altIsOnScreen(theUnicorn)){
                        altCameraTurnTo(theUnicorn);
                    }
                }
                if(!altIsOnScreen(theUnicorn)){
                    altCameraTurnTo(theUnicorn);
                }
                if(me.getLocation().distanceTo(theUnicorn)>=8){
                    ctx.movement.findPath(theUnicorn).traverse();
                }
                if(!theUnicorn.interact("Attack")){
                    ctx.movement.findPath(theUnicorn).traverse();
                    theUnicorn.interact("Attack");
                }
                altCameraTurnTo(theUnicorn);
                if(!me.isInCombat()){
                    theUnicorn.interact("Attack");
                }


            }

            if(eating.activate()){
                eating.execute();
            }
        }
    }

}
