model MyModel "description string"
Modelica.Electrical.Analog.Basic.Capacitor capacitor1 (C = 1) annotation(Placement(transformation(origin = {300.0, 200.0},
extent = {{-30.0, -30.0}, {30.0, 30.0}},
rotation = 270) ) );
Modelica.Electrical.Analog.Basic.Resistor resistor1 (alpha = 0,
T_ref = 300.15,
R = 1) annotation(Placement(transformation(origin = {87.0, 296.0},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Ground ground1 () annotation(Placement(transformation(origin = {204.0, 260.0},
extent = {{-30.0, -30.0}, {30.0, 30.0}},
rotation = 0) ) );
Modelica.Electrical.Analog.Sources.ConstantVoltage voltage1 (V = 1) annotation(Placement(transformation(origin = {200.0, 100.0},
extent = {{-30.0, -30.0}, {30.0, 30.0}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (resistor1.n, ground1.p)annotation(Line(points = {{167.0, 296.0}, {167.0, 290.0}}) );
connect (capacitor1.n, voltage1.p)annotation(Line(points = {{265.0, 170.0}, {265.0, 100.0}}) );
connect (capacitor1.p, ground1.p)annotation(Line(points = {{252.0, 230.0}, {252.0, 290.0}}) );
connect (voltage1.n, resistor1.p)annotation(Line(points = {{57.0, 100.0}}) );
end MyModel;
