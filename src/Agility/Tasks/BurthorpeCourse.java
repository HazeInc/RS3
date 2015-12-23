package Agility.Tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Camera;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.Component;

import Agility.Task;

public class BurthorpeCourse extends  Task<ClientContext> {
	private long last = 0;
	
    private enum Obstacles {
        LOG_BEAM("Log beam", 66894, new Area(
        		new Tile(2915, 3553, 0),
    			new Tile(2915, 3550, 0),
    			new Tile(2920, 3553, 0),
    			new Tile(2920, 3550, 0)), "Walk"),
        WALL("Wall", 66912, new Area(
        		new Tile(2918, 3558, 0),
    			new Tile(2920, 3558, 0),
    			new Tile(2919, 3561, 0),
    			new Tile(2920, 3561, 0)), "Climb-up"),
        BALANCING_LEDGE("Balancing ledge", 66909, new Area(
        		new Tile(2920, 3562, 1),
    			new Tile(2918, 3562, 1),
    			new Tile(2920, 3564, 1),
    			new Tile(2918, 3564, 1)), "Walk-across"),
        OBSTACLE_LOW_WALL("Obstacle low wall", 66902, new Area(
        		new Tile(2912, 3564, 1)), "Climb-over"),
        ROPE_SWING("Rope swing", 66903, new Area(
        		new Tile(2912, 3562, 1)), "Swing-on"),
        MONKEY_BARS("Monkey bars", 66897, new Area(
        		new Tile(2916, 3562, 1)), "Swing-across"),
        LEDGE("Ledge", 66910, new Area(
        		new Tile(2917, 3553, 1)), "Jump-down");
        final String obstacleName, obstacleInteraction;
        final int obstacleId;
        final Area obstacleArea;

        private Obstacles(final String obstacleName, final int obstacleId,
                          final Area obstacleArea, final String obstacleInteraction) {
            this.obstacleName = obstacleName;
            this.obstacleId = obstacleId;
            this.obstacleArea = obstacleArea;
            this.obstacleInteraction = obstacleInteraction;
        }

        public String getObstacleName() {
            return obstacleName;
        }

        public String getObstacleInteraction() {
            return obstacleInteraction;
        }

        public int getObstacleId() {
            return obstacleId;
        }

        public Area getObstacleArea() {
            return obstacleArea;
        }
    }

    public BurthorpeCourse(ClientContext ctx) {
		super(ctx);
	}

    @Override
    public boolean activate() {
        return !ctx.players.local().inMotion() && ctx.players.local().animation() == -1;
        
    }

    @Override
    public void execute() {
        for (Obstacles obstacles : Obstacles.values()) {
            if (obstacles.getObstacleArea().contains(ctx.players.local())) {
                final GameObject gameObject = ctx.objects.select().id(obstacles.getObstacleId()).nearest().poll();
                if (gameObject.inViewport()) {
                	last = System.currentTimeMillis();
                    gameObject.interact(obstacles.getObstacleInteraction(), obstacles.getObstacleName());
                    	Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.players.local().animation() != -1;
                        }
                    }, 150, 10);
                } else {
                    ctx.camera.turnTo(gameObject);
                }
            }
        }
    }
}
