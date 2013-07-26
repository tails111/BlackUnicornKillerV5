package BlackUnicornKiller.Nodes.CombatHandlers;
import BlackUnicornKiller.BlackUnicornKiller;

import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.script.methods.Game;
import org.powerbot.script.methods.Hud;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Actor;
import org.powerbot.script.wrappers.Item;

public class FoodHandler extends Job {

    public FoodHandler (MethodContext ctx){
        super(ctx);
    }

    Actor me;

    public int getHpPercent() {
        return Math.abs(100 - 100 * ctx.widgets.get(748, 5).getHeight() / 28);
    }

    public boolean activate(){
        me= ctx.players.local();
        if(!ctx.hud.isOpen(Hud.Window.BACKPACK)){
            ctx.hud.open(Hud.Window.BACKPACK);
        }
        Globals.emergencyTeleport();
        return (me.isValid() && getHpPercent()<=50
                && ctx.backpack.select().id(Globals.ID_ITEMS_LOBSTER).count()>=1);
    }

    public void execute(){
        for (Item food : ctx.backpack.id(Globals.ID_ITEMS_LOBSTER)){
            if(food != null){
                BlackUnicornKiller.status = "Eating food.";
                food.interact("Eat");
            }
        }
    }
}
