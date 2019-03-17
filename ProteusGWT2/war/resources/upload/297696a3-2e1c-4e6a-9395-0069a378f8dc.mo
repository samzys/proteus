model MyModel "description string"
Modelica.Blocks.Math.Gain gain0 (k = 1) annotation(Placement(transformation(origin = {70, 46},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
Modelica.Blocks.Math.InverseBlockConstraints inverseblockconstraints1 () annotation(Placement(transformation(origin = {150, 280},
extent = {{-66, -39}, {66, 39}},
rotation = 0) ) );
Modelica.Blocks.Math.InverseBlockConstraints inverseblockconstraints0 () annotation(Placement(transformation(origin = {-203, 171},
extent = {{-66, -39}, {66, 39}},
rotation = 270) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (inverseblockconstraints1.y1, gain0.y)annotation(Line(points = {{162.0, 280.0}, {162.0, 47.0}}) );
connect (inverseblockconstraints0.y1, gain0.u)annotation(Line(points = {{-199.0, 75.0}, {-199.0, 47.0}}) );
end MyModel;
