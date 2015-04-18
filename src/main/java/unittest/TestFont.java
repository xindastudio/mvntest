package unittest;
import java.awt.GraphicsEnvironment;

/**
 * @author wuliwei
 *
 */
public class TestFont {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();
		for (int i = 0; i < fontNames.length; i++)
			System.out.println(fontNames[i]);
	}

}
