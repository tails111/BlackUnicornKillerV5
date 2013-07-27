package BlackUnicornKiller.Nodes.CombatHandlers;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.wrappers.Actor;

public class ActionBarHandler {

   // public static void momentumCheck(){
   //     MethodContext ctx = new MethodContext();
   //     Actor me = ctx.players.local();
   //     if(ctx.settings.get(679)>=1){
   //         do{
   //             Delay.sleep(50, 100);
   //         }while(me.isInCombat());
   //         int x=0;
   //         do{
   //             x++;
   //             Delay.sleep(400,600);
   //             if(abilityReady(1)){
    //                executeAbility(1);
    //            }
    //            if(me.getMessage().matches("Momentum is now active.")){
    //                break;
    //            }
     //       }while(x<=20);
     //   }
    //}

    public static boolean abilityReady(int slotNum){
        MethodContext ctx = new MethodContext();
        final int TEXTURE_ID = 14521;
        final int ADRENA_TEXT_COLOR = 16777215;
        final int WIDGET = 640;
        int coolDownSlot = 0;
        int adrenaSlot = 0;

        switch (slotNum){
            case 1: coolDownSlot = 36;
                adrenaSlot = 32;
                break;
            case 2: coolDownSlot = 73;
                adrenaSlot = 72;
                break;
            case 3: coolDownSlot = 77;
                adrenaSlot = 76;
                break;
            case 4: coolDownSlot = 81;
                adrenaSlot = 80;
                break;
            case 5: coolDownSlot = 85;
                adrenaSlot = 84;
                break;
            case 6: coolDownSlot = 89;
                adrenaSlot = 88;
                break;
            case 7: coolDownSlot = 93;
                adrenaSlot = 92;
                break;
            case 8: coolDownSlot = 97;
                adrenaSlot = 96;
                break;
            case 9: coolDownSlot = 101;
                adrenaSlot = 100;
                break;
            case 0: coolDownSlot = 105;
                adrenaSlot = 104;
                break;
            case 10: coolDownSlot = 109;
                adrenaSlot = 108;
                break;
            case 11: coolDownSlot = 113;
                adrenaSlot = 112;
                break;

        }

        if(ctx.widgets.get(WIDGET,coolDownSlot).getTextureId()==TEXTURE_ID){
            if(ctx.widgets.get(WIDGET,adrenaSlot).getTextColor()==ADRENA_TEXT_COLOR){
                return true;
            }
        }
        return false;
    }

    public static void executeAbility(int slotNum){
        MethodContext ctx = new MethodContext();
        switch (slotNum){
            case 1: ctx.keyboard.sendln("1");
                break;
            case 2: ctx.keyboard.sendln("2");
                break;
            case 3: ctx.keyboard.sendln("3");
                break;
            case 4: ctx.keyboard.sendln("4");
                break;
            case 5: ctx.keyboard.sendln("5");
                break;
            case 6: ctx.keyboard.sendln("6");
                break;
            case 7: ctx.keyboard.sendln("7");
                break;
            case 8: ctx.keyboard.sendln("8");
                break;
            case 9: ctx.keyboard.sendln("9");
                break;
            case 0: ctx.keyboard.sendln("0");
                break;
            case 10: ctx.keyboard.sendln("-");
                break;
            case 11: ctx.keyboard.sendln("=");
                break;
        }
        Delay.sleep(1000,1500);
    }
}