model MyModel "description string"
Modelica.Blocks.Math.InverseBlockConstraints inverseblockconstraints0 () annotation(Placement(transformation(origin = {-339, 269},
extent = {{-66, -39}, {66, 39}},
rotation = 0) ) );
Modelica.Blocks.Math.Gain gain0 (k = 1) annotation(Placement(transformation(origin = {18, 124},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Mechanics.Translational.Components.Fixed fixed0 (s0 = 0) annotation(Placement(transformation(origin = {-344, -81},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (inverseblockconstraints0.y1, gain0.u)annotation(Line(points = {{354.0, 131.0}, {354.0, 275.0}}) );
connect (gain0.y, fixed0.flange)annotation(Line(points = {{354.0, 275.0}, {354.0, 480.0}}) );
connect (inverseblockconstraints0.u1, fixed0.flange)annotation(Line(points = {{121.0, 130.0}, {121.0, 480.0}}) );
end MyModel;
