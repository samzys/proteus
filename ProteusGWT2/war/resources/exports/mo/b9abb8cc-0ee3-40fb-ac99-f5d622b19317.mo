model MyModel "description string"
Modelica.Blocks.Math.InverseBlockConstraints inverseblockconstraints0 () annotation(Placement(transformation(origin = [218, 261],
extent = [-66, -39, 66, 39],
rotation = 0) ) );
Modelica.Blocks.Math.Gain gain0 (k = 1) annotation(Placement(transformation(origin = [391, 107],
extent = [-33, -33, 33, 33],
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (inverseblockconstraints0.y1, gain0.u)annotation(Line(points = {{368, 300}, {368, 139}}) );
end MyModel;
