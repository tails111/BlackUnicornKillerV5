package BlackUnicornKiller.Nodes.BankingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.script.methods.*;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.*;

public class BankingHandler extends Job {

    public BankingHandler(MethodContext ctx){
        super(ctx);
    }

    private Tile bankTile = new Tile(3093,3494,0);
    public Item nilItem = ctx.backpack.getNil();

    public boolean invChangeSleep(){
        Timer timeCheck = new Timer(Random.nextInt(1200, 1600));
        int tempInvCount = ctx.backpack.count();
        int newInvCount;
        do {
            BlackUnicornKiller.status="Waiting for Inv Change: " +timeCheck.getRemaining();
            Delay.sleep(20, 50);
            newInvCount = ctx.backpack.count();
            if(timeCheck.getRemaining()<=100){return false;}
        }while(tempInvCount==newInvCount && timeCheck.isRunning());
        return true;
    }

    public boolean toggleBank(boolean open){
        for (GameObject bankBooth : ctx.objects.select().id(Globals.ID_BANK_BOOTH).nearest().first()){
            if(open){
                if(!ctx.bank.isOpen()){
                    BlackUnicornKiller.status = "Opening bank booth.";
                    int x = 0;
                    do{
                        x++;
                        bankBooth.interact("Bank");
                        Delay.sleep(750,1000);
                        do{
                            Delay.sleep(20,30);
                        }while(ctx.players.local().isInMotion());
                    }while(!ctx.bank.isOpen() && x<=10);
                    if(ctx.bank.isOpen()){return true;}
                }
            }else{
                Timer timeCheck = new Timer(5000);
                do{
                    ctx.bank.close();
                    Delay.sleep(125,250);
                }while(ctx.bank.isOpen() && timeCheck.isRunning());
                if(!ctx.bank.isOpen()){return true;}
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

    Item item = nilItem;
    public boolean activate(){
        item=nilItem;
        for(Item tempItem : ctx.backpack.select().id(Globals.ID_ITEMS_LOBSTER)){item=checkItem(tempItem);}
        System.out.println("---Bank handler Activate----");
        System.out.println("Dis to Bank <= 8: " + (ctx.players.local().getLocation().distanceTo(bankTile)<=8));
        System.out.println("item==nilItem : " + (item==nilItem));
        System.out.println("--------------------------------------");
        return ctx.players.local().getLocation().distanceTo(bankTile)<=8 && item == nilItem;
    }

    public void execute(){
        System.out.println("Banking ACTIVATED.");

        if(toggleBank(true)){
            BlackUnicornKiller.status = "Depositing inventory.";
            ctx.bank.depositInventory();
            invChangeSleep();
            for(int x = 0; x<= Globals.ITEMS_REQUIRED.length-1; x++){
                BlackUnicornKiller.status = "Withdrawing required items.";
                if(ctx.bank.withdraw(Globals.ITEMS_REQUIRED[x],Globals.ITEMS_REQUIRED_AMOUNTS[x])){invChangeSleep();}
            }
           // if(needStaff){
           //     ctx.bank.withdraw(Globals.ID_WEAPON, 1);
           //     invChangeSleep();
           //     if(ctx.backpack.select().id(Globals.ID_WEAPON).getNil()!=null){
           //         ctx.backpack.select().id(Globals.ID_WEAPON).getNil().interact("Wield");
           //         invChangeSleep();
           //     }
          //  }
            Timer timeCheck = new Timer(Random.nextInt(3000,5000));
            do{
                toggleBank(false);
            }while(ctx.bank.isOpen() && timeCheck.isRunning());
        }
        //   Globals.scriptSleeper();
    }
}