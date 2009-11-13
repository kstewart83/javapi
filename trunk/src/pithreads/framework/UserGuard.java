package pithreads.framework;

public abstract class UserGuard extends Guard {
	
	protected UserGuard(int index) {
		super(Type.USER,index);
	}
	
	public abstract boolean enable();

}
