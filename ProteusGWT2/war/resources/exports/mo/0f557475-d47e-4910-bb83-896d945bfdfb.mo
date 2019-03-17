model MyModel "description string"
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {-369.0, 170.0},
extent = {{-33.0, -33.0}, {33.0, 33.0}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Resistor resistor0 (alpha = 3,
T_ref = 300.15,
R = 3) annotation(Placement(transformation(origin = {-209.0, 253.0},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (ground0.p, resistor0.p)annotation(Line(points = {{-348.0, 203.0}, {-348.0, 255.0}, {-295.0, 255.0}, {-295.0, 253.0}}) );
end MyModel;
