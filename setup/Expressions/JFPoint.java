package setup.Expressions;
 
import java.awt.Point;

@SuppressWarnings("serial")
public class JFPoint extends Point {
	
	public JFPoint() {
		super();
	}
	
	public JFPoint(int x, int y) {
		super(x, y);
	}
	@Override
	public String toString() {
		String x, y;
		
		x = String.valueOf(JFPoint.this.getX());
		y = String.valueOf(JFPoint.this.getY());
		
		return "[X=" + x + ",Y=" + y + "]"; 
	}
}
