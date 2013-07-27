package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Actor;
import org.powerbot.script.wrappers.Item;
import org.powerbot.script.wrappers.Tile;

public class WalkToUnicornsHandler extends Job {

    public WalkToUnicornsHandler (MethodContext ctx){
        super(ctx);
    }

    private Tile distanceToUnicorns;

    public static final Tile[] PATH_TO_UNICORNS = new Tile[] {new Tile(3133,3633,0), new Tile(3126,3627,0)};

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
        Actor me = ctx.players.local();

        emergencyTeleport();
        for(int i=0; i<=Globals.unicornPacePath.length-1; i++){
            distanceToUnicorns = Globals.unicornPacePath[i];
            if(me.getLocation().distanceTo(distanceToUnicorns)<=4){
                break;
            }
        }
        System.out.println("---Walk to UNi handler Activate----");
        System.out.println("get loc dis to Uni >= 4: " + (me.getLocation().distanceTo(distanceToUnicorns)>=4));
        System.out.println("get loc dis to Wild Tile <= 12:  " + (me.getLocation().distanceTo(Globals.TILE_LOAD_WILDERNESS)<=12));
        System.out.println("Inventory count <= 28 : " + (ctx.backpack.count()<=28));
        System.out.println("Inventory count: " + ctx.backpack.count());
        System.out.println("Inv count w/ sel: " + ctx.backpack.select().count());
        System.out.println("--------------------------------------");
        return (me.getLocation().distanceTo(distanceToUnicorns)>=4 && me.getLocation().distanceTo(Globals.TILE_LOAD_WILDERNESS)<=12
                && ctx.backpack.select().count()<=27);
    }

    public void execute(){
        BlackUnicornKiller.status = "Walking to Unicorns.";
        System.out.println("Walk to Uni ACTIVATED.");
        ctx.movement.newTilePath(PATH_TO_UNICORNS).traverse();
    }

}
