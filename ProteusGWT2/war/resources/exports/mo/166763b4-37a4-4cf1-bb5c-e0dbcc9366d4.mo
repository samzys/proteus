model MyModel "description string"
Modelica.Electrical.Analog.Basic.Ground ground0 () annotation(Placement(transformation(origin = [183, 153],
extent = [-33, -33, 33, 33],
rotation = 0) ) );
Modelica.Blocks.Math.InverseBlockConstraints inverseblockconstraints0 () annotation(Placement(transformation(origin = [389, 89],
extent = [-66, -39, 66, 39],
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (ground0.p, inverseblockconstraints0.u1)annotation(Line(points = {{298, 153}, {298, 127}}) );
end MyModel;
