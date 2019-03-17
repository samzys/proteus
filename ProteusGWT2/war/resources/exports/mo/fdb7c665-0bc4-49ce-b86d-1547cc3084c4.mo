model MyModel "description string"
Modelica.Electrical.Analog.Basic.Resistor resistor0 (alpha = 0,
T_ref = 300.15,
R = 9) annotation(Placement(transformation(origin = {-309, 231},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {-112, 260},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (resistor0.n, ground0.p)annotation(Line(points = {{-194, 231}, {-194, 293}}) );
end MyModel;
