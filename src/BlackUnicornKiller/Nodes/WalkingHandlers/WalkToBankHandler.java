package BlackUnicornKiller.Nodes.WalkingHandlers;


import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Actor;
import org.powerbot.script.wrappers.Tile;


public class WalkToBankHandler extends Job {

    public WalkToBankHandler (MethodContext ctx){
        super(ctx);
    }


    private final Tile[] PATH_BANK_TO = new Tile[] {
            new Tile(3074,3502,0), new Tile(3081, 3501, 0),
            new Tile(3087, 3498, 0), new Tile(3093, 3494, 0)
    };


    private final Tile TILE_BANK = new Tile(3093,3494,0);
    private final Tile TILE_TELEPORT_AFTER = new Tile(3067,3505,0);


    public boolean activate(){
        Actor me = ctx.players.local();
        return (me.getLocation().distanceTo(TILE_TELEPORT_AFTER)<=45) && ctx.backpack.select().id(Globals.ID_ITEMS_HORN).count()>=27 &&
                (me.getAnimation()==-1 && me.getLocation().distanceTo(TILE_BANK)>=5);
    }


    public void execute(){
        BlackUnicornKiller.status = "Walking to Bank.";
        ctx.movement.newTilePath(PATH_BANK_TO).traverse();
    }
}

