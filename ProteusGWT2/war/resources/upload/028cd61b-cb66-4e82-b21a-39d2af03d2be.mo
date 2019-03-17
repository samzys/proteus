model MyModel "description string"
   Modelica.Electrical.Analog.Basic.Capacitor capacitor0 () annotation(Placement(transformation(origin = {-204, 370},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
   Modelica.Electrical.Analog.Sources.ConstantVoltage constantvoltage0 () annotation(Placement(transformation(origin = {-51, 370},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
protected
   final inner parameter input Modelica.Electrical.Analog.Basic.Resistor resistor0 () annotation(Placement(transformation(origin = {-337, 371},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
   Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = {-316, 199},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (capacitor0.n, constantvoltage0.p)annotation(Line(points = {{-173, 370}}) );
connect (constantvoltage0.n, ground0.p)annotation(Line(points = {{6, 370}, {6, 229}}) );
connect (resistor0.n, capacitor0.p)annotation(Line(points = {{-270, 371}, {-270, 370}}) );
connect (resistor0.p, ground0.p)annotation(Line(points = {{-411, 371}, {-411, 229}}) );
end MyModel;
