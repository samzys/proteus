model MyModel "description string"
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {79, 369},
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
Modelica.Electrical.Analog.Basic.Inductor inductor0 (L = 1) annotation(Placement(transformation(origin = {353, 337},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Blocks.Math.InverseBlockConstraints inverseblockconstraints0 () annotation(Placement(transformation(origin = {657, 92},
extent = {{-66, -39}, {66, 39}},
rotation = 270) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (resistor0.p, ground0.p)annotation(Line(points = {{151, 58}, {151, 204}}) );
connect (resistor0.n, capacitor0.p)annotation(Line(points = {{309, 58}, {309, 51}}) );
connect (ground0.p, inductor0.p)annotation(Line(points = {{233, 204}, {233, 218}}) );
connect (inverseblockconstraints0.u1, capacitor0.n)annotation(Line(points = {{467, 57}, {467, 58}}) );
connect (inductor0.n, inverseblockconstraints0.y1)annotation(Line(points = {{570, 369}, {570, 200}}) );
end MyModel;
