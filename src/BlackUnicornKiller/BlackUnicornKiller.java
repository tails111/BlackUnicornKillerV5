package BlackUnicornKiller;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Actor;

import BlackUnicornKiller.Jobs.Job;
import BlackUnicornKiller.Jobs.JobContainer;
import BlackUnicornKiller.Nodes.Globals;
import BlackUnicornKiller.Nodes.BankingHandlers.BankingHandler;
import BlackUnicornKiller.Nodes.CombatHandlers.AttackHandler;
import BlackUnicornKiller.Nodes.CombatHandlers.LootHandler;
import BlackUnicornKiller.Nodes.WalkingHandlers.TeleportToBankHandler;
import BlackUnicornKiller.Nodes.WalkingHandlers.TeleportToUnicornsHandler;
import BlackUnicornKiller.Nodes.WalkingHandlers.WalkToBankHandler;
import BlackUnicornKiller.Nodes.WalkingHandlers.WalkToUnicornsHandler;

@Manifest(name = "Black Unicorn Killer", authors = "tails111", description = "Kills Black Unicorns, and collects horns.", version = 1.0, hidden = true)
public class BlackUnicornKiller extends PollingScript implements PaintListener {

    JobContainer container = new JobContainer();

	@Override
	public void start() {

        Globals.setContext(ctx);

        System.out.println("Starting Script.");
		
		me = ctx.players.local();
		interacting = me.getInteracting();
		container.submit(new WalkToBankHandler(ctx), new BankingHandler(ctx), new TeleportToUnicornsHandler(ctx),
				new WalkToUnicornsHandler(ctx), new AttackHandler(ctx), new LootHandler(ctx), new TeleportToBankHandler(ctx));
	}

	public long startTime = System.currentTimeMillis();

	private int getPerHour(final int value){
		return (int) (value * 3600000D / (System.currentTimeMillis() - startTime));
	}

	public boolean running = true;

	public static int actualProfit = 0;
	public static int postedProfitPerMath = 0;
	public static int postedHorns = 0;
	public static int postedHornsPerMath = 0;
	public static int postedTimeRun = 0;

	
	private final Timer runtime = new Timer(0);
	
	public static String status = "Hello Stephen.";

	private Actor me;
	private Actor interacting;

	public void repaint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		
		postedProfitPerMath = getPerHour(actualProfit);
		postedHornsPerMath = getPerHour(postedHorns);
		postedTimeRun = getPerHour(1000);

		g.setColor(Color.ORANGE);
		int mouseY = (int) ctx.mouse.getLocation().getY();
		int mouseX = (int) ctx.mouse.getLocation().getX();
		g.drawLine(mouseX - 999, mouseY + 999, mouseX + 999, mouseY - 999);
		g.drawLine(mouseX + 999, mouseY + 999, mouseX - 999, mouseY - 999);
		g.drawOval(mouseX-4,mouseY-4, 8, 8);
		g.setColor(Color.GREEN);
		g.fillOval(mouseX-2,mouseY-2,5,5);

		Font fontNormal = new Font("Comic Sans MS", Font.PLAIN, 12);
		Font fontTitle = new Font("Comic Sans MS", Font.BOLD, 12);
		g.setFont(fontTitle);
		g.setColor(Color.BLACK);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
		g.fillRect(10, 75, 200, 84);
		g.setColor(Color.CYAN);
		g.drawLine(10, 75, 10, 159);//LEFT
		g.drawLine(10,75,210,75);//TOP
		g.drawLine(210,75,210,159);//RIGHT
		g.drawLine(10,159,210,159);//BOTTOM
		//x1,y1,x2,y2
		g.drawString("    Black Unicorn Killer",11,90);
		g.setFont(fontNormal);
		g.drawString(("Status: " + status), 15, 105);
		g.drawString(("Time Run: " + runtime.toElapsedString()), 15, 122);
		g.drawString(("Profit: " + actualProfit + "(" + postedProfitPerMath + ")"), 15, 139);
		g.drawString(("Horns/H: "+ postedHorns + "(" + postedHornsPerMath + ")"),15, 156);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

		if(me != null)
		interacting = me.getInteracting();

		if(interacting != null){
			int perc = interacting.getHealthPercent();
			g.setColor(perc >= 75 ? Color.GREEN : perc >= 50 ? Color.YELLOW : Color.RED);
			
			for(final Polygon p : interacting.getModel().getTriangles()){
				g.fillPolygon(p);
			}
		}
	}

    Job job = null;
	@Override
	public int poll(){
        me = ctx.players.local();
		interacting = me.getInteracting();

		job = container.get();
        if(job != null){
            System.out.println("job started: " + container.get());
			job.execute();
			return job.delay();
        }

		return(Random.nextInt(650, 800));
	}

}