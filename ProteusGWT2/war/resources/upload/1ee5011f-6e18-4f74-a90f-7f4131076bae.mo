model MyModel "description string"
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {-439.0, 232.0},
extent = {{-33.0, -33.0}, {33.0, 33.0}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Resistor resistor0 (alpha = 0,
T_ref = 300.15,
R = 1) annotation(Placement(transformation(origin = {-331.0, 116.0},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Capacitor capacitor0 (C = 1) annotation(Placement(transformation(origin = {-282.0, 355.0},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Sources.ConstantVoltage constantvoltage0 (V = 1) annotation(Placement(transformation(origin = {-121.0, 158.0},
extent = {{-33, -33}, {33, 33}},
rotation = 180) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (ground0.p, capacitor0.p)annotation(Line(points = {{-336.0, 265.0}, {-336.0, 355.0}}) );
connect (capacitor0.n, constantvoltage0.p)annotation(Line(points = {{-47.0, 355.0}, {-47.0, 241.0}, {-97.0, 241.0}, {-97.0, 158.0}}) );
connect (constantvoltage0.n, resistor0.n)annotation(Line(points = {{-228.0, 158.0}, {-228.0, 116.0}}) );
connect (resistor0.p, ground0.p)annotation(Line(points = {{-362.0, 116.0}, {-362.0, 265.0}}) );
end MyModel;
