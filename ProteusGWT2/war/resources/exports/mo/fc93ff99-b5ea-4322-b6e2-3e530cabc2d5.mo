model MyModel "description string"
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {81, 218},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Resistor resistor0 (useHeatPort = false,
T = 300.15,
alpha = 0,
T_ref = 300.15,
R = 1) annotation(Placement(transformation(origin = {192, 26},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Capacitor capacitor0 (C = 1) annotation(Placement(transformation(origin = {361, 26},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Inductor inductor0 (L = 1) annotation(Placement(transformation(origin = {355, 186},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (resistor0.p, ground0.p)annotation(Line(points = {{151, 58}, {151, 204}}) );
connect (resistor0.n, capacitor0.p)annotation(Line(points = {{309, 58}, {309, 51}}) );
connect (capacitor0.n, inductor0.n)annotation(Line(points = {{423, 58}, {423, 218}}) );
connect (ground0.p, inductor0.p)annotation(Line(points = {{233, 204}, {233, 218}}) );
end MyModel;
