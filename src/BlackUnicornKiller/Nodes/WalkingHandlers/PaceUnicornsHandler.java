package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Actor;
import org.powerbot.script.wrappers.GroundItem;
import org.powerbot.script.wrappers.Item;
import org.powerbot.script.wrappers.Tile;

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

    public static double distanceToUnicorns(){
        MethodContext ctx = new MethodContext();
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

    public boolean activate(){
        me = ctx.players.local();
        Globals.emergencyTeleport();
        for(GroundItem tempLoot :  ctx.groundItems.select().id(Globals.ID_ITEMS_HORN).nearest().first()){loot=tempLoot;}
        for(Item tempItem : ctx.backpack.select().id(Globals.ID_ITEMS_FALLYTAB).first()){item=tempItem;}
        return(ctx.backpack.select().count()>=28 && me.getHealthPercent()>=25 && distanceToUnicorns()<=10
              && interacting == null && !loot.isValid() && item.isValid());
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
