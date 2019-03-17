model MyModel "description string"
Modelica.Electrical.Analog.Basic.Resistor resistor0 (alpha = 0,
T_ref = 300.15,
R = 1) annotation(Placement(transformation(origin = {-70, 75},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {-223, 189},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Sources.ConstantVoltage constantvoltage0 (V = 1) annotation(Placement(transformation(origin = {140, 81},
extent = {{-33, -33}, {33, 33}},
rotation = 180) ) );
Modelica.Electrical.Analog.Basic.Capacitor capacitor0 (C = 1) annotation(Placement(transformation(origin = {103, 328},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (capacitor0.n, constantvoltage0.p)annotation(Line(points = {{136, 211}, {173, 211}}) );
connect (ground0.p, capacitor0.p)annotation(Line(points = {{-223, 328}}) );
connect (constantvoltage0.n, resistor0.n)annotation(Line(points = {{-37, 81}}) );
connect (resistor0.p, ground0.p)annotation(Line(points = {{-223, 75}}) );
end MyModel;
