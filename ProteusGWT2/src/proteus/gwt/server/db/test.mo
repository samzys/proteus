model MyModel "description string"
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {-273, 244},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Resistor resistor0 (alpha = 0,
T_ref = 300.15,
R = 1) annotation(Placement(transformation(origin = {-79, 262},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.HeatingResistor heatingresistor0 (R_ref = 1,
alpha = 0,
T_ref = 300.15) annotation(Placement(transformation(origin = {-380, 138},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (ground0.p, heatingresistor0.n)annotation(Line(points = {{-314, 280.3}, {-314, 141.3}}) );
connect (ground0.p, resistor0.p)annotation(Line(points = {{-196, 280.3}, {-196, 265.3}}) );
end MyModel;
