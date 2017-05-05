package restructo.robot.doc;

public class CompVarCaller extends Caller<CompositeVariable> {

	@Override
	public String getName() {
		return this.getOrigin().getName();
	}

}
