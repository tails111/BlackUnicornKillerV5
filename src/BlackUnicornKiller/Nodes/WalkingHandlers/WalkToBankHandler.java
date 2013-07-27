package BlackUnicornKiller.Nodes.WalkingHandlers;


import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
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
        System.out.println("---Walk to bank handler Activate----");
        System.out.println("Players get loc: " + (me.getLocation().distanceTo(TILE_TELEPORT_AFTER)<=45));
        System.out.println("Inventory Horn Count >= 27:  " + (ctx.backpack.select().id(Globals.ID_ITEMS_HORN).count()>=27));
        System.out.println("Me.getAnim == -1 : " + (me.getAnimation()==-1));
        System.out.println("getLoc >= BankTile : " + ( me.getLocation().distanceTo(TILE_BANK)>=8));
        System.out.println("--------------------------------------");
        return (me.getLocation().distanceTo(TILE_TELEPORT_AFTER)<=45) && ctx.backpack.select().id(Globals.ID_ITEMS_HORN).count()>=27 &&
                (me.getAnimation()==-1 && me.getLocation().distanceTo(TILE_BANK)>=8);
    }


    public void execute(){
        BlackUnicornKiller.status = "Walking to Bank.";
        System.out.println("Walk to Bank ACTIVATED.");
        ctx.movement.newTilePath(PATH_BANK_TO).traverse();
        Delay.sleep(400,600);
    }
}

