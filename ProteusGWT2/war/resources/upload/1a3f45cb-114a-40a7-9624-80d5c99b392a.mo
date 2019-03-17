model MyModel "description string"
Modelica.Blocks.Sources.Sine sine0 (amplitude = 10,
freqHz = 5) annotation(Placement(transformation(origin = {-429, 274},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Sources.Torque torque1 () annotation(Placement(transformation(origin = {-291, 274},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Components.Fixed fixed0 () annotation(Placement(transformation(origin = {172, 36},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Components.Inertia inertia0 (J = 0.1) annotation(Placement(transformation(origin = {-154, 274},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Components.Inertia inertia1 (J = 2,
phi(start = 0) ,
w(start = 0) ) annotation(Placement(transformation(origin = {76, 274},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Components.IdealGear idealgear0 (ratio = 10) annotation(Placement(transformation(origin = {-52, 274},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Components.Inertia inertia2 (J = 2,
phi(start = 0) ,
w(start = 0) ) annotation(Placement(transformation(origin = {372, 274},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Components.Spring spring0 (c = 1.e4) annotation(Placement(transformation(origin = {218, 274},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Components.Damper damper0 (d = 10) annotation(Placement(transformation(origin = {172, 161},
extent = {{-30, -30}, {30, 30}},
rotation = 270) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (fixed0.flange, damper0.flange_b)annotation(Line(points = {{172, 131}}) );
connect (damper0.flange_a, spring0.flange_a)annotation(Line(points = {{172, 274}}) );
connect (inertia1.flange_b, spring0.flange_a)annotation(Line(points = {{106, 274}}) );
connect (spring0.flange_b, inertia2.flange_a)annotation(Line(points = {{248, 274}}) );
connect (idealgear0.flange_b, inertia1.flange_a)annotation(Line(points = {{-22, 274}}) );
connect (inertia0.flange_b, idealgear0.flange_a)annotation(Line(points = {{-124, 274}}) );
connect (torque1.flange, inertia0.flange_a)annotation(Line(points = {{-261, 274}}) );
connect (sine0.y, torque1.tau)annotation(Line(points = {{-396, 274}}) );
end MyModel;
