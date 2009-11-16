package pithreads.framework;

public class UserGuard extends Guard {
	
	public UserGuard(int index, GuardExpression guardExpr) {
		super(Type.USER,index, guardExpr);
	}
	
}
