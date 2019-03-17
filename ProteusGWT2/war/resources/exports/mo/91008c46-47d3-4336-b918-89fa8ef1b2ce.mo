model MyModel "description string"
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {-367, 242},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Resistor resistor0 (alpha = 0,
T_ref = 300.15,
R = 1) annotation(Placement(transformation(origin = {-131, 360},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (ground0.p, resistor0.p)annotation(Line(points = {{-368, 275}, {-368, 360}}) );
end MyModel;
