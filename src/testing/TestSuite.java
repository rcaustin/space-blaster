package testing;

abstract public class TestSuite {
	static final double FLOAT_TOLERANCE = 0.00000001;
	private String name;
	protected int failures;
	
	protected TestSuite(String name) {
		this.name = name;
		this.failures = 0;
	}
	
	public void printHeader() {
		System.out.println("**************************************************");
		System.out.println("** Test Suite -- " + name);
		System.out.println("**************************************************");
	}
	
	protected int success() {
		System.out.println(" [SUCCESS]");
		return 0;
	}
	
	protected int fail() {
		System.out.println(" -- FAILED --");
		return 1;
	}
	
	abstract public int run();
}
