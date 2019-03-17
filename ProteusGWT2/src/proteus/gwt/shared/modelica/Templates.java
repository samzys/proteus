package proteus.gwt.shared.modelica;

public interface Templates {

	public static final String CLASS = "class MyClass \"description string\"\n"
		+"protected\n"
		+ "	annotation(Icon(), Diagram());\n"
			+ "equation\n" + "end MyClass;";

	public static final String MODEL = "model MyModel \"description string\"\n"
			+"protected\n"
			+ "	annotation(Icon(), Diagram());\n" + "equation\n" + "end MyModel;";

	public static final String CONNECTOR = "connector MyConnector \"description string\"\n"
			+ "	annotation(Icon(), Diagram());\n" + "equation\n" + "end MyConnector;";

	public static final String VARDECL = "MyTypePrefix foo(@arg_list@) annotation(Placement(transformation(origin = {@x0@, @y0@}, extent= {{@x1@,@y1@},{@x2@,@y2@}}, rotation=@z@)))";
//	FinalPrefix TypingPrefix ReplacebalePrefix VariabilityPrefix CausalityPrefix 
	// @sam 15 Apr, 2011. Annotation for the connection line, add line color,
	// line thickness in the
	public static final String CONNECT = "connect (@src@, @dest@) annotation(Line(points= {{@x1@,@y1@}}));";
}
