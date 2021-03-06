package BlackUnicornKiller.Nodes.CombatHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import BlackUnicornKiller.Nodes.WalkingHandlers.PaceUnicornsHandler;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Actor;
import org.powerbot.script.wrappers.Item;
import org.powerbot.script.wrappers.Npc;
import org.powerbot.script.wrappers.Tile;


public class AttackHandler extends Job {

    public AttackHandler (MethodContext ctx){
        super(ctx);
    }

    FoodHandler eating = new FoodHandler(ctx);
    PaceUnicornsHandler paceUnicorns = new PaceUnicornsHandler(ctx);

    Actor me;
    Npc theUnicorn;

    private Npc nilNpc = ctx.npcs.getNil();

    public void altCameraTurnTo(Actor e){
        Timer timeCheck = new Timer(Random.nextInt(2800, 3200));
        do{
            ctx.camera.setYaw(ctx.camera.getYaw() + Random.nextInt(35, 55));
            if(e == nilNpc){
                break;
            }
        }while(!altIsOnScreen(e) && timeCheck.isRunning());
    }

    public boolean altIsOnScreen(Actor e){
        return(e.isOnScreen());
    }

    private Npc checkNPC(Npc tNpc){
        if(tNpc==null){
            return ctx.npcs.getNil();
        }
        return tNpc;
    }

    public boolean closeToUnicorns(){
        Tile distanceToUnicornsTile;
        for(int i=0; i<=Globals.unicornPacePath.length-1; i++){
            distanceToUnicornsTile = Globals.unicornPacePath[i];
            if(ctx.players.local().getLocation().distanceTo(distanceToUnicornsTile)<=18){
                return(ctx.players.local().getLocation().distanceTo(distanceToUnicornsTile)<=18);
            }
        }
        return false;
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

                Timer timeCheck5 = new Timer(Random.nextInt(6000,8500));
                do{
                    ctx.movement.newTilePath(Globals.PATH_BANK_TO).traverse();
                }while(me.getLocation().distanceTo((Globals.TILE_BANK))>=5 && timeCheck5.isRunning());

                return true;
            }
        }
        return false;
    }

    public boolean activate(){
        emergencyTeleport();

        theUnicorn = nilNpc;
        for (Npc tempUnicorn : ctx.npcs.select().id(Globals.ID_NPCS_UNICORNS).nearest().first()){theUnicorn=checkNPC(tempUnicorn);}
        me = ctx.players.local();
        //    if(theUnicorn == nilNpc || theUnicorn ==null){
        //        if(paceUnicorns.activate()){
        //            paceUnicorns.execute();
        //        }
        //    }
        System.out.println("---Attack handler Activate----");
        System.out.println("Inv Count <= 27: " + (ctx.backpack.select().count()<=27));
        System.out.println("ClosetoUni: " +  (closeToUnicorns()));
        System.out.println("theUnicorn.getHealthPercent()!=0 : " + (theUnicorn.getHealthPercent()!=0));
        System.out.println("ctx.backpack.select().count()<=27: " + (ctx.backpack.select().count()<=27));
        System.out.println("--------------------------------------");
            return(theUnicorn != nilNpc && theUnicorn.getHealthPercent()!=0 && closeToUnicorns()
                    && ctx.backpack.select().count()<=27 && me.getInteracting()==null);
    }

    public void execute(){
        System.out.println("Attack handler ACTIVATED.");
        BlackUnicornKiller.status="Attacking Unicorn.";
        me=ctx.players.local();
       // ActionBarHandler.momentumCheck();
        emergencyTeleport();
        if(theUnicorn != nilNpc){
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
        theUnicorn=nilNpc;
    }
}
