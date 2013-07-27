package BlackUnicornKiller.Nodes.WalkingHandlers;


import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Actor;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.GroundItem;
import org.powerbot.script.wrappers.Item;


public class TeleportToUnicornsHandler extends Job {


    public TeleportToUnicornsHandler (MethodContext ctx){
        super(ctx);
    }

    private void sleepGameTick(){
        Delay.sleep(650, 850);
    }


    Actor interacting;
    Actor me;

    GroundItem loot;
    Item item;

    Component wildernessLode;
    Component wildernessLodeClick;
    Component wildernessWarning;
    Component wildernessChat;
    Component actionBar;
    Component actionBarSpell;


    public boolean activate(){
        me = ctx.players.local();
        wildernessLode = ctx.widgets.get(1092,0);
        wildernessLodeClick = ctx.widgets.get(1092,59);
        wildernessWarning = ctx.widgets.get(1186,7);
        wildernessChat = ctx.widgets.get(1188,3);
        actionBar = ctx.widgets.get(1430,7);
        actionBarSpell = ctx.widgets.get(1430,7);


        for(GroundItem tempLoot :  ctx.groundItems.select().id(Globals.ID_ITEMS_HORN).nearest().first()){loot=tempLoot;}
        for(Item tempItem : ctx.backpack.select().id(Globals.ID_ITEMS_LOBSTER).first()){item=tempItem;}
        return (me.getLocation().distanceTo(Globals.TILE_LOAD_WILDERNESS)>=10 && PaceUnicornsHandler.distanceToUnicorns()<=6
                && !loot.isValid() &&  item.isValid() && interacting == null);
    }


    public void execute(){
        BlackUnicornKiller.status = "Homeporting to Wilderness.";


        //Wilderness load click  to warning
        if(wildernessLode.isVisible()){
            wildernessLode.click(true);
            Timer timeCheck = new Timer(2000);
            do{
                Delay.sleep(30,50);
            }while(timeCheck.isRunning() && !ctx.widgets.get(1186,7).isVisible());
        }
        sleepGameTick();


        //Wilderness Warning to Teleport Question
        if(wildernessWarning.isValid()){
            wildernessWarning.click(true);
            Timer timeCheck2 = new Timer(2000);
            do{
                Delay.sleep(30,50);
            }while(timeCheck2.isRunning() && !wildernessChat.isVisible());
        }
        sleepGameTick();


        //Yes to Wilderness
        if(wildernessChat != null && wildernessChat.isVisible()){
            if(wildernessChat.getText().contains("Yes")){
                wildernessChat.click(true);
            }
            Timer timeCheck3 = new Timer(20000);
            do{
                Delay.sleep(30,50);
            }while(timeCheck3.isRunning() && me.getLocation().distanceTo(Globals.TILE_LOAD_WILDERNESS)>=5);
        }
        sleepGameTick();


        //Action bar click to LodeStone wait.

        if(actionBar.isVisible() && !wildernessChat.isVisible() && !wildernessWarning.isVisible() && me.getAnimation()==-1){
            actionBarSpell.click(true);   //Action bar click
            Timer timeCheck = new Timer(5000);
            do{
                Delay.sleep(20,50);
            }while(timeCheck.isRunning() && !actionBar.isVisible()); //Lodestone Network Screen
        }


    }
}

