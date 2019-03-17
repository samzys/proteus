model MyModel "description string"
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {-327, 370},
extent = {{-33, 33}, {33, -33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Resistor resistor0 (alpha = 0,
T_ref = 300.15,
R = 1) annotation(Placement(transformation(origin = {-158, 338},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Conductor conductor0 (G = 1,
alpha = 0,
T_ref = 300.15) annotation(Placement(transformation(origin = {-1, 338},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Inductor inductor0 (L = 1) annotation(Placement(transformation(origin = {-134, 97},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Sources.SignalVoltage signalvoltage0 () annotation(Placement(transformation(origin = {-392, 224},
extent = {{-33, -33}, {33, 33}},
rotation = 270) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (ground0.p, resistor0.p)annotation(Line(points = {{-254, 259}, {-254, 338}}) );
connect (resistor0.n, conductor0.p)annotation(Line(points = {{-90, 338}}) );
connect (conductor0.n, inductor0.n)annotation(Line(points = {{92, 338}, {92, 97}}) );
connect (signalvoltage0.p, ground0.p)annotation(Line(points = {{-392, 337.7}}) );
connect (signalvoltage0.n, inductor0.p)annotation(Line(points = {{-392, 97}}) );
end MyModel;
