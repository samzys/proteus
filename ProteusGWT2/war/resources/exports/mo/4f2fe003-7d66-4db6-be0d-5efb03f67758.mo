model MyModel "description string"
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {-399, -232},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Resistor resistor0 (useHeatPort = false,
T = 300.15,
alpha = 0,
T_ref = 300.15,
R = 1) annotation(Placement(transformation(origin = {-200, -294},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Blocks.Math.InverseBlockConstraints inverseblockconstraints0 () annotation(Placement(transformation(origin = {-352, -377},
extent = {{-66, -39}, {66, 39}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (ground0.p, resistor0.p)annotation(Line(points = {{216, 168}, {216, 138}}) );
connect (inverseblockconstraints0.y1, resistor0.n)annotation(Line(points = {{324, 62}, {324, 138}}) );
end MyModel;
