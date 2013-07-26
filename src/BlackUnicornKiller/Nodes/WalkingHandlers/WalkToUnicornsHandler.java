package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Tile;

public class WalkToUnicornsHandler extends Job {

    public WalkToUnicornsHandler (MethodContext ctx){
        super(ctx);
    }

    private Tile distanceToUnicorns;

    public static final Tile[] PATH_TO_UNICORNS = new Tile[] {new Tile(3133,3633,0), new Tile(3126,3627,0)};

    public boolean activate(){
        Globals.me = ctx.players.local();

        Globals.emergencyTeleport();
        for(int i=0; i<=Globals.unicornPacePath.length-1; i++){
            distanceToUnicorns = Globals.unicornPacePath[i];
            if(Globals.me.getLocation().distanceTo(distanceToUnicorns)<=4){
                break;
            }
        }
        return (Globals.me.getLocation().distanceTo(distanceToUnicorns)>=4 && Globals.me.getLocation().distanceTo(Globals.TILE_LOAD_WILDERNESS)<=12
                && ctx.backpack.select().count()>=28);
    }

    public void execute(){
        BlackUnicornKiller.status = "Walking to Unicorns.";
        ctx.movement.newTilePath(PATH_TO_UNICORNS).traverse();
    }

}
