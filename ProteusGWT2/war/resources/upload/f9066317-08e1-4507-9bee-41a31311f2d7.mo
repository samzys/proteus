model MyModel "description string"
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {-295, 228},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.HeatingResistor heatingresistor0 (useHeatPort = true,
T = 300.15,
R_ref = 1,
alpha = 0,
T_ref = 300.15) annotation(Placement(transformation(origin = {-129, 272},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (ground0.p, heatingresistor0.p)annotation(Line(points = {{-232, 264.3}, {-232, 275.3}}) );
end MyModel;
