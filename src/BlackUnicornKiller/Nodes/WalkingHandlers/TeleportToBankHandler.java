package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;

import org.powerbot.script.util.Random;
import org.powerbot.script.wrappers.Area;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Actor;
import org.powerbot.script.wrappers.Item;
import org.powerbot.script.wrappers.Tile;

public class TeleportToBankHandler extends Job {

    public TeleportToBankHandler (MethodContext ctx){
        super(ctx);
    }


    final Area edgeville = new Area(new Tile(3063,3509,0), new Tile(3100,3486,0));

    Actor me;

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
        me = ctx.players.local();

        emergencyTeleport();
        System.out.println("---Tele to Bank handler Activate----");
        System.out.println("Edge contains me: " + !edgeville.contains(me.getLocation()));
        System.out.println("Anim == -1: " + (me.getAnimation() == -1));
        System.out.println("Inv count Horns >= 27 : " + (ctx.backpack.select().id(Globals.ID_ITEMS_HORN).count()>=27));
        System.out.println("--------------------------------------");
        return !edgeville.contains(me.getLocation()) &&
                me.getAnimation() == -1 && ctx.backpack.select().id(Globals.ID_ITEMS_HORN).count()>=27;
    }

    public void execute(){
        BlackUnicornKiller.status = "Homeporting to Edgeville.";
        System.out.println("Tele to Bank ACTIVATED.");
        Timer timeCheck = new Timer(5000);
        do{
            ctx.movement.stepTowards(Globals.TILE_LOAD_WILDERNESS);
            Timer timeCheck2 = new Timer(2000);
            do{
                Delay.sleep(150, 275);
                me = ctx.players.local();
            }while(me.isInMotion() && timeCheck2.isRunning());
        }while(timeCheck.isRunning() && me.getLocation().distanceTo(Globals.TILE_LOAD_WILDERNESS)>=4);

        ctx.movement.findPath(Globals.TILE_LOAD_WILDERNESS).traverse();
        me = ctx.players.local();
        if(me.getLocation().distanceTo(Globals.TILE_LOAD_WILDERNESS)<=14 && !me.isInCombat()){
            if(!ctx.widgets.get(1092,0).isVisible()){
                ctx.widgets.get(1430,7).click(true);
                do{
                    Delay.sleep(50,150);
                    me=ctx.players.local();
                    emergencyTeleport();
                }while(!ctx.widgets.get(1092,0).isVisible() || me.isInCombat());
            }
            if(ctx.widgets.get(1092,0).isVisible()){
                ctx.widgets.get(1092,45).click(true);
                Timer timeCheck3 = new Timer(15000);
                do{
                    emergencyTeleport();
                    Delay.sleep(600);
                    me = ctx.players.local();
                }while(me.getLocation().distanceTo(Globals.TILE_LOAD_EDGEVILLE)>=5 && timeCheck3.isRunning());
            }
        }
    }
}