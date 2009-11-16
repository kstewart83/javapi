package pithreads.framework;

public class TrueGuard implements GuardExpression {
	private static final TrueGuard singleton = new TrueGuard();
	
	private TrueGuard() {		
	}
	
	public static final TrueGuard getSingleton() {
		return singleton;
	}
	
	@Override
	public boolean check() {
		return true;
	}

}
