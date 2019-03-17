model 2.mo "description string"
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
T_ref = 300.15) annotation(Placement(transformation(origin = {-7, 127},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Electrical.Analog.Basic.Ground ground1 () annotation(Placement(transformation(origin = {-193, 94},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (ground0.p, resistor0.p)annotation(Line(points = {{-376, 325}}) );
connect (resistor0.n, heatingresistor0.n)annotation(Line(points = {{85, 325}, {85, 127}}) );
connect (heatingresistor0.p, ground1.p)annotation(Line(points = {{-120, 113}, {-120, 127}}) );
connect (ground0.p, ground1.p)annotation(Line(points = {{-286, 127}, {-286, 128}}) );
end 2.mo;
