model www "description string"
Modelica.Electrical.Analog.Basic.Resistor resistor0 (alpha = 0,
T_ref = 300.15,
R = 123) annotation(Placement(transformation(origin = {-158, 325},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {-376, 94},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.HeatingResistor heatingresistor0 (R_ref = 1111,
alpha = 0,
T_ref = 300.15) annotation(Placement(transformation(origin = {3, 110},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Ground ground1 () annotation(Placement(transformation(origin = {-185, 134},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Ground ground2 () annotation(Placement(transformation(origin = {-445, 194},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Resistor resistor1 (alpha = 0,
T_ref = 300.15,
R = 1) annotation(Placement(transformation(origin = {29, 209},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (ground0.p, resistor0.p)annotation(Line(points = {{-376, 325}}) );
connect (resistor0.n, heatingresistor0.n)annotation(Line(points = {{85, 325}, {85, 110}}) );
connect (heatingresistor0.p, ground1.p)annotation(Line(points = {{-120, 113}, {-120, 167}}) );
connect (ground0.p, ground1.p)annotation(Line(points = {{-286, 127}, {-286, 167}}) );
connect (ground2.p, ground0.p)annotation(Line(points = {{-407, 227}, {-407, 127}}) );
end www;
