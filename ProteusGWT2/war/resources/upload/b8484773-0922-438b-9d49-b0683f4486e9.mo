model MyModel "description string"
Modelica.Blocks.Math.InverseBlockConstraints inverseblockconstraints0 () annotation(Placement(transformation(origin = {-134, 61},
extent = {{-66, -39}, {66, 39}},
rotation = 0) ) );
Modelica.Blocks.Math.Gain gain0 (k = 1) annotation(Placement(transformation(origin = {-182, 220},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (gain0.u, inverseblockconstraints0.u1)annotation(Line(points = {{285.0, 179.0}, {285.0, 338.0}}) );
end MyModel;
