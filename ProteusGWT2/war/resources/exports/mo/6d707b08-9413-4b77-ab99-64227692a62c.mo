model momo Modelica.Blocks.Math.Gain gain0 (k = 1) annotation(Placement(transformation(origin = {-167, 67},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Blocks.Math.InverseBlockConstraints inverseblockconstraints0 () annotation(Placement(transformation(origin = {-50, 162},
extent = {{-66, -39}, {66, 39}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (inverseblockconstraints0.u1, gain0.y)annotation(Line(points = {{373.0, 237.0}, {373.0, 332.0}}) );
end momo;
