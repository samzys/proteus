model  Modelica.Blocks.Math.InverseBlockConstraints inverseblockconstraints0 () annotation(Placement(transformation(origin = {-134, 61},
extent = {{-66, -39}, {66, 39}},
rotation = 0) ) );
Modelica.Blocks.Math.Gain gain0 (k = 1) annotation(Placement(transformation(origin = {-165, 211},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (gain0.y, inverseblockconstraints0.y1)annotation(Line(points = {{403.0, 188.0}, {403.0, 339.0}}) );
end ;
