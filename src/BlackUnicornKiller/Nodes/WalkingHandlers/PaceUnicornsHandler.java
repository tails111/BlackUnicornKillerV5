package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.*;

public class PaceUnicornsHandler extends Job {

    public PaceUnicornsHandler (MethodContext ctx){
        super(ctx);
    }

    public static Tile[] unicornPacePath = new Tile[] {
            new Tile(3116,3601,0), new Tile(3119,3610,0), new Tile(3123,3619,0), new Tile(3122,3629,0),
            new Tile(3115,3638,0), new Tile(3100,3638,0), new Tile(3089,3634,0), new Tile(3079,3632,0),
            new Tile(3075,3622,0), new Tile(3075,3633,0)};

    public static Tile[] unicornPacePathReverse = new Tile[] {
            new Tile(3075,3633,0), new Tile(3075,3622,0), new Tile(3079,3632,0), new Tile(3089,3634,0),
            new Tile(3100,3638,0), new Tile(3115,3638,0), new Tile(3122,3629,0), new Tile(3123,3619,0),
            new Tile(3119,3610,0), new Tile(3116,3601,0)};

    public static Tile endTile = new Tile(3075,3633,0);
    public static Tile startTile = new Tile(3116,3601,0);

    public static Tile distanceToUnicornsTile;

    public static char placement = 'B';

    public double distanceToUnicorns(){
        for(int i=0; i<=Globals.unicornPacePath.length-1; i++){
            distanceToUnicornsTile = Globals.unicornPacePath[i];
            if(ctx.movement.getDistance(ctx.players.local(),distanceToUnicornsTile)<=8){
                return(ctx.movement.getDistance(ctx.players.local(),distanceToUnicornsTile));
            }
        }
        return 0;
    }

    Actor interacting;
    Actor me;

    GroundItem loot;
    Item item;

    public Item nilItem = ctx.backpack.getNil();

    private GroundItem checkGround(GroundItem tGround){
        if(tGround==null){
            return ctx.groundItems.getNil();
        }
        return tGround;
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

    private Item checkItem(Item tItem){
        if(tItem==null){
            return ctx.backpack.getNil();
        }
        return tItem;
    }

    public boolean activate(){
        me = ctx.players.local();
        interacting = me.getInteracting();
        emergencyTeleport();
        for(GroundItem tempLoot :  ctx.groundItems.select().id(Globals.ID_ITEMS_HORN).nearest().first()){loot=checkGround(tempLoot);}
        for(Item tempItem : ctx.backpack.select().id(Globals.ID_ITEMS_FALLYTAB).first()){item=checkItem(tempItem);}
        return(ctx.backpack.select().count()>=28 && me.getHealthPercent()>=25 && distanceToUnicorns()<=10
              && interacting == null && !loot.isValid() && item!=nilItem);
    }

    public void execute(){
        me = ctx.players.local();
        BlackUnicornKiller.status="Pacing to find Unicorns.";

        if(me.getLocation().distanceTo(endTile)<=6){
            placement = 'E';
        }
        if(me.getLocation().distanceTo(startTile)<=6){
            placement = 'B';
        }

        if(placement == 'B'){
            ctx.movement.newTilePath(unicornPacePath).traverse();
        }

        if(placement == 'E'){
            ctx.movement.newTilePath(unicornPacePathReverse).traverse();
        }



    }

}
